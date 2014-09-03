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
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Pubudu
 */
public class ActionBlock extends Pane {

    private final Button btn;
    private final Image fullBlockImg, blockImg, btnImg;
    private final String type, message;
    private final ActuatorBlock actuatorBlock;
    // private final Rectangle rectangle;

    public ActionBlock(String type, String message, Image fullBlockImg, Image blockImg, Image btnImg) {

        this.btn = new Button();
        this.fullBlockImg = fullBlockImg;
        this.blockImg = blockImg;
        this.btnImg = btnImg;
        this.type = type;
        this.message = message;

        this.actuatorBlock = new ActuatorBlock(this.type, this.blockImg, null, true);
        this.actuatorBlock.setCursor(Cursor.CLOSED_HAND);
        moveBlock(this.actuatorBlock);

        Double heightValue = (this.fullBlockImg.getHeight() - this.blockImg.getHeight()) / 2;
        super.setPrefSize(this.fullBlockImg.getWidth(), this.fullBlockImg.getHeight());
        setButtonProperties(this.btn, this.fullBlockImg, this.btnImg);
        setShape(this.type, this.fullBlockImg);
        this.actuatorBlock.relocate(this.btnImg.getWidth() + 0.65, heightValue + 0.5);
        super.getChildren().addAll(this.btn, this.actuatorBlock);

        this.setId(Integer.toString(this.hashCode()));

    }

    public void setShape(String type, Image fullBImg) {
        switch (type) {
            case "action":
            case "actionC":
            case "condition":
            case "sense":
                Rectangle r = new Rectangle(fullBImg.getWidth(), fullBImg.getHeight(), new ImagePattern(fullBImg));
                r.setArcHeight(20);
                r.setArcWidth(20);
                super.getChildren().add(r);
                break;
        }

    }

    public void setButtonProperties(Button button, Image img, Image btnImg) {

        button.relocate(0, 0);
        button.setMaxHeight(img.getHeight());
        button.setMaxWidth(btnImg.getWidth());
        button.setMinHeight(img.getHeight());
        button.setMinWidth(btnImg.getWidth());
        button.setCursor(Cursor.HAND);

        ImageView imgView = new ImageView(btnImg);
        button.setGraphic(imgView);
        button.setStyle("-fx-background-color: transparent");

        if (message.equals("err")) {
            button.setDisable(true);
        }
        changeBackgroundOnHover(button);

        checkButton(button);

    }

    public void changeBackgroundOnHover(final Node node) {

        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                node.setEffect(new DropShadow());
            }
        });
        node.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                node.setEffect(null);
            }
        });
    }

    public void moveBlock(final Node node) {

        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                node.setEffect(new Bloom(0.3));
            }
        });
        node.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                node.setEffect(null);
            }
        });
    }

    public Button getBtn() {
        return btn;
    }

    public Image getFullBlockImg() {
        return fullBlockImg;
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

    public ActuatorBlock getActuatorBlock() {
        return actuatorBlock;
    }

    private void checkButton(Button button) {

        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                Dialog dlg = new Dialog(null, "Message from SiFEB");
                dlg.setResizable(false);
                dlg.setIconifiable(false);
                dlg.setGraphic(new ImageView(blockImg));
                dlg.setMasthead("Would You Like to Check Me?");
                //dlg.setContent(content);
                dlg.getActions().addAll(Dialog.Actions.YES, Dialog.Actions.NO);

                Action response = dlg.show();
                System.out.println("response" + response);

                if (response == Dialog.Actions.YES) {

                    try {
                        ComPortController.writeComPort("COM17", 10, message);
                    } catch (PortInUseException | IOException | UnsupportedCommOperationException ex) {
                        Logger.getLogger(ActuatorBlock.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    // ... user cancelled, reset form to default
                }

            }
        });
    }

}
