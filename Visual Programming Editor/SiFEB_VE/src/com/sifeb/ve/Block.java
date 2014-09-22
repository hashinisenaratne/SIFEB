/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author Pubudu
 */
public final class Block extends Pane {

    private final Capability capability;
    private TextField textField;

    public Block(Capability capability) {

        this.capability = capability;
        this.textField = null;
        setShape(capability.getType(), capability.getImage());

        super.setPrefSize(capability.getImage().getWidth(), capability.getImage().getHeight());
        super.setBackground(new Background(new BackgroundImage(capability.getImage(), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        setEventHandlers();
        this.setId(capability.getType() + Integer.toString(this.hashCode()));
    }

    /*
     * Constructor for undragable block
     */
    public Block(Image image) {

        this.capability = null;
        this.textField = null;

        super.setPrefSize(image.getWidth(), image.getHeight());
        super.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
    }

    public Capability getCapability() {
        return capability;
    }

    public TextField getTextField() {
        return textField;
    }

    public void setEventHandlers() {
        this.setOnMouseEntered((MouseEvent event) -> {
            this.setEffect(new Bloom(0.3));
        });
        this.setOnMouseExited((MouseEvent event) -> {
            this.setEffect(null);
        });
        this.setOnDragDetected((MouseEvent event) -> {
            if (!this.getParent().getClass().getName().contains("ActionBlock")) {
                this.setVisible(false);
            }

            Dragboard db = this.startDragAndDrop(TransferMode.COPY_OR_MOVE);
            db.setDragView(capability.getImage());
            ClipboardContent content = new ClipboardContent();
            content.putString(this.getId());
            db.setContent(content);
            event.consume();
        });

        this.setOnDragDone((DragEvent event) -> {
            this.setVisible(true);
        });

        ContextMenu contextMenu = new ContextMenu();

        MenuItem item1 = new MenuItem("Delete");
        item1.setOnAction((ActionEvent e) -> {
            ((Pane) this.getParent()).getChildren().remove(this);
            Capability cp = this.getCapability();
            cp.getDevice().getCapabilities().remove(cp);
        });

        contextMenu.getItems().add(item1);

        this.setOnMouseClicked((MouseEvent event) -> {

            if (event.getButton() == MouseButton.SECONDARY) {

                if (this.getParent().getClass().getName().contains("ActionBlock") == false) {

                    if (capability.getType().contains("action") || capability.getType().equals("sense") || capability.getType().equals("condition")) {
                        contextMenu.show(this, event.getSceneX(), event.getSceneY());
                    }

                }
            }
        });

    }

    public void setShape(String type, Image img) {
        switch (type) {
            case "rectangle":
                break;
            case "condition":
                textField = new TextField();
                textField.setPrefSize(30, 2);
                textField.setFont(new Font(8));
                textField.relocate(27, 40);
                textField.setDisable(true);
                super.getChildren().add(textField);

                break;
        }
//        Label name = new Label(this.capability.getCapName());
//        name.setAlignment(Pos.CENTER);
//        name.setFont(new Font(16));
//        name.setPrefSize(img.getWidth()*0.8, 30);
//        name.setMaxWidth(img.getWidth()*0.8);
//        name.relocate((img.getWidth()*0.1), 30);
//        super.getChildren().add(name);

    }

    public void disableTextField(boolean bool) {
        if (this.textField != null) {
            textField.setDisable(bool);
        }
    }

    public void toggleHighlight(boolean on) {
        Parent p = this.getParent();
        if (on) {
            if ((p != null) && p.getClass().getName().contains("ActionBlock")) {
                p.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLUEVIOLET, 20, 0.5, 0, 0));
            } else {
                this.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLUEVIOLET, 20, 0.5, 0, 0));
            }
        } else {
            if ((p != null) && p.getClass().getName().contains("ActionBlock")) {
                p.setEffect(null);
            } else {
                this.setEffect(null);
            }
        }
    }
}
