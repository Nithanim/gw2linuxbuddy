package me.nithanim.gw2linuxbuddy.controllers;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.inject.Singleton;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import lombok.SneakyThrows;
import me.nithanim.gw2linuxbuddy.FxMain;
import me.nithanim.gw2linuxbuddy.Gw2InstanceBuilder;
import me.nithanim.gw2linuxbuddy.Gw2InstanceStartService;
import me.nithanim.gw2linuxbuddy.InstanceDataFolder;
import me.nithanim.gw2linuxbuddy.settings.Settings;

@Singleton
public class MainController implements Initializable {
  @FXML ListView<ProfileController> lvProfiles;

  @FXML Button btnSettings;

  @Inject Settings settings;
  @Inject Gw2InstanceStartService gw2InstanceStartService;

  private final Map<String, Task<?>> tasks = new HashMap<>();

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    lvProfiles.setCellFactory(listView -> new CustomListCell());

    appendProfile(createProfile("main", "Main"));
    //appendProfile(createProfile("2", "2"));
    appendProfile(createProfile("3", "3 (PoF)"));
    appendProfile(createProfile("4", "4 (PoF)"));
    appendProfile(createProfile("11", "11 (PoF)"));
    appendProfile(createProfile("12", "12 (PoF)"));
    appendProfile(createProfile("5", "5"));
    appendProfile(createProfile("6", "6"));
    appendProfile(createProfile("8", "8"));
    appendProfile(createProfile("9", "9"));
    appendProfile(createProfile("10", "10"));

    settings.init();
    btnSettings.setOnAction(e -> settings.show());
  }

  private void appendProfile(ProfileController profile) {
    lvProfiles.getItems().add(profile);
  }

  @SneakyThrows
  private ProfileController createProfile(String instanceId, String name) {
    ProfileController controller = new ProfileController();

    FXMLLoader fxmlLoader = new FXMLLoader();
    fxmlLoader.setClassLoader(Thread.currentThread().getContextClassLoader());
    fxmlLoader.setLocation(FxMain.class.getClassLoader().getResource("/fxml/profile.fxml"));
    fxmlLoader.setController(controller);
    Parent parent = fxmlLoader.load();
    controller.initialize(name);

    controller
        .getBtnPlay()
        .setOnMousePressed(
            e -> {
              onProfileButton(instanceId, controller);
            });

    return controller;
  }

  private void onProfileButton(String instanceId, ProfileController controller) {
    if (tasks.containsKey(instanceId)) {
      Task<?> task = tasks.get(instanceId);
      task.cancel(true);
    } else {
      controller.getBtnPlay().setText("…");
      Task<Void> task =
          new Task<>() {
            @Override
            protected Void call() throws Exception {
              Gw2InstanceStartService.RunningInstance process = startProfile(instanceId);
              try {
                process.getGameProcess().waitFor();
              } catch (InterruptedException ex) {
                process.stop();
              }
              return null;
            }
          };
      task.setOnRunning(e -> controller.getBtnPlay().setText("■"));
      EventHandler<WorkerStateEvent> onTaskEnd =
          e -> {
            controller.getBtnPlay().setText("▶");
            tasks.remove(instanceId);
          };
      task.setOnSucceeded(onTaskEnd);
      task.setOnCancelled(onTaskEnd);
      task.setOnFailed(onTaskEnd);
      tasks.put(instanceId, task);
      Thread t = new Thread(task);
      t.setName("game-" + instanceId);
      t.start();
    }
  }

  private Gw2InstanceStartService.RunningInstance startProfile(String instanceId)
      throws IOException {
    Path dataDir = Path.of("data");

    Map<String, String> additionalEnv =
        Map.ofEntries(
            Map.entry("WINEDEBUG", "-all"),
            Map.entry("WINEFSYNC", "1"),
            Map.entry("WINEESYNC", "1"),
            Map.entry("DXVK_ASYNC", "1"),
            Map.entry("STAGING_SHARED_MEMORY", "1"),
            Map.entry("DXVK_HUD", "devinfo,fps"),
            Map.entry("ENABLE_DEVICE_CHOOSER_LAYER", "1"),
            Map.entry("VULKAN_DEVICE_INDEX", "1"),
            Map.entry("MESA_GLSL_CACHE_DIR", "/tmp"));

    String gameargs = "-autologin -bmp -mapLoadinfo";

    var b = Gw2InstanceBuilder.builder();
    b.environmentParameters(additionalEnv);
    b.arguments(gameargs);
    b.executablePath(settings.getGameExe());
    b.winePath(settings.getWineExePath());
    b.instanceId(instanceId);
    b.instanceDataFolder(new InstanceDataFolder(instanceId, dataDir.resolve("instances")));

    return gw2InstanceStartService.startGame(b.build());
  }
}
