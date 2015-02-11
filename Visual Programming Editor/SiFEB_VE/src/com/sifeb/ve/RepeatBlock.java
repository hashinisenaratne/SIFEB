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
import javafx.geometry.Insets;
import javafx.geometry.Side;
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
 * This class is used for handling Repeat Blocks in the VPE
 *
 * @author Hashini
 */
public class RepeatBlock extends Holder {

    private static final String BG_TOP_IMG = "/com/sifeb/ve/images/Repeat_V_top.png";
    private static final String BG_BOTTOM_IMG = "/com/sifeb/ve/images/Repeat_V_bottom.png";
    private static final String BG_MID_IMG1 = "/com/sifeb/ve/images/Repeat_V_middle_1.png";
    private static final String BG_MID_IMG2 = "/com/sifeb/ve/images/Repeat_V_middle_2.png";

    private VBox condition;
    protected VBox holders;

    public RepeatBlock(MainEditorController mainCtrl) {

        super(mainCtrl);
        topImage = new Image(getClass().getResourceAsStream(RepeatBlock.BG_TOP_IMG));
        middleImage1 = new Image(getClass().getResourceAsStream(RepeatBlock.BG_MID_IMG1));
        middleImage2 = new Image(getClass().getResourceAsStream(RepeatBlock.BG_MID_IMG2));
        bottomImage1 = new Image(getClass().getResourceAsStream(RepeatBlock.BG_BOTTOM_IMG));
        setBackImage(topImage, bottomImage1);
        setActions();
        setCondition();
        super.getChildren().add(this.condition);

        exitBtn.toFront();
        addBtn.toFront();
        addListeners();

        addRepeatContent(middleImage1, middleImage2);
    }

    public final void setActions() {
        actions.setMinWidth(122);
        actions.setMinHeight(60);
        actions.setPadding(new Insets(0, 0, 0, 16));
        this.actions.setBackground(Background.EMPTY);
        this.actions.relocate(11, 19.5);
    }

    private void addRepeatContent(Image img1, Image img2) {
        this.holders = new VBox();
        this.holders.setSpacing(-15);
        BackgroundImage bckImg1 = new BackgroundImage(img1, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, new BackgroundPosition(Side.LEFT, 0, false, Side.TOP, 1, false), BackgroundSize.DEFAULT);
        BackgroundImage bckImg2 = new BackgroundImage(img2, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        holders.setMinWidth(122);
        holders.setMinHeight(101);
        holders.setPadding(new Insets(0, 0, 0, 10));
        this.holders.setBackground(new Background(bckImg2, bckImg1));
        this.holders.relocate(0, 94);
        ((Pane) this.holders).getChildren().add(new Holder(mainCtrl));
        super.getChildren().add(this.holders);
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

    public VBox getHolders() {
        return holders;
    }

    public final void setCondition() {
        this.condition = new VBox();
        this.condition.setPrefSize(90, 60);
        this.condition.relocate(168, 19);
        condition.setPadding(new Insets(1, 0, 0, 0));
    }

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
                    if (p.getClass().getName().contains("ActionBlock")) {
                        Capability cap = draggedBlock.getCapability().cloneCapability();
                        draggedBlock = cap.getBlock();
                    } else {
                        ((Pane) p).getChildren().remove(draggedBlock);
                    }
                    String blockType = draggedBlock.getCapability().getType();
                    switch (blockType) {
                        case Capability.CAP_CONTROL:
                            this.addElementToVbox(draggedBlock);
                            if (this.getActions().getChildren().size() > 1) {
                                this.getActions().getChildren().remove(0);
                            }
                            success = true;
                            this.mainCtrl.addHolderAfterMe(this, (VBox) this.getParent(), false);
                            break;
                        case Capability.CAP_ACTION:
                        case Capability.CAP_ACTION_C:
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
