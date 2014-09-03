/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.controller;

import com.sifeb.ve.ActionBlock;
import com.sifeb.ve.ActuatorBlock;
import com.sifeb.ve.ConditionBlock;
import com.sifeb.ve.Holder;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
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

    ArrayList<Holder> holders;
    Holder lastHolder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        holders = new ArrayList<>();
        editorBox.setSpacing(-17);
        addStartEndBlocks();
        addBlockHolder(0, false);
        addBlock(devicesBox);
        addBlock(capabilityBox);

        setEventHandlers();
    }

    private void addStartEndBlocks() {
        Image img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/Start.png"));
        editorBox.getChildren().add(new ActuatorBlock("rectangle", img, null, false));

        img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/Stop.png"));
        editorBox.getChildren().add(new ActuatorBlock("rectangle", img, null, false));
    }

    //use -1 as index to add a holder to the end
    private void addBlockHolder(int index, boolean withCondition) {
        Holder holder = new Holder(this);
        if (withCondition) {
            holder = new ConditionBlock(this);
        }
        if (index >= 0) {
            holders.add(index, holder);
        } else {
            holders.add(holder);
        }
        editorBox.getChildren().remove(1, editorBox.getChildren().size() - 1);
        editorBox.getChildren().addAll(1, holders);
    }

    public void changeHolderType(Holder holder, Node node) {
        String holderType = holder.getClass().getName();
        int index = holders.indexOf(holder);
        holders.remove(index);
        if (holderType.contains("Holder")) {
            addBlockHolder(index, true);
        } else {
            addBlockHolder(index, false);
        }
        if (node != null) {
            holders.get(index).addElementToVbox(node);
        }
    }

    public void addHolderAfterMe(Holder holder) {
        int index = holders.indexOf(holder);
        //if it's the last holder
        if (index == (holders.size() - 1)) {
            addBlockHolder(-1, false);
        }
    }

    public void deleteHolder(Holder holder) {
        int index = holders.indexOf(holder);
        //if there are more than 1 holders
        if (holders.size() > 1) {
            holders.remove(index);
            editorBox.getChildren().remove(1, editorBox.getChildren().size() - 1);
            editorBox.getChildren().addAll(1, holders);
        } else {
            System.out.println("Last holder cannot be deleted");
        }
    }

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
                addBlockHolder(-1, false);
            }
        });

        editorPane.setOnDragDropped((DragEvent event) -> {
//            System.out.println("dropped");
            Dragboard db = event.getDragboard();
            Parent p = ((Node) event.getGestureSource()).getParent();
            boolean success = false;
            if (db.hasString()) {
                String nodeId = db.getString();
                ActuatorBlock draggedBlock = (ActuatorBlock) p.lookup("#" + nodeId);

                if (draggedBlock != null) {
//                    System.out.println(p.getClass().getName());
                    if (p.getClass().getName().contains("ActionBlock")) {
                        draggedBlock = new ActuatorBlock(draggedBlock.getType(), draggedBlock.getBlockImg(), draggedBlock.getBtnImg(), draggedBlock.isDragable());
                    } else {
                        ((Pane) p).getChildren().remove(draggedBlock);
                    }

                    editorPane.getChildren().add(draggedBlock);
                    draggedBlock.setLayoutX(event.getX());
                    draggedBlock.setLayoutY(event.getY());
                    if (draggedBlock.getType().equals("condition")) {
                        draggedBlock.disableTextField(true);
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        editorPane.setOnDragOver((DragEvent event) -> {
            if (event.getDragboard().hasString()) {
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

    //for testing only
    private void addBlock(VBox parent) {

        System.out.println(parent.getId());
        String parentId = parent.getId();

        if (parentId.equals("devicesBox")) {

            Image img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/Mwheels.png"));
            Image btnImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/MAPlay.png"));

            parent.getChildren().add(new ActuatorBlock("device", img, btnImg, false));

            img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/Msonar.png"));
            btnImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/MSPlay.png"));

            parent.getChildren().add(new ActuatorBlock("device", img, btnImg, false));

            img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/Mlight.png"));
            btnImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/MAPlay.png"));

            parent.getChildren().add(new ActuatorBlock("device", img, btnImg, false));

        } else if (parentId.equals("capabilityBox")) {

            Image fullImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/ActionTest.png"));
            Image btnnImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/ActionTestBtn.png"));
            Image blkImg;
            String sensorImage;
            String[] strList = {"b", "c", "e", "d", "err", "errLight", "errLight"};

            //adding actions
            for (int i = 1; i < 8; i++) {
                sensorImage = "ActionA" + String.valueOf(i);
                blkImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/" + sensorImage + ".png"));

                if (i < 3 || i > 5) {
                    parent.getChildren().add(new ActionBlock("actionC", strList[i-1], fullImg, blkImg, btnnImg));
                } else {
                    parent.getChildren().add(new ActionBlock("action", strList[i-1], fullImg, blkImg, btnnImg));
                }

            }

            //adding conditions
            blkImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/Constraint1.png"));
            parent.getChildren().add(new ActionBlock("condition", "err", fullImg, blkImg, btnnImg));
            blkImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/Constraint2.png"));
            parent.getChildren().add(new ActionBlock("condition", "err", fullImg, blkImg, btnnImg));
            blkImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/Constraint3.png"));
            parent.getChildren().add(new ActionBlock("sense", "err", fullImg, blkImg, btnnImg));

            //adding senses
            for (int i = 1; i <= 2; i++) {

                sensorImage = "Sense" + String.valueOf(i);
                blkImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/" + sensorImage + ".png"));
                parent.getChildren().add(new ActionBlock("sense", "err", fullImg, blkImg, btnnImg));
                // parent.getChildren().add(new ActionBlock("sense", fullImg, blkImg, btnnImg));
            }

        }

    }

}
