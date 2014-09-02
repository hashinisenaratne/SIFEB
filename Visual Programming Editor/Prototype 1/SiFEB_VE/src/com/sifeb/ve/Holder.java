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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Pubudu
 */
public class Holder extends Pane {

    private Image blockImg;
    private VBox actions;

    public Holder() {

        this.setBlockImg(new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/Holder.png")));
        System.out.println("here");
        this.actions = new VBox();
        this.actions.relocate(18, 15);

        super.getChildren().add(this.actions);

        setEventHandlers();
    }

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
                        draggedBlock.setLayoutX(event.getX());
                        draggedBlock.setLayoutY(event.getY());
                        success = true;
                    }
                }

                draggedBlock.setVisible(true);
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

    public void addElementToVbox(Node node) {
        actions.getChildren().add(node);

    }

    public VBox getActions() {
        return actions;
    }

    public void setActions(VBox actions) {
        this.actions = actions;
    }

    public void setBlockImg(Image blockImg) {
        this.blockImg = blockImg;
        super.setPrefSize(this.blockImg.getWidth(), this.blockImg.getHeight());
        super.setBackground(new Background(new BackgroundImage(this.blockImg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
    }
}
