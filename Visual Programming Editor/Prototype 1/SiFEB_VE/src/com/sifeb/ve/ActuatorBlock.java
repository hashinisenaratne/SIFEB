/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import com.sifeb.ve.controller.ComPortController;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
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
import javafx.scene.text.Font;

/**
 *
 * @author Pubudu
 */
public final class ActuatorBlock extends Pane {

    private Button btn;
    private final Image blockImg;
    private Image btnImg;
    private final String type;
    private final boolean dragable;
    private TextField textField;

    public ActuatorBlock(String type, Image blockImg, Image btnImg, boolean dragable) {

        // this.btn = new Button();
        this.blockImg = blockImg;
        this.btnImg = btnImg;
        this.type = type;
        this.dragable = dragable;
        this.textField = null;
        setShape(this.type, this.blockImg);

        super.setPrefSize(this.blockImg.getWidth(), this.blockImg.getHeight());
        super.setBackground(new Background(new BackgroundImage(this.blockImg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        if (btnImg != null) {
            this.btn = new Button();
            super.getChildren().add(this.btn);
            setButtonProperties(this.btn, this.blockImg, this.btnImg);
            checkDevice(this.btn);
        }

        setEventHandlers();
        this.setId(this.type + Integer.toString(this.hashCode()));
    }

    public void setEventHandlers() {

        this.setOnDragDetected((MouseEvent event) -> {
            if (this.isDragable()) {
                if (!this.getParent().getClass().getName().contains("ActionBlock")) {
                    this.setVisible(false);
                }
//                System.out.println("dragged");
                Dragboard db = this.startDragAndDrop(TransferMode.COPY_OR_MOVE);
                db.setDragView(this.getBlockImg());
                ClipboardContent content = new ClipboardContent();
                content.putString(this.getId());
                db.setContent(content);
                event.consume();
            }
        });

        this.setOnDragDone((DragEvent event) -> {
            this.setVisible(true);
        });

        ContextMenu contextMenu = new ContextMenu();

        MenuItem item1 = new MenuItem("Delete");
        item1.setOnAction((ActionEvent e) -> {

            ((Pane) this.getParent()).getChildren().remove(this);
        });

        contextMenu.getItems().add(item1);

        this.setOnMouseClicked((MouseEvent event) -> {

            if (event.getButton() == MouseButton.SECONDARY) {

                if (this.getParent().getClass().getName().contains("ActionBlock") == false) {

                    if (type.contains("action") || type.equals("sense") || type.equals("condition")) {
                        contextMenu.show(this, event.getSceneX(), event.getSceneY());
                    }

                }
            }
        });

    }

    public void setShape(String type, Image img) {
        switch (type) {
            case "rectangle":
//                Rectangle r = new Rectangle(img.getWidth(), img.getHeight(), new ImagePattern(img));
//                r.setArcHeight(20);
//                r.setArcWidth(20);
                //  super.getChildren().add(r);
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

    }

    public void setButtonProperties(Button button, Image img, Image btnImg) {

        button.setMaxHeight(img.getHeight());
        button.setMaxWidth(btnImg.getWidth());
        button.setMinHeight(img.getHeight());
        button.setMinWidth(btnImg.getWidth());
        button.setCursor(Cursor.HAND);

        ImageView imgView = new ImageView(btnImg);
        button.setGraphic(imgView);
        button.setStyle("-fx-background-color: transparent");
        changeBackgroundOnHoverUsingEvents(button, imgView);
    }

    public void changeBackgroundOnHoverUsingEvents(final Node node, ImageView imgView) {

        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                imgView.setEffect(new DropShadow());
            }
        });
        node.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                imgView.setEffect(null);
            }
        });
    }

    public void checkDevice(Button btn) {
        btn.setOnAction((ActionEvent event) -> {

            try {
                ComPortController.writeComPort("COM15", 10, "g");
            } catch (PortInUseException | IOException | UnsupportedCommOperationException ex) {
                Logger.getLogger(ActuatorBlock.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    public void disableTextField(boolean bool) {
        textField.setDisable(bool);
    }

    public Button getBtn() {
        return btn;
    }

    public Image getBlockImg() {
        return blockImg;
    }

    public Image getBtnImg() {
        return btnImg;
    }

    public String getType() {
        return type;
    }

    public boolean isDragable() {
        return dragable;
    }
}
