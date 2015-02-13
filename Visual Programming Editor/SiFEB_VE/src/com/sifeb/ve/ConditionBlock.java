/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import com.sifeb.ve.controller.MainEditorController;
import com.sifeb.ve.handle.SoundHandler;
import com.sun.javafx.beans.event.AbstractNotifyListener;
import javafx.beans.Observable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * This is the ConditionalBlock which handles an Action with a termination
 * condition
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
        topImage = new Image(getClass().getResourceAsStream(ConditionBlock.BG_TOP_IMG));
        middleImage1 = new Image(getClass().getResourceAsStream(ConditionBlock.BG_MID_IMG1));
        middleImage2 = new Image(getClass().getResourceAsStream(ConditionBlock.BG_MID_IMG2));
        bottomImage1 = new Image(getClass().getResourceAsStream(ConditionBlock.BG_BOTTOM_IMG));
        setBackImage(topImage, bottomImage1);
        setActions(middleImage1, middleImage2);
        setCondition();
        super.getChildren().add(this.condition);

        exitBtn.toFront();
        addBtn.toFront();
        addListeners();
    }

    // adds a conditon
    public void addCondition(Node node) {
        condition.getChildren().add(node);
    }

    // checks the condition
    public boolean hasCondition() {
        return (condition.getChildren().size() > 0);
    }

    // remove current condition
    public void removeCurrentCondition() {
        condition.getChildren().remove(0);
    }

    // returns the conditon in the block
    public VBox getCondition() {
        return condition;
    }

    // set the condition in block
    public final void setCondition() {
        this.condition = new VBox();
        this.condition.setPrefSize(90, 60);
        this.condition.relocate(157, 19);
    }

    // sets event handler for drag and drop
    @Override
    public void setEventHandlers() {
        this.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            Parent p = ((Node) event.getGestureSource()).getParent();
            boolean success = false;
            if (db.hasString()) {
                String nodeId = db.getString();
                Block draggedBlock = (Block) p.lookup("#" + nodeId);

                if (draggedBlock != null) {
                    this.mainCtrl.addHolderAfterMe(this, (VBox) this.getParent(), false);
                    if (p.getClass().getName().contains("ActionBlock")) {
                        Capability cap = draggedBlock.getCapability().cloneCapability();
                        draggedBlock = cap.getBlock();
                    } else {
                        ((Pane) p).getChildren().remove(draggedBlock);
                    }

                    String blockType = draggedBlock.getCapability().getType();
                    switch (blockType) {
                        case Capability.CAP_ACTION_C:
                            this.addElementToVbox(draggedBlock);
                            success = true;
                            this.mainCtrl.addHolderAfterMe(this, (VBox) this.getParent(), false);
                            break;
                        case Capability.CAP_ACTION:
                        case Capability.CAP_CONTROL:
                        case Capability.CAP_IFELSE:
                            super.mainCtrl.changeHolderType(this, (VBox) this.getParent(), draggedBlock);
                            success = true;
                            break;
                        case Capability.CAP_SENSE:
                        case Capability.CAP_CONDITION:
                            if (this.hasCondition()) {
                                this.removeCurrentCondition();
                            }
                            this.addCondition(draggedBlock);
                            if (blockType.equals(Capability.CAP_CONDITION)) {
                                draggedBlock.disableTextField(false);
                            }
                            success = true;
                            break;
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
            SoundHandler.playAudioClip("blockDrop.wav", 1);
        });

        this.setOnDragOver((DragEvent event) -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });
    }

    // add event listeners
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
            super.mainCtrl.changeHolderType(this, (VBox) this.getParent(), null);
        }
    }

}
