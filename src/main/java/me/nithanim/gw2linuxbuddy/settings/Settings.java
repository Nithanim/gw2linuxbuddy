package me.nithanim.gw2linuxbuddy.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Setting;

import io.vavr.control.Either;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;

@Singleton
public class Settings {
  private final SimpleObjectProperty<File> gameExePath = new SimpleObjectProperty<>(null);
  private final SimpleObjectProperty<File> wineExePath = new SimpleObjectProperty<>(null);
  private final SimpleObjectProperty<File> wineserverExePath = new SimpleObjectProperty<>(null);
  private PreferencesFx preferences;
  private Label wineVersion;

  public void init() {
    wineVersion = new Label();
    wineExePath.addListener(observable -> updateWineVersionDisplay());
    wineserverExePath.addListener(observable -> updateWineVersionDisplay());
    updateWineVersionDisplay();

    preferences =
        PreferencesFx.of(
            Settings.class,
            Category.of(
                "General",
                Setting.of("GW2 executable", gameExePath, false),
                Setting.of("Wine executable", wineExePath, false),
                Setting.of("Wineserver executable", wineserverExePath, false),
                Setting.of(wineVersion)));
    preferences.instantPersistent(false);
    preferences.saveSettings(true);
  }

  private boolean isSet(File value) {
    return value != null && !value.equals(new File(""));
  }

  private void updateWineVersionDisplay() {
    wineVersion.setText(
        "Wine: "
            + getWineVersionAsText(wineExePath.getValue())
            + "\nWineserver: "
            + getWineVersionAsText(wineserverExePath.getValue()));
  }

  public void show() {
    preferences.show(true);
  }

  private String getWineVersionAsText(File file) {
    Optional<Either<Execution, Exception>> e = getWineVersion(file);
    if (e.isPresent()) {
      Either<Execution, Exception> o = e.get();
      if (o.isLeft()) {
        Execution l = o.getLeft();
        if (l.exitCode() != 0) {
          return "ExitCode: " + l.exitCode() + "; Output: " + l.output();
        } else {
          return l.output();
        }
      } else {
        return o.swap().getLeft().getMessage();
      }
    } else {
      return "?";
    }
  }

  private Optional<Either<Execution, Exception>> getWineVersion(File file) {
    if (!isSet(file)) {
      return Optional.empty();
    }
    Path path = file.toPath();
    if (!Files.exists(path)) {
      return Optional.of(Either.right(new FileNotFoundException(path.toString())));
    }
    ProcessBuilder pb = new ProcessBuilder(path.toAbsolutePath().toString(), "--version");
    pb.redirectOutput(ProcessBuilder.Redirect.PIPE);
    try {
      Process process = pb.start();
      boolean hasExited = process.waitFor(2, TimeUnit.SECONDS);
      if (!hasExited) {
        process.destroyForcibly();
      }
      int exitValue = process.exitValue();
      String output =
          new String(process.getInputStream().readAllBytes())
              + new String(process.getErrorStream().readAllBytes());
      return Optional.of(Either.left(new Execution(exitValue, output)));
    } catch (IOException | InterruptedException ex) {
      return Optional.of(Either.right(ex));
    }
  }

  public Path getWineExePath() {
    return wineExePath.getValue() != null ? wineExePath.getValue().toPath() : null;
  }

  public Path getGameExe() {
    return gameExePath.getValue() != null ? gameExePath.getValue().toPath() : null;
  }

  private record Execution(int exitCode, String output) {}
}
