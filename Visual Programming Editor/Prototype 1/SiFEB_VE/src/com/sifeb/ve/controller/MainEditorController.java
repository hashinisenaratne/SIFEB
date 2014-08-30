/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.controller;

import com.sifeb.ve.ActuatorBlock;
import com.sifeb.ve.Block;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Udith Arosha
 */
public class MainEditorController implements Initializable {

    @FXML
    Button addActuatorBtn;
    @FXML
    Button addSensorBtn;
    @FXML
    Button addCapBtn;
    @FXML
    Button addSenseBtn;
    @FXML
    VBox actuatorBox;
    @FXML
    VBox sensorBox;
    @FXML
    VBox capabilityBox;
    @FXML
    VBox senseBox;
   

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setEventHandlers();
    }

    private void setEventHandlers() {
        addActuatorBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                addBlock(actuatorBox);
            }
        });
        addSensorBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                addBlock(sensorBox);
            }
        });
        addCapBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                addBlock(capabilityBox);
            }
        });
        addSenseBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                addBlock(senseBox);
            }
        });
    }

    private void addBlock(VBox parent) {
       
        Image img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/Mwheels.png"));
        Image btnImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/MSPlay.png"));
        Button button = new Button();       

        parent.getChildren().add(new Block("rectangle", img, button,btnImg));
    }

}
