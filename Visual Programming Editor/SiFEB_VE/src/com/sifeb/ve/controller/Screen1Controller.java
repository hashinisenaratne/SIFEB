/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sifeb.ve.controller;

import com.sifeb.ve.MainApp;
import com.sifeb.ve.ScreensController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 *
 * @author Hashini Senaratne
 */
public class Screen1Controller implements Initializable, ControlledScreen{ 

     ScreensController myController;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }

    @FXML
    private void goToScreen0(ActionEvent event){
       MainApp.getStage().setMaximized(true);
       myController.setScreen(com.sifeb.ve.MainApp.screen0ID);
    }
    
    @FXML
    private void goToScreen2(ActionEvent event){
        MainApp.getStage().setMaximized(false);
        myController.setScreen(com.sifeb.ve.MainApp.screen2ID);
    }
    
    @FXML
    private void goToScreen3(ActionEvent event){
       MainApp.getStage().setMaximized(false);
       myController.setScreen(com.sifeb.ve.MainApp.screen3ID);
    }
}
