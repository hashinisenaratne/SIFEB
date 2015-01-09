/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.controller;

import com.sifeb.ve.MainApp;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class for Tutorials Screen
 *
 * @author Hashini Senaratne
 */
public class TutorialController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
            MainApp.getStage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToLearn(ActionEvent event) {
    }
}
