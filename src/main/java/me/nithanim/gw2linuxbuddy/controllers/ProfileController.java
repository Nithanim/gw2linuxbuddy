package me.nithanim.gw2linuxbuddy.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Getter;

public class ProfileController implements Initializable {
  @Getter @FXML Parent root;
  @FXML Label lblName;
  @Getter @FXML Button btnPlay;

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  public void initialize(String name) {
    lblName.setText(name);
  }
}
