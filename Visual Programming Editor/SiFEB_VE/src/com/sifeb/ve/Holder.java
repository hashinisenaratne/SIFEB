/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import com.sifeb.ve.controller.MainEditorController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author Pubudu
 */
public class Holder extends Pane {

    private static final String CROSS_IMG = "/com/sifeb/ve/images/cross.png";
    private static final String ADD_IMG = "/com/sifeb/ve/images/add.png";
    private static final String BG_TOP_IMG = "/com/sifeb/ve/images/Holder_V_top.png";
    private static final String BG_BOTTOM_IMG = "/com/sifeb/ve/images/Holder_V_bottom.png";
    private static final String BG_MID_IMG = "/com/sifeb/ve/images/Holder_V_middle.png";

    protected VBox actions;
    MainEditorController mainCtrl;
    protected final Button exitBtn;
    protected final Button addBtn;
    Image topImage;
    Image middleImage1;
    Image middleImage2;
    Image bottomImage1;

    public Holder(MainEditorController mainCtrl) {

        this.mainCtrl = mainCtrl;
        topImage = new Image(getClass().getResourceAsStream(Holder.BG_TOP_IMG));
        middleImage1 = new Image(getClass().getResourceAsStream(Holder.BG_MID_IMG));
        middleImage2 = new Image(getClass().getResourceAsStream(Holder.BG_MID_IMG));
        bottomImage1 = new Image(getClass().getResourceAsStream(Holder.BG_BOTTOM_IMG));

        this.setBackImage(topImage, bottomImage1);
        this.setActions(middleImage1, middleImage2);

        this.exitBtn = new Button();
        this.addBtn = new Button();
        setExitButtonProperties(this.exitBtn);
        setAddButtonProperties(this.addBtn);

        super.getChildren().add(this.exitBtn);
        super.getChildren().add(this.addBtn);
        setEventHandlers();
    }

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
                    if (draggedBlock.getCapability().getType().equals(Capability.CAP_ACTION)) {
                        this.addElementToVbox(draggedBlock);
                        success = true;
                    } else  {
                        this.mainCtrl.changeHolderType(this, (VBox) this.getParent(), draggedBlock);
                        success = true;
                    }
                    /*
                    if (draggedBlock.getCapability().getType().equals("actionC")
                            || draggedBlock.getCapability().getType().equals("control")
                            || draggedBlock.getCapability().getType().equals("ifelse"))
                    */
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        this.setOnDragOver((DragEvent event) -> {
            if (event.getDragboard().hasString()) {
                String dbStr = event.getDragboard().getString();
                if (dbStr.contains(Capability.CAP_ACTION) || dbStr.contains(Capability.CAP_CONTROL) || dbStr.contains(Capability.CAP_IFELSE)) {
                    event.acceptTransferModes(TransferMode.COPY);
                } else {
                    System.out.println("not allowed");
                }
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

    public final void setActions(Image img1, Image img2) {
        this.actions = new VBox();
        BackgroundImage bckImg1 = new BackgroundImage(img1, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        BackgroundImage bckImg2 = new BackgroundImage(img2, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        actions.setMinWidth(122);
        actions.setMinHeight(60);
        actions.setPadding(new Insets(0, 0, 0, 16));
        this.actions.setBackground(new Background(bckImg2, bckImg1));
        this.actions.relocate(0, 19);
        super.getChildren().add(this.actions);
    }

    public final void setBackImage(Image topImage, Image bottomImage) {
        super.setMinWidth(topImage.getWidth());
        super.setPadding(new Insets(20, 0, 8, 0));

        BackgroundImage top = new BackgroundImage(topImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        BackgroundImage bottom = new BackgroundImage(bottomImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true), BackgroundSize.DEFAULT);
        super.setBackground(new Background(bottom, top));
    }
    
    public final void setEmptyBackImage(){
        super.setBackground(Background.EMPTY);
    }

    private void setExitButtonProperties(Button button) {
        Image crossImg = new Image(getClass().getResourceAsStream(Holder.CROSS_IMG));
        button.relocate(0, 0);
        button.setGraphic(new ImageView(crossImg));
        button.setMaxHeight(crossImg.getHeight());
        button.setMaxWidth(crossImg.getWidth());
        button.setMinHeight(crossImg.getHeight());
        button.setMinWidth(crossImg.getWidth());
        button.setCursor(Cursor.HAND);
        button.setStyle("-fx-background-color: transparent");

        button.setOnAction((ActionEvent event) -> {
            this.mainCtrl.deleteHolder(this,(VBox) this.getParent());
        });
        setBtnHoverEffects(this.exitBtn);
    }

    private void setAddButtonProperties(Button button) {

        Image addImg = new Image(getClass().getResourceAsStream(Holder.ADD_IMG));
        button.relocate(0, 20);
        button.setGraphic(new ImageView(addImg));
        button.setMaxHeight(addImg.getHeight());
        button.setMaxWidth(addImg.getWidth());
        button.setMinHeight(addImg.getHeight());
        button.setMinWidth(addImg.getWidth());
        button.setCursor(Cursor.HAND);
        button.setStyle("-fx-background-color: transparent");

        button.setOnAction((ActionEvent event) -> {
            this.mainCtrl.addHolderAfterMe(this,(VBox) this.getParent(), true);
        });
        setBtnHoverEffects(this.addBtn);
    }

    public void setBtnHoverEffects(final Button btn) {

        btn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                btn.setEffect(new DropShadow());
            }
        });
        btn.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                btn.setEffect(null);
            }
        });
    }

    public void toggleHighlight(boolean on) {
        if (on) {
            this.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.CRIMSON, 20, 0.5, 0, 0));

        } else {
            this.setEffect(null);
        }
    }

}
