/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import javafx.scene.Group;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private Label name;

    public Block(Capability capability) {

        this.capability = capability;
        this.textField = null;
        name = new Label();
        Block.this.setBlockText();
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

        name = new Label();
        name.setFont(new Font(14));
        name.setPrefSize(image.getWidth() * 0.8, 30);
        name.setMaxWidth(image.getWidth() * 0.8);
        name.relocate((image.getWidth() * 0.1), 30);        
        name.setAlignment(Pos.CENTER);
        super.getChildren().add(name);
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
    
    public void setBlockText(){
        name.setText(this.capability.getCapName());
    }
    
    public void setBlockText(String text){
        name.setText(text);
    }

    private void setShape(String type, Image img) {
        switch (type) {
            case "rectangle":
                break;
            case "action":
            case "actionC":
                name.setFont(new Font(12));
                name.setPrefSize(img.getWidth() * 0.9, 30);
                name.setMaxWidth(img.getWidth() * 0.9);
                name.relocate((img.getWidth() * 0.05), 30);
                break;
            case "sense":
                name.setFont(new Font(12));
                name.setPrefSize(img.getWidth() * 0.9, 30);
                name.setMaxWidth(img.getWidth() * 0.9);
                name.relocate((img.getWidth() * 0.05), 23);
                break;
            case "condition":
                name.setFont(new Font(13));
                name.setPrefSize(img.getWidth() * 0.9, 30);
                name.setMaxWidth(img.getWidth() * 0.9);
                name.relocate((img.getWidth() * 0.05), 24);

                textField = new TextField();
                textField.setPrefSize(30, 2);
                textField.setFont(new Font(8));
                textField.relocate(27, 40);
                textField.setDisable(true);
                super.getChildren().add(textField);

                break;
        }
        
        name.setAlignment(Pos.CENTER);
        super.getChildren().add(name);
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
