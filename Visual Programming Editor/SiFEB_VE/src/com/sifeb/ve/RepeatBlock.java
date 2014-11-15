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
        Image topImage = new Image(getClass().getResourceAsStream(RepeatBlock.BG_TOP_IMG));        
        Image middleImage1 = new Image(getClass().getResourceAsStream(RepeatBlock.BG_MID_IMG1));
        Image middleImage2 = new Image(getClass().getResourceAsStream(RepeatBlock.BG_MID_IMG2));
        Image bottomImage = new Image(getClass().getResourceAsStream(RepeatBlock.BG_BOTTOM_IMG));
        this.setBackImage(topImage, bottomImage);
        setActions(middleImage1);
        setCondition();
        super.getChildren().add(this.condition);
        
        super.getChildren().remove(super.exitBtn);
        super.getChildren().remove(super.addBtn);
        super.getChildren().add(super.exitBtn);
        super.getChildren().add(super.addBtn);
        
        //super.setMinHeight(224);
        
        addListeners();
        
        addRepeatContent(middleImage2);
    }

    public void addRepeatContent(Image img){
        this.holders = new VBox();
        BackgroundImage bckImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        holders.setMinWidth(122);
        holders.setMinHeight(101);
        //holders.setPadding(new Insets(1, 0, 0, 27));
        this.holders.setBackground(new Background(bckImg));
        this.holders.relocate(0, 95);        
        //this.addElementToVbox(new Holder(mainCtrl));
        //this.mainCtrl.addHolderAfterMe(this,false);
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
    
    public void setActions(Image img) {
        this.actions = new VBox();
        BackgroundImage bckImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        actions.setMinWidth(122);
        actions.setMinHeight(60);
        actions.setPadding(new Insets(1, 0, 0, 27));
        this.actions.setBackground(new Background(bckImg));
        this.actions.relocate(0, 19);
        super.getChildren().add(this.actions);
    }

    public VBox getCondition() {
        return condition;
    }

    public void setCondition() {
        this.condition = new VBox();
        this.condition.setPrefSize(90, 60);
        this.condition.relocate(168, 19);
        condition.setPadding(new Insets(1, 0, 0, 0));
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
                    if (p.getClass().getName().contains("ActionBlock")) {
                        Capability cap = draggedBlock.getCapability().cloneCapability();
                        draggedBlock = cap.getBlock();
                    } else {
                        ((Pane) p).getChildren().remove(draggedBlock);
                    }
                    String blockType = draggedBlock.getCapability().getType();
                    if (blockType.equals("actionC") || blockType.equals("action")
                            || blockType.equals("control")) {                        
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
