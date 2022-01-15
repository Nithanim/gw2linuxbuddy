package me.nithanim.gw2linuxbuddy;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.AnnotationLiteral;

import javafx.application.Application;
import javafx.stage.Stage;
import me.nithanim.gw2linuxbuddy.conf.StartupScene;

@ApplicationScoped
public class FxMain extends Application {

  @Override
  public void start(Stage primaryStage) {
    CDI.current()
        .getBeanManager()
        .fireEvent(primaryStage, new AnnotationLiteral<StartupScene>() {});
  }
}
