/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import com.sifeb.ve.controller.ComPortController;
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
import javafx.stage.Modality;
import javafx.stage.Window;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

/**
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

//    public void setShape(String type, Image fullBImg) {
//        switch (type) {
//            case "action":
//            case "actionC":
//            case "condition":
//            case "sense":
//                Rectangle r = new Rectangle(fullBImg.getWidth(), fullBImg.getHeight(), new ImagePattern(fullBImg));
//                r.setArcHeight(20);
//                r.setArcWidth(20);
//                super.getChildren().add(r);
//                break;
//        }
//
//    }
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
//            Window owner = this.getScene().getWindow();
//            Alert dlg = new Alert(AlertType.CONFIRMATION, "");
//            dlg.initModality(Modality.APPLICATION_MODAL);
//            dlg.initOwner(owner); 
//            
//            dlg.setTitle("You do want dialogs right?");
//            dlg.setResizable(false);
////            dlg.setIconifiable(false);
//            dlg.setGraphic(new ImageView(cp.getStaticImage()));
//            String optionalMasthead = "Just Checkin'";
//            dlg.getDialogPane().setContentText("I was a bit worried that you might not want them, so I wanted to double check.");
//            dlg.show();
//            dlg.resultProperty().addListener(o -> System.out.println("Result is: " + dlg.getResult()));
            
            Dialog dlg = new Dialog(null, Strings.getString("message.fromsifeb"));
            dlg.setResizable(false);
            dlg.setIconifiable(false);
            dlg.setGraphic(new ImageView(cp.getStaticImage()));
            dlg.setMasthead(Strings.getString("message.liketocheck")+" \'"+cp.getCapName()+"\'?");
            Dialog.Actions.YES.textProperty().set(Strings.getString("btn.yes"));
            Dialog.Actions.NO.textProperty().set(Strings.getString("btn.no"));
            dlg.getActions().addAll(Dialog.Actions.YES, Dialog.Actions.NO);

            Action response = dlg.show();
            System.out.println("response" + response);

            if (response == Dialog.Actions.YES) {
                FeedBackLogger.sendGoodMessage(Strings.getString("message.testing") + " \'" + cp.getCapName() + "\' " + Strings.getString("message.capability") + "...");
                ComPortController.writeComPort(ComPortController.port, cp.getDevice().getAddress(), cp.getCommand());
            } else {
                FeedBackLogger.sendBadMessage(Strings.getString("message.testlater") + "...");
                // ... user cancelled, reset form to default
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
