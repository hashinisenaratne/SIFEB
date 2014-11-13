/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import com.sifeb.ve.controller.MainEditorController;
import com.sun.javafx.beans.event.AbstractNotifyListener;
import javafx.beans.Observable;
import javafx.geometry.Insets;
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
public class ConditionBlock extends Holder {

    private static final String BG_TOP_IMG = "/com/sifeb/ve/images/Conditional_V_top.png";
    private static final String BG_BOTTOM_IMG = "/com/sifeb/ve/images/Conditional_V_bottom.png";
    private static final String BG_MID_IMG1 = "/com/sifeb/ve/images/Conditional_V_middle1.png";
     private static final String BG_MID_IMG2 = "/com/sifeb/ve/images/Conditional_V_middle2.png";
    
    private VBox condition;

    public ConditionBlock(MainEditorController mainCtrl) {

        super(mainCtrl);
        Image topImage = new Image(getClass().getResourceAsStream(ConditionBlock.BG_TOP_IMG));        
        Image middleImage1 = new Image(getClass().getResourceAsStream(ConditionBlock.BG_MID_IMG1));
        Image middleImage2 = new Image(getClass().getResourceAsStream(ConditionBlock.BG_MID_IMG2));
        Image bottomImage = new Image(getClass().getResourceAsStream(ConditionBlock.BG_BOTTOM_IMG));
        this.setBackImage(topImage, bottomImage);
        setActions(middleImage1,middleImage2);
        setCondition();
        super.getChildren().add(this.condition);
        
        super.getChildren().remove(super.exitBtn);
        super.getChildren().remove(super.addBtn);
        super.getChildren().add(super.exitBtn);
        super.getChildren().add(super.addBtn);
        
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
    
    public void setActions(Image img1, Image img2) {
        this.actions = new VBox();
        BackgroundImage bckImg1 = new BackgroundImage(img1, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        BackgroundImage bckImg2 = new BackgroundImage(img2, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        actions.setMinWidth(122);
        actions.setMinHeight(60);
        actions.setPadding(new Insets(0, 0, 0, 16));
        this.actions.setBackground(new Background(bckImg2,bckImg1));
        this.actions.relocate(0, 19);
        super.getChildren().add(this.actions);
    }

    public VBox getCondition() {
        return condition;
    }

    public void setCondition() {
        this.condition = new VBox();
        this.condition.setPrefSize(90, 60);
        this.condition.relocate(157, 19);
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
                Block draggedBlock = (Block) p.lookup("#" + nodeId);

                if (draggedBlock != null) {
                    this.mainCtrl.addHolderAfterMe(this,false);
                    if (p.getClass().getName().contains("ActionBlock")) {
                        Capability cap = draggedBlock.getCapability().cloneCapability();
                        draggedBlock = cap.getBlock();
                    } else {
                        ((Pane) p).getChildren().remove(draggedBlock);
                    }
                    String blockType = draggedBlock.getCapability().getType();
                    if (blockType.equals("actionC")) {
                        this.addElementToVbox(draggedBlock);
                        success = true;
                        this.mainCtrl.addHolderAfterMe(this,false);
                    } else if (blockType.equals("action")) {
                        super.mainCtrl.changeHolderType(this, draggedBlock);
                        success = true;
                    } else if (blockType.equals("sense") || blockType.equals("condition")) {
                        if (this.hasCondition()) {
                            this.removeCurrentCondition();
                        }
                        this.addCondition(draggedBlock);
                        if (blockType.equals("condition")) {
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
        
        this.getCondition().getChildren().addListener(new AbstractNotifyListener() {

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
