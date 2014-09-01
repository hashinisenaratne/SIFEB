/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import java.awt.Dialog;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.Blend;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialogs;
import static sun.security.krb5.KrbException.errorMessage;

/**
 *
 * @author Pubudu
 */
public final class Block extends Pane {

    private final Button btn;
    private final Image blockImg, btnImg;
    private final String type;

    public Block(String type, Image blockImg, Image btnImg) {

        this.btn = new Button();
        this.blockImg = blockImg;
        this.btnImg = btnImg;
        this.type = type;

        setButtonProperties(this.btn, this.blockImg, this.btnImg);
        setShape(this.type, this.blockImg);
        super.getChildren().add(this.btn);

        checkDevice(this.btn);

    }

    public void setShape(String type, Image img) {
        switch (type) {
            case "rectangle":
                Rectangle r = new Rectangle(img.getWidth(), img.getHeight(), new ImagePattern(img));
                r.setArcHeight(20);
                r.setArcWidth(20);
                super.getChildren().add(r);
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

        });

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
}
