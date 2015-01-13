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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 *
 * @author Udith Arosha
 * @author Hashini Senaratne
 */
public class RootController implements Initializable {
    
    @FXML
    BorderPane rootPane;
    @FXML
    RadioMenuItem langMenuEng;
    @FXML
    RadioMenuItem langMenuSin;
    @FXML
    MenuItem connectMenu;
    @FXML
    MenuItem aboutMenuItem;
    @FXML
    MenuItem libEditMenu;

    MainEditorController meCtrl;
    Stage libEditStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setEventHandlers();
        loadMainEditor();
//        loadGameEditor();
        //TEST
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(MainApp.class.getResource("view/LibraryEditor.fxml"));
//        try {
//            AnchorPane rootLayout = (AnchorPane) loader.load();
//            Stage stage = new Stage();
//            stage.setTitle("SiFEB Library Editor");
//            stage.setScene(new Scene(rootLayout));
//            stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/com/sifeb/ve/images/appIcon.png")));
//                
//            stage.show();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
        //
    }

    private void setEventHandlers() {
        langMenuEng.setOnAction((ActionEvent event) -> {
            changeLanguage(new Locale("en", "US"));
        });

        langMenuSin.setOnAction((ActionEvent event) -> {
            changeLanguage(new Locale("si", "LK"));
        });
        connectMenu.setOnAction((ActionEvent event) -> {
            try {
                ComPortController.closePort();

                Thread.sleep(2000);

                ComPortController.openPort();
            } catch (InterruptedException ex) {
                Logger.getLogger(RootController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        aboutMenuItem.setOnAction((ActionEvent event) -> {
            final Popup popup = new Popup();
            popup.setAutoHide(true);
            popup.setWidth(500);
            popup.setHeight(300);
            popup.centerOnScreen();

            ImageView iv = new ImageView(new Image(MainApp.class.getResourceAsStream("/com/sifeb/ve/images/about.png")));
            popup.getContent().add(iv);

            Label urlLabel = new Label("www.sifebplaymate.com");
            urlLabel.setFont(new Font(24));
            urlLabel.setTextFill(Color.ORANGE);
            urlLabel.relocate(120, 210);
            popup.getContent().add(urlLabel);

            Button closeBtn = new Button("Close");
            closeBtn.setPrefWidth(80);
            closeBtn.relocate(200, 260);
            closeBtn.setOnAction((ActionEvent e) -> {
                popup.hide();
            });
            popup.getContent().add(closeBtn);

            popup.show(rootPane.getScene().getWindow());
        });

        libEditMenu.setOnAction((ActionEvent event) -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/LibraryEditor.fxml"));
            try {
                if (libEditStage == null) {
                    AnchorPane rootLayout = (AnchorPane) loader.load();
                    libEditStage = new Stage();
                    libEditStage.setTitle("SiFEB Library Editor");
                    libEditStage.setScene(new Scene(rootLayout));
                    libEditStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/com/sifeb/ve/images/appIcon.png")));
                    libEditStage.show();
                } else {
                    libEditStage.toFront();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }

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

            ComPortController.setBlockCreator(blkCreator);
            ComPortController.openPort();
            // ComPortController.setEventListener();
            blkCreator.addDefaultCapabilities();

            //for test only
            blkCreator.createDeviceBlock("10", "10");
            blkCreator.createDeviceBlock("11", "11");
            rootPane.setCenter(mainEditor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadGameEditor() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/GameEditor.fxml"));
            AnchorPane gameEditor = (AnchorPane) loader.load();
            meCtrl = loader.getController();
            BlockCreator blkCreator = new BlockCreator(meCtrl);

            ComPortController.setBlockCreator(blkCreator);
            ComPortController.openPort();
            // ComPortController.setEventListener();
            blkCreator.addDefaultCapabilities();

            //for test only
            blkCreator.createDeviceBlock("10", "10");
            blkCreator.createDeviceBlock("11", "11");
            rootPane.setCenter(gameEditor);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
