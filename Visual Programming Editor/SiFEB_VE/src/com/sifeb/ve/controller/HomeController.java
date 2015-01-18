/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.controller;

import com.sifeb.ve.MainApp;
import com.sifeb.ve.handle.SoundHandler;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class for Home Screen
 *
 * @author Hashini Senaratne
 */
public class HomeController implements Initializable {

    @FXML
    ToggleButton musicBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        musicBtn.setSelected(SoundHandler.isMusicOn());
        SoundHandler.playBackMusic();

    }

    @FXML
    private void goToMainEditor1(ActionEvent event) {
        goToMainEditor(1);
    }

    @FXML
    private void goToMainEditor2(ActionEvent event) {
        goToMainEditor(2);
    }

    @FXML
    private void goToMainEditor3(ActionEvent event) {
        goToMainEditor(3);
    }

    @FXML
    private void toggleMusic(ActionEvent event) {
        if (musicBtn.isSelected()) {
            SoundHandler.setMusicOn(true);
            SoundHandler.playBackMusic();
        } else {            
            SoundHandler.setMusicOn(false);
            SoundHandler.stopBackMusic();
        }
    }

    private void goToMainEditor(int level) {
        try {
            FXMLLoader loader = new FXMLLoader();
            RootController.setLevel(level);
            loader.setLocation(MainApp.class.getResource(MainApp.RootFile));
            MainApp.setPane((Pane) loader.load());
            Scene scene = new Scene(MainApp.getPane());
            MainApp.getStage().setScene(scene);
            MainApp.getStage().setMaximized(true);
            MainApp.getStage().setResizable(true);
            MainApp.getStage().show();

            SoundHandler.stopBackMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToTutorials(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(MainApp.TutorialFile));
            MainApp.setPane((Pane) loader.load());
            Scene scene = new Scene(MainApp.getPane());
            MainApp.getStage().setScene(scene);
            MainApp.getStage().setMaximized(false);
            MainApp.getStage().setResizable(false);
            MainApp.getStage().setWidth(MainApp.InitialScreenWidth);
            MainApp.getStage().setHeight(MainApp.InitialScreenHeight);
            MainApp.getStage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAPI(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(MainApp.LibraryFile));
            MainApp.setPane((Pane) loader.load());
            Scene scene = new Scene(MainApp.getPane());
            MainApp.getStage().setScene(scene);
            MainApp.getStage().setMaximized(false);
            MainApp.getStage().setResizable(false);
            MainApp.getStage().show();

            SoundHandler.stopBackMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToHome(ActionEvent event) {
    }
}
