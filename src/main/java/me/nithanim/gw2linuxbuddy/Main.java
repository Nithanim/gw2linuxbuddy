package me.nithanim.gw2linuxbuddy;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import javafx.application.Application;

@QuarkusMain
public class Main implements QuarkusApplication {
  public static void main(String... args) {
    Quarkus.run(Main.class, args);
  }

  @Override
  public int run(String... args) {
    Application.launch(FxMain.class, args);
    return 0;
  }
}
