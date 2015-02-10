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
import com.sifeb.ve.IfBlock;
import com.sifeb.ve.MainApp;
import com.sifeb.ve.RepeatBlock;
import static com.sifeb.ve.controller.ComPortController.serialPort;
import com.sifeb.ve.handle.BlockCreator;
import com.sifeb.ve.handle.CodeGenerator;
import com.sifeb.ve.resources.Strings;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
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
import javafx.scene.paint.Color;
import jssc.SerialPortException;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

/**
 *
 * @author Udith Arosha
 * @author Hashini Senaratne
 */
public class MainEditorController implements Initializable {
    
    @FXML
    Button addDeviceBtn;
    @FXML
    Button addCapBtn;
    @FXML
    Button addHolderBtn;
    @FXML
    Button runBtn, clearBtn, uploadBtn;
    @FXML
    VBox devicesBox;
    @FXML
    VBox capabilityBox;
    @FXML
    Pane editorPane;
    @FXML
    VBox editorBox;
    @FXML
    VBox mainBox;
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
    @FXML
    Button programBtn;
    @FXML
    Button refreshConnBtn;
    @FXML
    Label connectedText;
    @FXML
    ImageView connectedImg;
    @FXML
    ToolBar toolBar;

//    ArrayList<Holder> holders;
    Holder lastHolder;
    Image playImg, clearImg, uploadImg;
    CodeGenerator codeGenerator;
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
        
        editorBox.setFillWidth(false);
        mainBox.setSpacing(-17);
        editorBox.setSpacing(-15);
        editorBox.setAlignment(Pos.TOP_LEFT);
        addStartEndBlocks();
        addBlockHolder(0, editorBox, 0);
        
        setEventHandlers();
        FeedBackLogger.setControls(this.fbFace, this.fbText);
        setFeedbackPanel();
        
