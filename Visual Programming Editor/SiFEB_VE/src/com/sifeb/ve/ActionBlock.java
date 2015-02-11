/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import com.sifeb.ve.controller.ComPortController;
import com.sifeb.ve.handle.CodeGenerator;
import com.sifeb.ve.resources.Strings;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

/**
 * ActionBlock is an integration of a capability. This class allows to handle
 * capability features such as testing capability.
 *
 * @author Pubudu
 */
public class ActionBlock extends HBox {

    private final Image img;
    private final Button btn;
    private final Block block;

    private static final String BACKGROUND_IMG = "/com/sifeb/ve/images/ActionTest.png";
    private static final String TEST_BTN = "/com/sifeb/ve/images/ActionTestBtn.png";

    public ActionBlock(Block block, boolean hasTestBtn) {

        this.block = block;
        this.img = new Image(getClass().getResourceAsStream(ActionBlock.BACKGROUND_IMG));
        super.setSpacing(1);
        super.setFillHeight(false);
        super.setAlignment(Pos.CENTER_LEFT);
        super.setBackground(new Background(new BackgroundImage(this.img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        this.btn = setTestButton(this.img.getWidth(), this.img.getHeight());
        super.getChildren().add(this.btn);
        super.getChildren().add(this.block);

        if (!hasTestBtn) {
            this.btn.setDisable(true);
        }

    }

//set Test button configuration in Action Block
    public Button setTestButton(double width, double height) {
        Image btnImg = new Image(getClass().getResourceAsStream(ActionBlock.TEST_BTN));

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

    //Set events for buttons and testing the capability 
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
            Capability cp = this.block.getCapability();
            Dialog dlg = new Dialog(null, Strings.getString("message.fromsifeb"));
            dlg.setResizable(false);
            dlg.setIconifiable(false);
            dlg.setGraphic(new ImageView(cp.getStaticImage()));
            dlg.setMasthead(Strings.getString("message.liketocheck") + " \'" + cp.getCapName() + "\'?");
            Dialog.Actions.YES.textProperty().set(Strings.getString("btn.yes"));
            Dialog.Actions.NO.textProperty().set(Strings.getString("btn.no"));
            dlg.getActions().addAll(Dialog.Actions.YES, Dialog.Actions.NO);

            Action response = dlg.show();

            if (response == Dialog.Actions.YES) {
                FeedBackLogger.sendGoodMessage(Strings.getString("message.testing") + " \'" + cp.getCapName() + "\' " + Strings.getString("message.capability") + "...");

                CodeGenerator codeGenerator = new CodeGenerator();
                byte[] sendingData = codeGenerator.generateTestBlockCode(cp.getBlock());
                ComPortController.writeComPort("d");
                ComPortController.writeProgram(sendingData);

            } else {
                FeedBackLogger.sendBadMessage(Strings.getString("message.testlater") + "...");
            }
        });
    }

    public void addToPane(Pane parent) {
        parent.getChildren().add(this);
    }

    public void removeMe() {
        Pane node = (Pane) this.getParent();
        node.getChildren().remove(this);

    }
}
