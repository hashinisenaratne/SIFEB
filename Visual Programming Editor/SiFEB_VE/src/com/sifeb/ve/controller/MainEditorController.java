/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.controller;

import com.sifeb.ve.ActionBlock;
import com.sifeb.ve.Block;
import com.sifeb.ve.Capability;
import com.sifeb.ve.ConditionBlock;
import com.sifeb.ve.RepeatBlock;
import com.sifeb.ve.Device;
import com.sifeb.ve.FeedBackLogger;
import com.sifeb.ve.Holder;
import com.sifeb.ve.resources.Strings;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

/**
 *
 * @author Udith Arosha
 */
public class MainEditorController implements Initializable {

    @FXML
    Button addDeviceBtn;
    @FXML
    Button addCapBtn;
    @FXML
    Button addHolderBtn;
    @FXML
    Button runBtn;
    @FXML
    VBox devicesBox;
    @FXML
    VBox capabilityBox;
    @FXML
    Pane editorPane;
    @FXML
    VBox editorBox;
    @FXML
    ImageView fbFace;
    @FXML
    ImageView fbLeft;
    @FXML
    ImageView fbRight;
    @FXML
    Label fbText;
    @FXML
    Label haveLabel;
    @FXML
    Label doLabel;

    ArrayList<Holder> holders;
    Holder lastHolder;
    Image playImg;
    //temp
    ArrayList<Device> devices;
    ArrayList<Capability> capabilities;

//    VBox messageBox;
    public int hValue;
    public boolean ackReceived;

    //
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //temp        
        devices = new ArrayList<>();
        capabilities = new ArrayList<>();

        //  messageBox = new VBox();
        holders = new ArrayList<>();
        editorBox.setFillWidth(false);
        editorBox.setSpacing(-15);
        editorBox.setAlignment(Pos.TOP_LEFT);
        addStartEndBlocks();
        addBlockHolder(0, false, false);
        //addBlock(devicesBox);
        //  addBlock(capabilityBox);

        setEventHandlers();
        FeedBackLogger.setControls(this.fbFace, this.fbText);
        setFeedbackPanel();

