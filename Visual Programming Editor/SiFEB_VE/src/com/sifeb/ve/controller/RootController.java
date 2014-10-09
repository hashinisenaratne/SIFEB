/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.controller;

import com.sifeb.ve.MainApp;
import com.sifeb.ve.handle.BlockCreator;
import com.sifeb.ve.resources.Strings;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Udith Arosha
 */
public class RootController implements Initializable {

    @FXML
    BorderPane rootPane;
    @FXML
    RadioMenuItem langMenuEng;
    @FXML
    RadioMenuItem langMenuSin;

    MainEditorController meCtrl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setEventHandlers();
        loadMainEditor();
    }

    private void setEventHandlers() {
        langMenuEng.setOnAction((ActionEvent event) -> {
            changeLanguage(new Locale("en", "US"));
        });

        langMenuSin.setOnAction((ActionEvent event) -> {
            changeLanguage(new Locale("si", "LK"));
        });
    }

    private void changeLanguage(Locale l) {
        Strings.setLocale(l);
        meCtrl.setTextStrings();
    }

    private void loadMainEditor() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/MainEditor.fxml"));
            AnchorPane mainEditor = (AnchorPane) loader.load();
            meCtrl = loader.getController();
            BlockCreator blkCreator = new BlockCreator(meCtrl);

            ComPortController.openPort();
            ComPortController.setBlockCreator(blkCreator);
            ComPortController.setEventListener();
            blkCreator.createCapability("cap_def1", null);
            blkCreator.createCapability("cap_def2", null);
            
            //for test only
        //    blkCreator.createBlock("11");
          //  blkCreator.createBlock("10");
            
            rootPane.setCenter(mainEditor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
