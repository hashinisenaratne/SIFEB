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
import com.sifeb.ve.Device;
import com.sifeb.ve.FeedBackLogger;
import com.sifeb.ve.Holder;
import com.sifeb.ve.resources.Strings;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    //temp
    ArrayList<Device> devices;
    ArrayList<Capability> capabilities;
   
//    VBox messageBox;
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
        addBlockHolder(0, false);
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
    private void addBlockHolder(int index, boolean withCondition) {
        Holder holder = new Holder(this);
        if (withCondition) {
            holder = new ConditionBlock(this);
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
        if (holderType.contains("Holder")) {
            addBlockHolder(index, true);
        } else {
            addBlockHolder(index, false);
        }
        if (node != null) {
            holders.get(index).addElementToVbox(node);
        }
    }

    public void addHolderAfterMe(Holder holder) {
        int index = holders.indexOf(holder);
        //if it's the last holder
        if (index == (holders.size() - 1)) {
            addBlockHolder(-1, false);
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
            FeedBackLogger.sendGoodMessage("Program is running!");
//            ComPortController.writeComPort(ComPortController.port, 10, "r");
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

//    //for testing only
//    private void addBlock(VBox parent) {
//
//        System.out.println(parent.getId());
//        String parentId = parent.getId();
//
//        if (parentId.equals("devicesBox")) {
//
//            Device device = new Device("00001", "Wheels", 10, "actuator", "Mwheels.png");
//            device.addToPane(devicesBox);
//            devices.add(device);
//
//            device = new Device("00002", "Object Detector", 11, "sensor", "Msonar.png");
//            device.addToPane(devicesBox);
//            devices.add(device);
//
//            device = new Device("00003", "Lights", 12, "actuator", "Mlight.png");
//            device.addToPane(devicesBox);
//            devices.add(device);
//
//        } else if (parentId.equals("capabilityBox")) {
//
//            //adding actions
//            String[] actionNames_en = {"Go Forward", "Reverse", "Turn Left", "Turn Right", "Stop", "Light ON", "Light OFF"};
//            String[] actionNames_si = {"ඉදිරියට යන්න", "පසුපසට යන්න", "වමට හැරෙන්න", "දකුණට හැරෙන්න", "නවතින්න", "Light ON", "Light OFF"};
//            String[] actionCmd = {"b", "c", "e", "d", "", "l", ""};
//            for (int i = 1; i < 8; i++) {
//                Device d;
//                String type;
//                if (i <= 5) {
//                    d = devices.get(0);
//                } else {
//                    d = devices.get(2);
//                }
//                if (i < 3 || i > 5) {
//                    type = "actionC";
//                } else {
//                    type = "action";
//                }
////                Capability cap = new Capability("1000" + i, actionNames[i - 1], d, type, actionCmd[i - 1], "ActionA" + i + ".png");
//                Map<Locale, String> actNames = new HashMap<>();
//                actNames.put(new Locale("en", "US"), actionNames_en[i - 1]);
//                actNames.put(new Locale("si", "LK"), actionNames_si[i - 1]);
//
//                Capability cap = new Capability("1000" + i, actNames, d, type, actionCmd[i - 1], "right");
//                capabilities.add(cap);
//                d.addCapability(cap);
//                if (i == 5 || i == 7) {
//                    ActionBlock action = new ActionBlock(cap.getBlock(), false);
//                    action.addToPane(capabilityBox);
//                } else {
//                    ActionBlock action = new ActionBlock(cap.getBlock(), true);
//                    action.addToPane(capabilityBox);
//                }
//
//            }
//
//            //adding senses
//            String[] senseNames_en = {"No Object", "See Object"};
//            String[] senseNames_si = {"No Object", "See Object"};
//            String[] senseCmd = {"s1", "s2"};
//            for (int i = 1; i <= 2; i++) {
//                Map<Locale, String> senseNames = new HashMap<>();
//                senseNames.put(new Locale("en", "US"), senseNames_en[i - 1]);
//                senseNames.put(new Locale("si", "LK"), senseNames_si[i - 1]);
////                Capability cap = new Capability("2000" + i, senseNames[i - 1], devices.get(1), "sense", senseCmd[i - 1], "Sense" + i + ".png");
//                Capability cap = new Capability("2000" + i, senseNames, devices.get(1), "sense", senseCmd[i - 1], "Sense"+i);
//                capabilities.add(cap);
//                devices.get(1).addCapability(cap);
//
//                ActionBlock action = new ActionBlock(cap.getBlock(), true);
//                action.addToPane(capabilityBox);
//
//            }
//
//            //adding conditions
//            for (int i = 1; i <= 3; i++) {
//                Map<Locale, String> condNames = new HashMap<>();
//                condNames.put(new Locale("en", "US"), "Time is");
//                condNames.put(new Locale("si", "LK"), "කාලය");
//                Capability cap = new Capability("3000" + i, condNames, null, "condition", "", "Constraint" + i);
//                if (i == 3) {
//                    cap = new Capability("3000" + i, condNames, null, "condition", "", "Constraint" + i);
//                }
//                capabilities.add(cap);
//                ActionBlock action = new ActionBlock(cap.getBlock(), false);
//                action.addToPane(capabilityBox);
//
//            }
//        }
//
//    }
    public ImageView getFbFace() {
        return fbFace;
    }

    public Label getFbText() {
        return fbText;
    }
}