        checkConnection(true);
        setTextStrings();
        codeGenerator = new CodeGenerator();
    }
    
    private void checkConnection(boolean initialCheck) {
        if (initialCheck) {
            Image img = new Image(MainApp.class.getResourceAsStream("/com/sifeb/ve/images/Conn_refresh.png"));
            refreshConnBtn.setGraphic(new ImageView(img));
            refreshConnBtn.setTooltip(new Tooltip("Retry Connection"));
        }
        if (ComPortController.serialPort.isOpened()) {
            Image img = new Image(MainApp.class.getResourceAsStream("/com/sifeb/ve/images/connected.png"));
            connectedImg.setImage(img);
            connectedText.setTextFill(Color.FORESTGREEN);
            connectedText.setText("Robot Connected");
            refreshConnBtn.setVisible(false);
        } else {
            Image img = new Image(MainApp.class.getResourceAsStream("/com/sifeb/ve/images/disconnected.png"));
            connectedImg.setImage(img);
            connectedText.setTextFill(Color.CRIMSON);
            connectedText.setText("Robot Not Connected");
            refreshConnBtn.setVisible(true);
        }
    }
    
    private void setFeedbackPanel() {
        Image img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/bubbleLeft.png"));
        fbLeft.setImage(img);
        img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/bubbleRight.png"));
        fbRight.setImage(img);
        img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/bubbleMid.png"));
        fbText.setBackground(new Background(new BackgroundImage(img, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        playImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/play.png"));
        clearImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/clear.png"));
        uploadImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/upload.png"));
        FeedBackLogger.sendWelcomeMessage();
    }
    
    private void addStartEndBlocks() {
        Image img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/Start_V.png"));
        mainBox.getChildren().add(0, new Block(img));
        
        img = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/Stop_V.png"));
        mainBox.getChildren().add(new Block(img));
    }
    
    public void setTextStrings() {
        toolBar.setMinWidth(440);
        runBtn.setMinWidth(140);
        runBtn.setText(Strings.getString("btn.run"));
        uploadBtn.setMinWidth(140);
        uploadBtn.setText(Strings.getString("btn.upload"));
        clearBtn.setText(Strings.getString("btn.clear"));
        clearBtn.setMinWidth(140);
        haveLabel.setText(Strings.getString("label.have"));
        doLabel.setText(Strings.getString("label.do"));
        
        ((Block) mainBox.getChildren().get(0)).setBlockText(Strings.getString("block.start"));
        ((Block) mainBox.getChildren().get(mainBox.getChildren().size() - 1)).setBlockText(Strings.getString("block.end"));
        
        for (Device d : devices) {
            d.getDeviceBlock().setBlockText();
            ArrayList<Capability> caps = d.getCapabilities();
            for (int i = 0; i < caps.size(); i++) {
                caps.get(i).getBlock().setBlockText();
            }
        }
    }

    //use -1 as index to add a holder to the end
    //type 0 = normal holder
    //type 1 = condition block
    //type 2 = repeat block
    //type 3 = if block
    public void addBlockHolder(int index, VBox parent, int type) {
        Holder holder;
        switch (type) {
            case 1:
                holder = new ConditionBlock(this);
                break;
            case 2:
                holder = new RepeatBlock(this);
                break;
            case 3:
                holder = new IfBlock(this);
                break;
            default:
                holder = new Holder(this);
                break;
        }
        
        if (index >= 0) {
            parent.getChildren().add(index, holder);
        } else {
            parent.getChildren().add(holder);
        }
    }
    
    public void changeHolderType(Holder holder, VBox parent, Node node) {
        String holderType = holder.getClass().getName();
        int index = parent.getChildren().indexOf(holder);
        parent.getChildren().remove(index);
        
        if (node == null) {
            addBlockHolder(index, parent, 0);
        } else if (node.getId().contains(Capability.CAP_ACTION_C)) {
            addBlockHolder(index, parent, 1);
        } else if (node.getId().contains(Capability.CAP_CONTROL)) {
            addBlockHolder(index, parent, 2);
        } else if (node.getId().contains(Capability.CAP_ACTION)) {
            addBlockHolder(index, parent, 0);
        } else if (node.getId().contains(Capability.CAP_IFELSE)) {
            addBlockHolder(index, parent, 3);
        }
        if (node != null) {
            ((Holder) parent.getChildren().get(index)).addElementToVbox(node);
        }
    }
    
    public void addHolderAfterMe(Holder holder, VBox parent, boolean fromBtn) {
        int index = parent.getChildren().indexOf(holder);

        //if it's the last holder
        if (index == (parent.getChildren().size() - 1)) {
            addBlockHolder(-1, parent, 0);
        } else if (fromBtn) {
            addBlockHolder(index + 1, parent, 0);
        }
    }
    
    public void deleteHolder(Holder holder, VBox parent) {
        int index = parent.getChildren().indexOf(holder);
        //if there are more than 1 holders
        if (parent.getChildren().size() > 1) {
            parent.getChildren().remove(index);
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
        refreshConnBtn.setOnAction((event) -> {
            refreshConnBtn.setDisable(true);
            try {
                ComPortController.closePort();
                Thread.sleep(2000);
                ComPortController.openPort();
                checkConnection(false);
            } catch (InterruptedException ex) {
                Logger.getLogger(RootController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                refreshConnBtn.setDisable(false);
            }
        });
        
        runBtn.setOnAction((ActionEvent event) -> {
            
            Platform.runLater(new Runnable() {
                
                @Override
                public void run() {
                    
                    Dialog dlg = new Dialog(null, Strings.getString("message.fromsifeb"));
                    dlg.setResizable(false);
                    dlg.setIconifiable(false);
                    dlg.setGraphic(new ImageView(playImg));
                    dlg.setMasthead(Strings.getString("message.runtheprogram"));
                    Dialog.Actions.YES.textProperty().set(Strings.getString("btn.yes"));
                    Dialog.Actions.NO.textProperty().set(Strings.getString("btn.no"));
                    dlg.getActions().addAll(Dialog.Actions.YES, Dialog.Actions.NO);
                    
                    Action response = dlg.show();
                    
                    if (response == Dialog.Actions.YES) {
                        FeedBackLogger.sendGoodMessage("Program is running!");
                        ComPortController.writeComPort("r");
                    } else {
                        FeedBackLogger.sendBadMessage("We will check later!!!");
                        // ... user cancelled, reset form to default
                    }
                    
                }
            });
        });
        
        uploadBtn.setOnAction((ActionEvent event) -> {
            
            Platform.runLater(new Runnable() {
                
                @Override
                public void run() {
//                    runProgram();

                    Dialog dlg = new Dialog(null, Strings.getString("message.fromsifeb"));
                    dlg.setResizable(false);
                    dlg.setIconifiable(false);
                    dlg.setGraphic(new ImageView(uploadImg));
                    dlg.setMasthead(Strings.getString("message.uploadtheprogram"));
                    Dialog.Actions.YES.textProperty().set(Strings.getString("btn.yes"));
                    Dialog.Actions.NO.textProperty().set(Strings.getString("btn.no"));
                    dlg.getActions().addAll(Dialog.Actions.YES, Dialog.Actions.NO);
                    
                    Action response = dlg.show();
                    
                    if (response == Dialog.Actions.YES) {
                        
                        byte[] sendingData = codeGenerator.generateCode(editorBox);
                        ComPortController.removeEventListener();
                        boolean success = false;
                        
                        for (int i = 0; i < 5; i++) {
                            try {
                                ComPortController.writeComPort("u");
                                int size = sendingData.length;
                                serialPort.writeByte((byte) size);
                                ComPortController.writeProgram(sendingData);
                                byte[] receivedData = ComPortController.read(size);
                                
                                if (Arrays.equals(receivedData, sendingData)) {
                                    success = true;
                                    break;
                                }
                            } catch (SerialPortException ex) {
                                Logger.getLogger(MainEditorController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                        }
                        ComPortController.setEventListener();
                        
                        if (success) {
                            FeedBackLogger.sendGoodMessage("Program is sucessfully uploaded to SiFEB!");
                        } else {
                            FeedBackLogger.sendBadMessage("Program did not upload successfully!");
                        }
                        
                    } else {
                        FeedBackLogger.sendBadMessage("We will upload later!!!");
                        // ... user cancelled, reset form to default
                    }
                    
                }
            });
        });
        
        clearBtn.setOnAction((ActionEvent event) -> {

            Dialog dlg = new Dialog(null, Strings.getString("message.fromsifeb"));
            dlg.setResizable(false);
            dlg.setIconifiable(false);
            dlg.setGraphic(new ImageView(clearImg));
            dlg.setMasthead(Strings.getString("message.cleartheprogram"));
            Dialog.Actions.YES.textProperty().set(Strings.getString("btn.yes"));
            Dialog.Actions.NO.textProperty().set(Strings.getString("btn.no"));
            dlg.getActions().addAll(Dialog.Actions.YES, Dialog.Actions.NO);

            Action response = dlg.show();

            if (response == Dialog.Actions.YES) {

                clearEditorVbox();
                FeedBackLogger.sendGoodMessage("Program is Cleared Successfully!!!");
            } else {
                FeedBackLogger.sendGoodMessage("Program is not Cleared!!!");

            }
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
                    if (draggedBlock.getCapability().getType().equals(Capability.CAP_CONDITION)) {
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
        
        String id = dev.getDeviceID();
        int count = 1;
        for (Device d : devices) {
            if (d.getDeviceID().equals(id)) {
                d.getDeviceBlock().setDevNum(count, true);
                count++;
            }
        }
        if (count > 1) {
            dev.getDeviceBlock().setDevNum(count, true);
        }
        dev.addToPane(devicesBox);
        devices.add(dev);
        
        FeedBackLogger.sendGoodMessage(dev.getDeviceName() + " is connected!");
        
    }
    
    public void removeBlocks(int address) {
        for (Device d : devices) {
            if (address == d.getAddress()) {
                String id = d.getDeviceID();
                d.getDeviceBlock().removeMe();
                for (Capability cap : d.getCapabilities()) {
                    cap.getBlock().removeMe();
                }
                devices.remove(d);
                
                int count = 0;                
                for (Device d1 : devices) {
                    if (d1.getDeviceID().equals(id)) {
                        count++;
                    }
                }
                if (count > 1) {
                    count = 1;
                    for (Device d1 : devices) {
                        if (d1.getDeviceID().equals(id)) {
                            d1.getDeviceBlock().setDevNum(count, true);
                            count++;
                        }
                    }
                }else if(count==1){
                    for (Device d1 : devices) {
                        if (d1.getDeviceID().equals(id)) {
                            d1.getDeviceBlock().setDevNum(1, false);
                            break;
                        }
                    }
                }
                
                FeedBackLogger.sendBadMessage(d.getDeviceName() + " is disconnected!");
                break;
            }
        }
        
    }
    
    public void addCapabilityBlock(Capability cap) {
        capabilities.add(cap);
        ActionBlock action = new ActionBlock(cap.getBlock(), cap.isHasTest());
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
        
        if (response == Dialog.Actions.YES) {
            // FeedBackLogger.sendGoodMessage(Strings.getString("message.testing") + " \'" + cp.getCapName() + "\' " + Strings.getString("message.capability") + "...");
            // ComPortController.writeComPort(ComPortController.port, cp.getDevice().getAddress(), cp.getCommand());
            FeedBackLogger.sendGoodMessage("Program is running!");
            devicesBox.setDisable(true);
            capabilityBox.setDisable(true);
            editorBox.setDisable(true);
            
            int numSteps = editorBox.getChildren().size();
            for (int i = 0; i < numSteps; i++) {
                Holder h = (Holder) editorBox.getChildren().get(i);
                h.toggleHighlight(true);
                if (h.getClass().getName().contains("ConditionBlock")) {
                    ConditionBlock cb = (ConditionBlock) editorBox.getChildren().get(i);
                    runConditionBlock(cb);
                } else if (h.getClass().getName().contains("RepeatBlock")) {
                    RepeatBlock rb = (RepeatBlock) editorBox.getChildren().get(i);
                    runRepeatBlock(rb);
                } else if (h.getClass().getName().contains("IfBlock")) {
                    IfBlock ib = (IfBlock) editorBox.getChildren().get(i);
                    runIfBlock(ib);
                } else {
                    runBlock(h);
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
    
    public void runConditionBlock(ConditionBlock cb) {
        if ((cb.getActions().getChildren().size() == 0) || (cb.getCondition().getChildren().size() == 0)) {
            cb.toggleHighlight(false);
            return;
        }
        Block acBlock = (Block) cb.getActions().getChildren().get(0);
        int address = acBlock.getCapability().getDevice().getAddress();
        String cmd = acBlock.getCapability().getTestCommand();
        sendCmd(address, cmd.toUpperCase());
        Block conBlock = (Block) cb.getCondition().getChildren().get(0);
        executeConstraint(conBlock);
        sendCmd(10, "f");                                               // need to generalize to make neutral behaviours
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainEditorController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void runBlock(Holder h) {
        if (h.getActions().getChildren().size() == 0) {
            h.toggleHighlight(false);
            return;
        }
        Block acBlock = (Block) h.getActions().getChildren().get(0);
        int address = acBlock.getCapability().getDevice().getAddress();
        String cmd = acBlock.getCapability().getTestCommand();
        sendCmd(address, cmd);
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainEditorController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void runRepeatBlock(RepeatBlock rb) {
        if ((rb.getActions().getChildren().size() == 0) || (rb.getCondition().getChildren().size() == 0)
                || rb.getHolders().getChildren().size() == 0) {
            rb.toggleHighlight(false);
            return;
        }
        int numRepeatSteps = rb.getHolders().getChildren().size();
        for (int j = 0; j < numRepeatSteps; j++) {
            Holder rh = (Holder) rb.getHolders().getChildren().get(j);
            rh.toggleHighlight(true);
            if (rh.getClass().getName().contains("ConditionBlock")) {
                ConditionBlock cb = (ConditionBlock) rb.getHolders().getChildren().get(j);
                runConditionBlock(cb);
            } else if (rh.getClass().getName().contains("RepeatBlock")) {
                RepeatBlock rb1 = (RepeatBlock) rb.getHolders().getChildren().get(j);
                runRepeatBlock(rb1);
            } else if (rh.getClass().getName().contains("IfBlock")) {
                IfBlock ib = (IfBlock) rb.getHolders().getChildren().get(j);
                runIfBlock(ib);
            } else {
                runBlock(rh);
            }
        }
    }
    
    public void runIfBlock(IfBlock ib) {
        if ((ib.getActions().getChildren().size() == 0) || (ib.getCondition().getChildren().size() == 0)
                || ib.getIfHolders().getChildren().size() == 0) {
            ib.toggleHighlight(false);
            return;
        }
        Block conBlock = (Block) ib.getCondition().getChildren().get(0);
        boolean condition = getConstraint(conBlock);
        int numSteps;
        VBox ieVbox;
        if (condition) {
            numSteps = ib.getIfHolders().getChildren().size();
            ieVbox = ib.getIfHolders();
        } else {
            numSteps = ib.getElseHolders().getChildren().size();
            ieVbox = ib.getElseHolders();
        }
        for (int j = 0; j < numSteps; j++) {
            Holder ieh = (Holder) ieVbox.getChildren().get(j);
            ieh.toggleHighlight(true);
            if (ieh.getClass().getName().contains("ConditionBlock")) {
                ConditionBlock cb = (ConditionBlock) ieVbox.getChildren().get(j);
                runConditionBlock(cb);
            } else if (ieh.getClass().getName().contains("RepeatBlock")) {
                RepeatBlock rb = (RepeatBlock) ieVbox.getChildren().get(j);
                runRepeatBlock(rb);
            } else if (ieh.getClass().getName().contains("IfBlock")) {
                IfBlock ib1 = (IfBlock) ieVbox.getChildren().get(j);
                runIfBlock(ib1);
            } else {
                runBlock(ieh);
            }
        }
    }
    
    public void sendCmd(int address, String message) {
        ackReceived = false;
        ComPortController.writeComPort(message);
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
                    Logger.getLogger(MainEditorController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case "cap_def2": {   //distance
                double v = Double.parseDouble(constBlock.getTextField().getText());
                try {
                    Thread.sleep((long) ((v == 0) ? 0 : (v / 30) * 1000)); //should be changed to distance

//                    Thread.sleep((v/speed) * 1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainEditorController.class
                            .getName()).log(Level.SEVERE, null, ex);
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
                        Logger.getLogger(MainEditorController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            }
            case "cap_009": {    //see object
                hValue = 1000;
                while (hValue > 20) {
                    sendCmd(10, "h");
                    try {
                        Thread.sleep(50);
                        
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MainEditorController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            }
        }
    }
    
    public boolean getConstraint(Block constBlock) {    // need to generalize to test constraints
        String constID = constBlock.getCapability().getCapID();
        switch (constID) {
            case "cap_008": {    //no object
                hValue = 0;
                sendCmd(10, "h");   //where does h get update?
                try {
                    Thread.sleep(50);
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainEditorController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                if (hValue > 20) {
                    return true;
                } else {
                    return false;
                }
            }
            case "cap_009": {    //see object
                hValue = 1000;
                try {
                    Thread.sleep(50);
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainEditorController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                sendCmd(10, "h");   //where does h get update?
                if (hValue < 20) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
    
    public ImageView getFbFace() {
        return fbFace;
    }
    
    public Label getFbText() {
        return fbText;
    }
    
    @FXML
    private void goToProgram(ActionEvent event) {
    }
    
    @FXML
    private void goToHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(MainApp.HomeFile));
            MainApp.setPane((Pane) loader.load());
            Scene scene = new Scene(MainApp.getPane());
            MainApp.getStage().setScene(scene);
            MainApp.getStage().setMaximized(false);
            MainApp.getStage().setResizable(false);
            MainApp.getStage().setWidth(MainApp.InitialScreenWidth);
            MainApp.getStage().setHeight(MainApp.InitialScreenHeight);
            MainApp.getStage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Device getConnectedDevice(String devId, int address) {
        
        for (int i = 0; i < devices.size(); i++) {
            Device device = devices.get(i);
            if (device.getDeviceID().equals(devId) && device.getAddress() == address) {
                return device;
            }
        }
        return null;
    }
    
    public void clearEditorVbox() {
        editorBox.getChildren().clear();
        addBlockHolder(0, editorBox, 0);
    }
    
    public VBox getEditorBox() {
        return editorBox;
    }
    
    public boolean checkDeviceAddress(String address) {
        
        boolean isSameAddress = false;
        for (Device device : devices) {
            String devAd = Integer.toString(device.getAddress());
            if (devAd.equals(address)) {
                isSameAddress = true;
                break;
            }
        }
        return isSameAddress;
    }
}