        setTextStrings();
    }

    private void setFeedbackPanel() {
        Image img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/bubbleLeft.png"));
        fbLeft.setImage(img);
        img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/bubbleRight.png"));
        fbRight.setImage(img);
        img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/bubbleMid.png"));
        fbText.setBackground(new Background(new BackgroundImage(img, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        playImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/play.png"));
        FeedBackLogger.sendWelcomeMessage();
    }

    private void addStartEndBlocks() {
        Image img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/Start_V.png"));
        editorBox.getChildren().add(new Block(img));

        img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/Stop_V.png"));
        editorBox.getChildren().add(new Block(img));
    }

    public void setTextStrings() {
        runBtn.setText(Strings.getString("btn.run"));
        haveLabel.setText(Strings.getString("label.have"));
        doLabel.setText(Strings.getString("label.do"));

        ((Block) editorBox.getChildren().get(0)).setBlockText(Strings.getString("block.start"));
        ((Block) editorBox.getChildren().get(editorBox.getChildren().size() - 1)).setBlockText(Strings.getString("block.end"));

        for (Device d : devices) {
            d.getDeviceBlock().setBlockText();
            ArrayList<Capability> caps = d.getCapabilities();
            for (int i = 0; i < caps.size(); i++) {
                caps.get(i).getBlock().setBlockText();
            }
        }

//        for (Capability c : capabilities) {
//            c.getBlock().setBlockText();
//        }
    }

    //use -1 as index to add a holder to the end
    private void addBlockHolder(int index, boolean withCondition, boolean repeat) {
        Holder holder = new Holder(this);
        if (withCondition) {
            holder = new ConditionBlock(this);
        }
        if (repeat){
            holder = new RepeatBlock(this);
        }
        if (index >= 0) {
            holders.add(index, holder);
        } else {
            holders.add(holder);
        }
        editorBox.getChildren().remove(1, editorBox.getChildren().size() - 1);
        editorBox.getChildren().addAll(1, holders);
    }

    public void changeHolderType(Holder holder, Node node) {
        String holderType = holder.getClass().getName();
        int index = holders.indexOf(holder);
        holders.remove(index);
        if(node == null){
            addBlockHolder(index, false, false);
        } else if(holderType.contains("Holder")) {
            if(node.getId().contains("control")){
                addBlockHolder(index, false, true);
            }
            else if(node.getId().contains("actionC")){
                addBlockHolder(index, true, false);
            }
        } else if(holderType.contains("ConditionBlock")) {
            if(node.getId().contains("control")){
                addBlockHolder(index, false, true);
            }
            else if(node.getId().contains("action")){
                addBlockHolder(index, false, false);
            }
        } else if(holderType.contains("RepeatBlock")){
            if(node.getId().contains("actionC")){
                addBlockHolder(index, true, false);
            }
            else if(node.getId().contains("action")){
                addBlockHolder(index, false, false);
            }
            else if(node.getId().contains("control")){
                addBlockHolder(index, false, true);
            }
        }
        if (node != null) {
            holders.get(index).addElementToVbox(node);
        }
    }

    public void addHolderAfterMe(Holder holder, boolean fromBtn) {
        int index = holders.indexOf(holder);

        //if it's the last holder
        if (index == (holders.size() - 1)) {
            addBlockHolder(-1, false, false);
        } else if (fromBtn) {
            addBlockHolder(index + 1, false, false);
        }
    }

    public void deleteHolder(Holder holder) {
        int index = holders.indexOf(holder);
        //if there are more than 1 holders
        if (holders.size() > 1) {
            holders.remove(index);
            editorBox.getChildren().remove(1, editorBox.getChildren().size() - 1);
            editorBox.getChildren().addAll(1, holders);
        } else {
            FeedBackLogger.sendBadMessage("Sorry, You cannot delete the only holder");
        }
    }

    private void setEventHandlers() {
//        addDeviceBtn.setOnAction((ActionEvent event) -> {
//            addBlock(devicesBox);
//        });
//        addCapBtn.setOnAction((ActionEvent event) -> {
//            FeedBackLogger.sendGoodMessage("This is a happy message!");
//        });
//        addHolderBtn.setOnAction((ActionEvent event) -> {
//            FeedBackLogger.sendBadMessage("This is a sad message!");
//        });

        runBtn.setOnAction((ActionEvent event) -> {

            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    runProgram();
                }
            });
        });

        editorPane.setOnDragDropped((DragEvent event) -> {
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

                    editorPane.getChildren().add(draggedBlock);
                    draggedBlock.setLayoutX(event.getX());
                    draggedBlock.setLayoutY(event.getY());
                    if (draggedBlock.getCapability().getType().equals("condition")) {
                        draggedBlock.disableTextField(false);
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        editorPane.setOnDragOver((DragEvent event) -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        editorBox.setOnDragDropped((DragEvent event) -> {
        });

        editorBox.setOnDragOver((DragEvent event) -> {
            if (event.getGestureSource() != editorBox
                    && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.NONE);
            }
            event.consume();
        });

//        super.getActions().getChildren().addListener(new AbstractNotifyListener() {
//
//            @Override
//            public void invalidated(Observable observable) {
//                changeBackToHolder();
//            }
//        });
    }

    public void addDeviceBlock(Device dev) {

        dev.addToPane(devicesBox);
        devices.add(dev);

        FeedBackLogger.sendGoodMessage(dev.getDeviceName() + " is connected!");

    }

    public void removeBlocks(int address) {
        for (Device d : devices) {
            if (address == d.getAddress()) {
                d.getDeviceBlock().removeMe();
                for (Capability cap : d.getCapabilities()) {
                    cap.getBlock().removeMe();
                }
                devices.remove(d);
                FeedBackLogger.sendBadMessage(d.getDeviceName() + " is disconnected!");
                break;
            }
        }

    }

    public void addCapabilityBlock(Capability cap, Device dev, boolean hasTestButton) {

        capabilities.add(cap);
        if (dev != null) {
            dev.addCapability(cap);
        }
        ActionBlock action = new ActionBlock(cap.getBlock(), hasTestButton);
        action.addToPane(capabilityBox);

    }

    public void runProgram() {

//        while (true) {
//            
//            try {
//                ComPortController.writeComPort(ComPortController.port, 10,"h");
//                Thread.sleep(500);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(MainEditorController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            
//        }
        Dialog dlg = new Dialog(null, Strings.getString("message.fromsifeb"));
        dlg.setResizable(false);
        dlg.setIconifiable(false);
        dlg.setGraphic(new ImageView(playImg));
        dlg.setMasthead(Strings.getString("message.runtheprogram"));
        Dialog.Actions.YES.textProperty().set(Strings.getString("btn.yes"));
        Dialog.Actions.NO.textProperty().set(Strings.getString("btn.no"));
        dlg.getActions().addAll(Dialog.Actions.YES, Dialog.Actions.NO);

        Action response = dlg.show();
        System.out.println("response" + response);

        if (response == Dialog.Actions.YES) {
            // FeedBackLogger.sendGoodMessage(Strings.getString("message.testing") + " \'" + cp.getCapName() + "\' " + Strings.getString("message.capability") + "...");
            // ComPortController.writeComPort(ComPortController.port, cp.getDevice().getAddress(), cp.getCommand());
            FeedBackLogger.sendGoodMessage("Program is running!");
            devicesBox.setDisable(true);
            capabilityBox.setDisable(true);
            editorBox.setDisable(true);

            int numSteps = holders.size();
            for (int i = 0; i < numSteps; i++) {
                Holder h = holders.get(i);
                h.toggleHighlight(true);
                if (h.getClass().getName().contains("ConditionBlock")) {
                    ConditionBlock cb = (ConditionBlock) holders.get(i);
                    if ((cb.getActions().getChildren().size() == 0) || (cb.getCondition().getChildren().size() == 0)) {
                        h.toggleHighlight(false);
                        continue;
                    }
                    Block acBlock = (Block) cb.getActions().getChildren().get(0);
                    int address = acBlock.getCapability().getDevice().getAddress();
                    String cmd = acBlock.getCapability().getCommand();
                    sendCmd(address, cmd.toUpperCase());

                    Block conBlock = (Block) cb.getCondition().getChildren().get(0);
                    executeConstraint(conBlock);

                    sendCmd(10, "f");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MainEditorController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    if (h.getActions().getChildren().size() == 0) {
                        h.toggleHighlight(false);
                        continue;
                    }
                    Block acBlock = (Block) h.getActions().getChildren().get(0);
                    int address = acBlock.getCapability().getDevice().getAddress();
                    String cmd = acBlock.getCapability().getCommand();
                    sendCmd(address, cmd);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MainEditorController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                h.toggleHighlight(false);
            }
            devicesBox.setDisable(false);
            capabilityBox.setDisable(false);
            editorBox.setDisable(false);
            FeedBackLogger.sendGoodMessage("Program Finished!");

        } else {
            FeedBackLogger.sendBadMessage(Strings.getString("message.testlater") + "...");
            // ... user cancelled, reset form to default
        }

    }

    public void sendCmd(int address, String message) {
        ackReceived = false;
        ComPortController.writeComPort(ComPortController.port, address, message);
        //    while(!ackReceived){}
//        try {
//            Thread.sleep(1500);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(MainEditorController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void executeConstraint(Block constBlock) {
        String constID = constBlock.getCapability().getCapID();
        switch (constID) {
            case "cap_def1": {   //time
                double v = Double.parseDouble(constBlock.getTextField().getText());
                try {

                    Thread.sleep((long) ((v == 0) ? 0 : (v) * 1000));
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainEditorController.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case "cap_def2": {   //distance
                double v = Double.parseDouble(constBlock.getTextField().getText());
                try {
                    Thread.sleep((long) ((v == 0) ? 0 : (v/30) * 1000)); //should be changed to distance

//                    Thread.sleep((v/speed) * 1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainEditorController.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case "cap_008": {    //no object
                hValue = 0;
                while (hValue < 20) {
                    sendCmd(10, "h");
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MainEditorController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            }
            case "cap_009": {    //see object
                hValue = 1000;
                while (hValue > 20) {
                    System.out.println("h value is - " + hValue);
                    sendCmd(10, "h");
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MainEditorController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            }
        }
    }

    public ImageView getFbFace() {
        return fbFace;
    }

    public Label getFbText() {
        return fbText;
    }
}
