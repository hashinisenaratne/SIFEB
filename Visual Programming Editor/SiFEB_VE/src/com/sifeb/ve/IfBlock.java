/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import com.sifeb.ve.controller.MainEditorController;
import com.sun.javafx.beans.event.AbstractNotifyListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Hashini
 */
public class IfBlock extends Holder {

    private static final String BG_IF_TOP_LEFT_IMG = "/com/sifeb/ve/images/If_V_top_left.png";
    private static final String BG_IF_TOP_MID_IMG = "/com/sifeb/ve/images/If_V_top_middle.png";
    private static final String BG_IF_TOP_RIGHT_IMG = "/com/sifeb/ve/images/If_V_top_right.png";
    private static final String BG_BOTTOM1_IMG = "/com/sifeb/ve/images/Repeat_V_bottom.png";
    private static final String BG_BOTTOM2_IMG = "/com/sifeb/ve/images/If_V_bottom.png";
    private static final String BG_MID_IMG = "/com/sifeb/ve/images/Repeat_V_middle_2.png";

    private VBox condition;
    protected VBox ifHolders;
    protected VBox elseHolders;

    public IfBlock(MainEditorController mainCtrl) {

        super(mainCtrl);
        Image ifTopLeftImage = new Image(getClass().getResourceAsStream(IfBlock.BG_IF_TOP_LEFT_IMG));
        Image ifTopMidImage = new Image(getClass().getResourceAsStream(IfBlock.BG_IF_TOP_MID_IMG));
        Image ifTopRightImage = new Image(getClass().getResourceAsStream(IfBlock.BG_IF_TOP_RIGHT_IMG));
        Image middleImage = new Image(getClass().getResourceAsStream(IfBlock.BG_MID_IMG));
        bottomImage1 = new Image(getClass().getResourceAsStream(IfBlock.BG_BOTTOM1_IMG));
        Image bottomImage2 = new Image(getClass().getResourceAsStream(IfBlock.BG_BOTTOM2_IMG));
        setEmptyBackImage();
        setActions();
        setCondition();
        super.getChildren().add(this.condition);

        addIfContent(ifTopLeftImage, ifTopMidImage, ifTopRightImage, middleImage, bottomImage1, bottomImage2);

        exitBtn.toFront();
        addBtn.toFront();
        actions.toFront();
        condition.toFront();
        addListeners();

    }

    public final void setActions() {
        actions.setMinWidth(122);
        actions.setMinHeight(60);
        actions.setPadding(new Insets(2, 0, 0, 16));
        this.actions.setBackground(Background.EMPTY);
        this.actions.relocate(11, 18);
    }

    private void addIfContent(Image topLeft, Image topMiddle, Image topRight, Image mid, Image bottom1, Image bottom2) {
        HBox container = new HBox();

        this.ifHolders = new VBox();
        this.ifHolders.setSpacing(-15);
        this.elseHolders = new VBox();
        this.elseHolders.setSpacing(-15);

        super.setPadding(new Insets(0, 0, 0, 0));

        BackgroundImage bckTopLeft = new BackgroundImage(topLeft, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, new BackgroundPosition(Side.LEFT, 0, false, Side.TOP, 0, false), BackgroundSize.DEFAULT);
        BackgroundImage bckTopMid = new BackgroundImage(topMiddle, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, new BackgroundPosition(Side.LEFT, 260, false, Side.TOP, 0, false), BackgroundSize.DEFAULT);
        BackgroundImage bckTopRight = new BackgroundImage(topRight, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, new BackgroundPosition(Side.LEFT, 0, false, Side.TOP, 0, false), BackgroundSize.DEFAULT);
        BackgroundImage bckBottom1 = new BackgroundImage(bottom1, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, new BackgroundPosition(Side.LEFT, 0, false, Side.BOTTOM, 0, false), BackgroundSize.DEFAULT);
        BackgroundImage bckBottom2 = new BackgroundImage(bottom2, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, new BackgroundPosition(Side.LEFT, 0, false, Side.BOTTOM, 0, false), BackgroundSize.DEFAULT);
        BackgroundImage bckMid = new BackgroundImage(mid, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, new BackgroundPosition(Side.LEFT, 0, false, Side.TOP, 10, false), BackgroundSize.DEFAULT);

        ifHolders.setMinWidth(276);
        ifHolders.setMinHeight(101);
        ifHolders.setPadding(new Insets(94, 0, 30, 10));
        this.ifHolders.setBackground(new Background(bckTopMid,bckMid, bckTopLeft, bckBottom1));
        ((Pane) this.ifHolders).getChildren().add(new Holder(mainCtrl));
        container.getChildren().add(ifHolders);

        elseHolders.setMinWidth(276);
        elseHolders.setMinHeight(87);
        elseHolders.setPadding(new Insets(94, 0, 16, 10));
        HBox.setMargin(elseHolders, new Insets(0, 0, 14, 0));
        this.elseHolders.setBackground(new Background(bckMid, bckTopRight, bckBottom2));
        ((Pane) this.elseHolders).getChildren().add(new Holder(mainCtrl));
        container.getChildren().add(elseHolders);
        super.getChildren().add(container);
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
    
    public VBox getIfHolders() {
        return ifHolders;
    }

    public VBox getElseHolders() {
        return elseHolders;
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
                    if (blockType.equals("ifelse")) {
                        this.addElementToVbox(draggedBlock);
                        if (this.getActions().getChildren().size() > 1) {
                            this.getActions().getChildren().remove(0);
                        }
                        success = true;
                        this.mainCtrl.addHolderAfterMe(this, (VBox) this.getParent(), false);
                    } else if (blockType.equals("action") || blockType.equals("actionC")|| blockType.equals("control")) {
                        super.mainCtrl.changeHolderType(this, (VBox) this.getParent(), draggedBlock);
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

//        elseHolders.heightProperty().addListener(new ChangeListener<Number>() {
//
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                ifHolders.setMinHeight((double) newValue);
//                ifHolders.autosize();
//            }
//
//        });
//        
//        ifHolders.heightProperty().addListener(new ChangeListener<Number>() {
//
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                elseHolders.setMinHeight((double) newValue);
//                elseHolders.autosize();
//            }
//
//        });
    }

    private void changeBackToHolder() {
        int numActions = super.getActions().getChildren().size();
        int numConditions = this.getCondition().getChildren().size();
        if ((numConditions == 0) && (numActions == 0)) {
            super.mainCtrl.changeHolderType(this, (VBox) this.getParent(), null);
        }
    }

}
