package me.nithanim.gw2linuxbuddy.view;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import me.nithanim.gw2linuxbuddy.FxMain;
import me.nithanim.gw2linuxbuddy.conf.StartupScene;
import me.nithanim.gw2linuxbuddy.controllers.MainController;

public class App {

  @Inject FXMLLoader fxmlLoader;
  @Inject MainController mainController;

  @SneakyThrows
  public void start(@Observes @StartupScene Stage stage) {
    fxmlLoader.setLocation(FxMain.class.getClassLoader().getResource("/fxml/main.fxml"));
    fxmlLoader.setController(mainController);
    Parent parent = fxmlLoader.load();
    stage.setScene(new Scene(parent, 300, 250));
    stage.setTitle("GW2 Linux Buddy");
    stage.show();
  }
}
