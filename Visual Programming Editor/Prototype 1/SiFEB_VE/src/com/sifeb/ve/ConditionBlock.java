/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

/**
 *
 * @author Pubudu
 */
public class ConditionBlock extends Holder {

    private Pane pane;

    public ConditionBlock() {
        
        super();
        System.out.println("here here");
        super.setBlockImg(new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/Conditional.png")));
        super.getVbox().relocate(21, 15);
        this.pane = new Pane();
        this.pane.setPrefSize(90, 60);
        this.pane.relocate(158, 15);
        super.getChildren().add(this.pane);
    }

    public void addElementToPane(Node node) {
        pane.getChildren().add(node);
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
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
                    System.out.println(p.getClass().getName());
                    if (p.getClass().getName().contains("ActionBlock")) {
                        draggedBlock = new ActuatorBlock(draggedBlock.getType(), draggedBlock.getBlockImg(), draggedBlock.getBtnImg(), draggedBlock.isDragable());
                    } else {
                        ((Pane) p).getChildren().remove(draggedBlock);
                    }
                    if (draggedBlock.getType().equals("action")) {
                        this.addElementToVbox(draggedBlock);
                        success = true;
                    }
                    else if (draggedBlock.getType().equals("sense")) {
                        this.addElementToPane(draggedBlock);
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

}
