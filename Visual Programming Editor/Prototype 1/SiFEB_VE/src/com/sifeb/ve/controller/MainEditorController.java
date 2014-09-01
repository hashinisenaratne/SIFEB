/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.controller;

import com.sifeb.ve.ActionBlock;
import com.sifeb.ve.ActuatorBlock;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Udith Arosha
 */
public class MainEditorController implements Initializable {

    @FXML
    Button addDeviceBtn;
    @FXML
    Button addCapBtn;
    @FXML
    Button addHolderBtn;
    @FXML
    VBox devicesBox;
    @FXML
    VBox capabilityBox;
    @FXML
    Pane editorPane;
    @FXML
    HBox editorBox;

    ArrayList<ActuatorBlock> holders;
    ActuatorBlock lastHolder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        holders = new ArrayList<>();
        editorBox.setSpacing(-15);
//        addStartEndBlocks();
//        addBlockHolder();

        setEventHandlers();
    }

    private void addStartEndBlocks() {
        Image img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/start.png"));
        editorBox.getChildren().add(new ActuatorBlock("rectangle", img, null));

        img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/stop.png"));
        editorBox.getChildren().add(new ActuatorBlock("rectangle", img, null));
    }

//    private void addBlockHolder() {
//        Image img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/NBlock_copy.png"));
//        ActuatorBlock holder = new ActuatorBlock("rectangle", img, null);
//        holders.add(holder);
//        editorBox.getChildren().add(editorBox.getChildren().size() - 1, holder);
//        lastHolder = holder;
//    }

    private void setEventHandlers() {
        addDeviceBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                addBlock(devicesBox);
            }
        });
        addCapBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                addBlock(capabilityBox);
            }
        });
        addHolderBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
//                addBlockHolder();
            }
        });

        editorPane.setOnDragDropped((DragEvent event) -> {
            System.out.println("dropped");
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String nodeId = db.getString();
                ActuatorBlock draggedBlock = lookUpForBlock(nodeId);

                if (draggedBlock != null) {
                    if (draggedBlock.getParent().getId().equals("editorPane")) {
                        editorPane.getChildren().remove(draggedBlock);
                    } else {
                        draggedBlock = new ActuatorBlock(draggedBlock.getType(), draggedBlock.getBlockImg(), draggedBlock.getBtnImg());
                        draggedBlock.setId(Integer.toHexString(draggedBlock.hashCode()));
                    }
                    editorPane.getChildren().add(draggedBlock);
                    draggedBlock.setLayoutX(event.getX());
                    draggedBlock.setLayoutY(event.getY());
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        editorPane.setOnDragOver((DragEvent event) -> {
            if (event.getGestureSource() != editorPane
                    && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        editorBox.setOnDragDropped((DragEvent event) -> {
            System.out.println("hb dropped");
        });

        editorBox.setOnDragOver((DragEvent event) -> {
            if (event.getGestureSource() != editorBox
                    && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.NONE);
            }
            event.consume();
        });
    }

//    private Rectangle lookUpForBlock(String nodeId) {
//        Rectangle actBlock = (Rectangle) capabilityBox.lookup("#" + nodeId);
//        if (actBlock == null) {
//            actBlock = (Rectangle) editorPane.lookup("#" + nodeId);
//        }
//        System.out.println("lookup:::"+actBlock);
//        return actBlock;
//    }

    private ActuatorBlock lookUpForBlock(String nodeId) {
        System.out.println(capabilityBox.lookup("#" + nodeId));
                
        ActionBlock actBlock = (ActionBlock) (capabilityBox.lookup("#" + nodeId));
        ActuatorBlock draggedBlock = null;
        if (actBlock != null) {
            draggedBlock = actBlock.getActuatorBlock();
        }
        else
        {
            draggedBlock = (ActuatorBlock)(editorPane.lookup("#" + nodeId));
        }
        System.out.println("lookup:::"+actBlock);
        
        return draggedBlock;
    }
    
    //for testing only
    private void addBlock(VBox parent) {

        System.out.println(parent.getId());
        String parentId = parent.getId();

        if (parentId.equals("devicesBox")) {

            Image img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/Mwheels.png"));
            Image btnImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/MAPlay.png"));

            parent.getChildren().add(new ActuatorBlock("rectangle", img, btnImg));

            img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/Msonar.png"));
            btnImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/MSPlay.png"));

            parent.getChildren().add(new ActuatorBlock("rectangle", img, btnImg));

            img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/Mlight.png"));
            btnImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/MAPlay.png"));

            parent.getChildren().add(new ActuatorBlock("rectangle", img, btnImg));

        } else if (parentId.equals("capabilityBox")) {

            Image fullImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/ActionTest.png"));
            Image btnnImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/ActionTestBtn.png"));
            Image blkImg;
            String sensorImage;

            for (int i = 1; i < 8; i++) {

                sensorImage = "ActionA" + String.valueOf(i);
                blkImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/" + sensorImage + ".png"));
                parent.getChildren().add(new ActionBlock("rectangle", fullImg, blkImg, btnnImg));
            }

        }

    }

}
