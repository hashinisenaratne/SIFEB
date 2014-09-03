/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import com.sifeb.ve.controller.MainEditorController;
import com.sun.javafx.beans.event.AbstractNotifyListener;
import javafx.beans.Observable;
import javafx.collections.ObservableArray;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Pubudu
 */
public class ConditionBlock extends Holder {

    private VBox condition;

    public ConditionBlock(MainEditorController mainCtrl) {

        super(mainCtrl);
        super.setBlockImg(new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/Conditional.png")));
        super.getActions().relocate(20, 15);
//        this.pane = new Pane();
        this.condition = new VBox();
        this.condition.setPrefSize(90, 60);
        this.condition.relocate(161.25, 15.5);
        super.getChildren().add(this.condition);

        addListeners();
    }

    public void addCondition(Node node) {
        condition.getChildren().add(node);
    }

    public boolean hasCondition() {
        return (condition.getChildren().size() > 0);
    }

    public void removeCurrentCondition() {
        condition.getChildren().remove(0);
    }

    public VBox getCondition() {
        return condition;
    }

    public void setCondition(VBox condition) {
        this.condition = condition;
    }

    @Override
    public void setEventHandlers() {
        this.setOnDragDropped((DragEvent event) -> {
            System.out.println("dropped");
            Dragboard db = event.getDragboard();
            Parent p = ((Node) event.getGestureSource()).getParent();
            boolean success = false;
            if (db.hasString()) {
                String nodeId = db.getString();
                ActuatorBlock draggedBlock = (ActuatorBlock) p.lookup("#" + nodeId);

                if (draggedBlock != null) {
                    this.mainCtrl.addHolderAfterMe(this);
                    if (p.getClass().getName().contains("ActionBlock")) {
                        draggedBlock = new ActuatorBlock(draggedBlock.getType(), draggedBlock.getBlockImg(), draggedBlock.getBtnImg(), draggedBlock.isDragable());
                    } else {
                        ((Pane) p).getChildren().remove(draggedBlock);
                    }
                    if (draggedBlock.getType().equals("actionC")) {
                        this.addElementToVbox(draggedBlock);
                        success = true;
                        this.mainCtrl.addHolderAfterMe(this);
                    } else if (draggedBlock.getType().equals("action")) {
                        super.mainCtrl.changeHolderType(this, draggedBlock);
                        success = true;
                    } else if ((draggedBlock.getType().equals("sense")) || (draggedBlock.getType().equals("condition"))) {
                        if (this.hasCondition()) {
                            this.removeCurrentCondition();
                        }
                        this.addCondition(draggedBlock);
                        if (draggedBlock.getType().equals("condition")) {
                            draggedBlock.disableTextField(false);
                        }
                        success = true;
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        this.setOnDragOver((DragEvent event) -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });
    }

    private void addListeners() {
        super.getActions().getChildren().addListener(new AbstractNotifyListener() {

            @Override
            public void invalidated(Observable observable) {
                changeBackToHolder();
            }
        });

    }

    private void changeBackToHolder() {
        int numActions = super.getActions().getChildren().size();
        int numConditions = this.getCondition().getChildren().size();
        if ((numConditions == 0) && (numActions == 0)) {
            super.mainCtrl.changeHolderType(this, null);
        }
    }

}
