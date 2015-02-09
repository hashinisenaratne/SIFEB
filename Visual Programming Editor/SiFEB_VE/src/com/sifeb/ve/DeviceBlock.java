/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import com.sifeb.ve.controller.ComPortController;
import com.sifeb.ve.handle.CodeGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
public final class DeviceBlock extends Pane {

    private final Button btn;
    private final Device device;
    private final Image bckImg;
    private Label name;

    private static final String ACTUATOR_BTN = "/com/sifeb/ve/images/MAPlay.png";
    private static final String ACTUATOR_BCK = "/com/sifeb/ve/images/MABackground.png";
    private static final String SENSOR_BTN = "/com/sifeb/ve/images/MSPlay.png";
    private static final String SENSOR_BCK = "/com/sifeb/ve/images/MSBackground.png";
    private static final String SHOW_COMMAND = "g";

    public DeviceBlock(Device device) {

        this.device = device;

        switch (this.device.getType()) {
            case Device.DEV_ACTUATOR:
                bckImg = new Image(ACTUATOR_BCK);
                break;
            case Device.DEV_SENSOR:
                bckImg = new Image(SENSOR_BCK);
                break;
            default:
                bckImg = new Image(ACTUATOR_BCK);
        }

        super.setPrefSize(bckImg.getWidth(), bckImg.getHeight());
        super.setMaxSize(bckImg.getWidth(), bckImg.getHeight());
        super.setBackground(new Background(
                new BackgroundImage(bckImg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT),
                new BackgroundImage(device.getImage(), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, new BackgroundPosition(Side.RIGHT, 10, false, Side.TOP, 5, false), BackgroundSize.DEFAULT)));
        this.setCursor(Cursor.HAND);

        this.btn = setButton(device.getType(), bckImg.getWidth(), bckImg.getHeight());
        super.getChildren().add(this.btn);

        name = new Label();
        setBlockText();
        setBlockLabel();
        setEventHandlers();
    }

    public void setEventHandlers() {
        this.setOnMouseClicked((MouseEvent event) -> {
            toggleHighlight();
        });
    }

    public void setBlockText() {
        name.setText(this.device.getDeviceName());
        name.setTooltip(new Tooltip(name.getText()));
    }

    private void setBlockLabel() {
        name.setFont(new Font(14));
        name.setPrefSize(bckImg.getWidth() * 0.6, 30);
        name.setMaxWidth(bckImg.getWidth() * 0.6);
        name.relocate((bckImg.getWidth() * 0.3), 30);
        name.setAlignment(Pos.CENTER_RIGHT);
        super.getChildren().add(name);
    }

    public Button setButton(String type, double width, double height) {
        Image btnImg = new Image(getClass().getResourceAsStream(DeviceBlock.ACTUATOR_BTN));
        switch (type) {
            case Device.DEV_ACTUATOR: {
                btnImg = new Image(getClass().getResourceAsStream(DeviceBlock.ACTUATOR_BTN));
                break;
            }
            case Device.DEV_SENSOR: {
                btnImg = new Image(getClass().getResourceAsStream(DeviceBlock.SENSOR_BTN));
                break;
            }
        }

        Button button = new Button();
        button.setMaxHeight(height);
        button.setMaxWidth(btnImg.getWidth());
        button.setMinHeight(height);
        button.setMinWidth(btnImg.getWidth());

        ImageView imgView = new ImageView(btnImg);
        button.setGraphic(imgView);
        button.getStyleClass().add("transparent-back");
        setButtonEvents(button, imgView);

        return button;
    }

    public void setButtonEvents(Button btn, ImageView imgView) {

        btn.setOnMouseEntered((MouseEvent mouseEvent) -> {
            imgView.setEffect(new DropShadow());
        });
        btn.setOnMouseExited((MouseEvent mouseEvent) -> {
            imgView.setEffect(null);
        });
        btn.setOnMousePressed((MouseEvent event) -> {
            imgView.setEffect(new InnerShadow());
        });
        btn.setOnMouseReleased((MouseEvent event) -> {
            imgView.setEffect(new DropShadow());
        });
        btn.setOnAction((ActionEvent event) -> {
            FeedBackLogger.sendGoodMessage("We are showing the " + this.device.getDeviceName());

            CodeGenerator codeGenerator = new CodeGenerator();
            byte[] sendingData = codeGenerator.generateTestDeviceCode(this.device);
            ComPortController.writeComPort("d");
            ComPortController.writeProgram(sendingData);

        });
    }

    public Button getBtn() {
        return btn;
    }

    public Device getDevice() {
        return device;
    }

    public void toggleHighlight() {
        ArrayList<Capability> caps = this.device.getCapabilities();
        if (this.getEffect() == null) {
            this.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLUEVIOLET, 20, 0.5, 0, 0));
            for (Capability cap : caps) {
                cap.getBlock().toggleHighlight(true);
            }
        } else {
            this.setEffect(null);
            for (Capability cap : caps) {
                cap.getBlock().toggleHighlight(false);
            }
        }

    }

    public void removeMe() {
        Pane node = (Pane) this.getParent();
        node.getChildren().remove(this);

    }
}
