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
 * FXML Controller class for Home Screen
 *
 * @author Hashini Senaratne
 */
public class HomeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void goToMainEditor1(ActionEvent event){
            goToMainEditor(1);
    }
    
    @FXML
    private void goToMainEditor2(ActionEvent event){
            goToMainEditor(2);
    }
    
    @FXML
    private void goToMainEditor3(ActionEvent event){
            goToMainEditor(3);
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
            MainApp.getStage().setWidth(800);
            MainApp.getStage().setHeight(600);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
        @FXML
    private void goToHome(ActionEvent event) {
    }
}
