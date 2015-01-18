/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.controller;

import com.sifeb.ve.GameList;
import com.sifeb.ve.MainApp;
import com.sifeb.ve.handle.FileHandler;
import com.sifeb.ve.handle.SoundHandler;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * FXML Controller class for Tutorials Screen
 *
 * @author Hashini Senaratne
 */
public class TutorialController implements Initializable {

    @FXML
    AnchorPane learn;
    @FXML
    Button learnBtn;
    
    private GameList gamelist;
    private static int TutorialLevel;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SoundHandler.playBackMusic();
        FileHandler fileHandler = new FileHandler();
        gamelist = fileHandler.readFromGameListFile(TutorialLevel);
        if (TutorialLevel == 2) {
            learnBtn.setText("Learn - Level 2");
        } else if (TutorialLevel == 3) {
            learnBtn.setText("Learn - Level 3");
        }
    }

    @FXML
    private void goToHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(MainApp.HomeFile));
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
    private void goToLearn(ActionEvent event) {
    }
    
    @FXML
    private void pageUp(ActionEvent event) {
        FadeTransition ft = new FadeTransition(Duration.millis(3000), learn);
        ft.setFromValue(0.1);
        ft.setToValue(1.0);
        ft.play();
    }
    
    @FXML
    private void pageDown(ActionEvent event) {
        FadeTransition ft = new FadeTransition(Duration.millis(3000), learn);
        ft.setFromValue(0.1);
        ft.setToValue(1.0);
        ft.play();
    }
    
    public static void setLevel(int level) {
        TutorialLevel = level;
    }
    
}
