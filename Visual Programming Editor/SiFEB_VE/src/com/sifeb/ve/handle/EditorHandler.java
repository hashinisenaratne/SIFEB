/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.handle;

import com.sifeb.ve.Block;
import com.sifeb.ve.ConditionBlock;
import com.sifeb.ve.Holder;
import com.sifeb.ve.IfBlock;
import com.sifeb.ve.RepeatBlock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.VBox;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Pubudu
 */
public class EditorHandler {

    private Element rootElement, stepElement;
    private Document doc;
    private FileHandler fileHandler;

    public EditorHandler() {
        fileHandler = new FileHandler();
    }

    public void setUpDocument() {

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
            rootElement = doc.createElement("Sifeb");
            doc.appendChild(rootElement);

            Element mainEditor = doc.createElement("MainEditor");
            rootElement.appendChild(mainEditor);

            stepElement = doc.createElement("Steps");
            mainEditor.appendChild(stepElement);
        } catch (ParserConfigurationException ex) {

            Logger.getLogger(EditorHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void saveFile(String filePath, VBox editorBox) {

        setUpDocument();
        int numSteps = editorBox.getChildren().size();
        System.out.println("steps - " + numSteps);

        for (int i = 0; i < numSteps; i++) {
            Holder h = (Holder) editorBox.getChildren().get(i);

            if (h.getClass().getName().contains("ConditionBlock")) {

                System.out.println("<Step>");
                Element conStep = doc.createElement("Step");
                stepElement.appendChild(conStep);
                ConditionBlock cb = (ConditionBlock) editorBox.getChildren().get(i);
                saveConditionBlocks(cb, conStep);

            } else if (h.getClass().getName().contains("RepeatBlock")) {

                System.out.println("<Step>");
                Element repeatStep = doc.createElement("Step");
                stepElement.appendChild(repeatStep);
                RepeatBlock rb = (RepeatBlock) editorBox.getChildren().get(i);
                saveRepeatBlocks(rb, repeatStep);

            } else if (h.getClass().getName().contains("IfBlock")) {

                IfBlock ib = (IfBlock) editorBox.getChildren().get(i);
                System.out.println("<Step>");
                Element ifStep = doc.createElement("Step");
                stepElement.appendChild(ifStep);
                saveIfBlock(ib, ifStep);

            } else {

                System.out.println("<Step>");
                Element blkStep = doc.createElement("Step");
                stepElement.appendChild(blkStep);
                saveBlock(h, blkStep);
            }

        }

        fileHandler.writeToEditorFile(filePath, doc);
    }

    public void saveConditionBlocks(ConditionBlock cb, Element conStep) {

        Block acBlock = (Block) cb.getActions().getChildren().get(0);
        String devName = acBlock.getCapability().getDevice().getDeviceName();
        String devLocation = Integer.toString(acBlock.getCapability().getDevice().getAddress());
        String capabilityId = acBlock.getCapability().getCapID();

        System.out.println("<block>ConditionBlock</block>");
        Element block = doc.createElement("Block");
        block.appendChild(doc.createTextNode("ConditionalBlock"));
        conStep.appendChild(block);

        System.out.println("<device name>" + devName);
        Element deviceName = doc.createElement("DeviceName");
        deviceName.appendChild(doc.createTextNode(devName));

        System.out.println("<location>" + devLocation);
        Element location = doc.createElement("Location");
        location.appendChild(doc.createTextNode(devLocation));

        System.out.println("<cap id>" + capabilityId);
        Element capId = doc.createElement("CapId");
        capId.appendChild(doc.createTextNode(capabilityId));

        block.appendChild(deviceName);
        block.appendChild(location);
        block.appendChild(capId);

        if ((cb.getActions().getChildren().size() == 0) || (cb.getCondition().getChildren().size() == 0)) {
            return;
        } else {
            Block conBlock = (Block) cb.getCondition().getChildren().get(0);
            String conditionId = conBlock.getCapability().getCapID();

            System.out.println("<block>Condition</block>");
            Element conditionBlk = doc.createElement("Block");
            conditionBlk.appendChild(doc.createTextNode("Condition"));
            conStep.appendChild(conditionBlk);

            System.out.println("<conId>" + conditionId);
            Element conCapId = doc.createElement("CapId");
            conCapId.appendChild(doc.createTextNode(conditionId));
            conditionBlk.appendChild(conCapId);

            if (conditionId.equals("cap_def1") || conditionId.equals("cap_def2")) {
                String conditionValue = conBlock.getTextField().getText();
                System.out.println("<conValue>" + conditionValue);
                Element conValue = doc.createElement("ConValue");
                conValue.appendChild(doc.createTextNode(conditionValue));
                conditionBlk.appendChild(conValue);
            }
        }
    }

    public void saveRepeatBlocks(RepeatBlock rb, Element repeatStep) {

        System.out.println("<block>RepeatBlock</block>");
        Element repeatBlk = doc.createElement("Block");
        repeatBlk.appendChild(doc.createTextNode("RepeatBlock"));
        repeatStep.appendChild(repeatBlk);

        if ((rb.getCondition().getChildren().size() != 0)) {
            Block conBlk = (Block) rb.getCondition().getChildren().get(0);
            String capId = conBlk.getCapability().getCapID();

            System.out.println("<block>Condition</block>");
            Element conditionBlk = doc.createElement("Block");
            conditionBlk.appendChild(doc.createTextNode("Condition"));
            repeatStep.appendChild(conditionBlk);

            System.out.println("<conId>" + capId);
            Element conCapId = doc.createElement("CapId");
            conCapId.appendChild(doc.createTextNode(capId));
            conditionBlk.appendChild(conCapId);

            if (capId.equals("cap_def1") || capId.equals("cap_def2")) {
                String conditionValue = conBlk.getTextField().getText();
                System.out.println("<conValue>" + conditionValue);
                Element conValue = doc.createElement("ConValue");
                conValue.appendChild(doc.createTextNode(conditionValue));
                conditionBlk.appendChild(conValue);
            }
        }

        if ((rb.getActions().getChildren().size() == 0) || rb.getHolders().getChildren().size() == 0) {

            return;
        }

        int numRepeatSteps = rb.getHolders().getChildren().size();
        for (int j = 0; j < numRepeatSteps; j++) {
            Holder rh = (Holder) rb.getHolders().getChildren().get(j);

            if (rh.getClass().getName().contains("ConditionBlock")) {
                ConditionBlock cb = (ConditionBlock) rb.getHolders().getChildren().get(j);
                saveConditionBlocks(cb, repeatStep);
            } else if (rh.getClass().getName().contains("RepeatBlock")) {
                RepeatBlock rb1 = (RepeatBlock) rb.getHolders().getChildren().get(j);
                saveRepeatBlocks(rb1, repeatStep);
            } else if (rh.getClass().getName().contains("IfBlock")) {
                IfBlock ib = (IfBlock) rb.getHolders().getChildren().get(j);
                saveIfBlock(ib, repeatStep);
            } else {
                saveBlock(rh, repeatStep);
            }
        }
    }

    public void saveIfBlock(IfBlock ib, Element ifStep) {

        System.out.println("<block>IfBlock</block>");

        Element ifBlk = doc.createElement("Block");
        ifBlk.appendChild(doc.createTextNode("IfBlock"));
        ifStep.appendChild(ifBlk);

        if (ib.getCondition().getChildren().size() != 0) {

            Block conBlock = (Block) ib.getCondition().getChildren().get(0);
            String capId = conBlock.getCapability().getCapID();
            System.out.println("<block>Condition</block>");
            System.out.println("<conId>" + capId);

            Element conditionBlk = doc.createElement("Block");
            conditionBlk.appendChild(doc.createTextNode("Condition"));
            ifStep.appendChild(conditionBlk);

            Element conCapId = doc.createElement("CapId");
            conCapId.appendChild(doc.createTextNode(capId));
            conditionBlk.appendChild(conCapId);

            if (capId.equals("cap_def1") || capId.equals("cap_def2")) {

                String conditionValue = conBlock.getTextField().getText();
                System.out.println("<conValue>" + conditionValue);
                Element conValue = doc.createElement("ConValue");
                conValue.appendChild(doc.createTextNode(conditionValue));
                conditionBlk.appendChild(conValue);
            }
        }

        int numSteps;
        VBox ieVbox;

        if (ib.getIfHolders().getChildren().size() != 0) {
            numSteps = ib.getIfHolders().getChildren().size();
            ieVbox = ib.getIfHolders();
            System.out.println("<block>IF");
            Element ifConBlk = doc.createElement("Block");
            ifConBlk.appendChild(doc.createTextNode("IF"));
            ifStep.appendChild(ifConBlk);
            saveIfElse(numSteps, ieVbox, ifConBlk);
        }

        if (ib.getElseHolders().getChildren().size() != 0) {
            numSteps = ib.getElseHolders().getChildren().size();
            ieVbox = ib.getElseHolders();
            System.out.println("<block>ELSE");
            Element elseConBlk = doc.createElement("Block");
            elseConBlk.appendChild(doc.createTextNode("ELSE"));
            ifStep.appendChild(elseConBlk);
            saveIfElse(numSteps, ieVbox, elseConBlk);
        }

    }

    public void saveIfElse(int numSteps, VBox ieVbox, Element ifConBlk) {

        for (int j = 0; j < numSteps; j++) {
            Holder ieh = (Holder) ieVbox.getChildren().get(j);
            //ieh.toggleHighlight(true);
            if (ieh.getClass().getName().contains("ConditionBlock")) {
                ConditionBlock cb = (ConditionBlock) ieVbox.getChildren().get(j);
                saveConditionBlocks(cb, ifConBlk);
            } else if (ieh.getClass().getName().contains("RepeatBlock")) {
                RepeatBlock rb = (RepeatBlock) ieVbox.getChildren().get(j);
                saveRepeatBlocks(rb, ifConBlk);
            } else if (ieh.getClass().getName().contains("IfBlock")) {
                IfBlock ib1 = (IfBlock) ieVbox.getChildren().get(j);
                saveIfBlock(ib1, ifConBlk);
            } else {
                saveBlock(ieh, ifConBlk);
            }
        }
    }

    public void saveBlock(Holder h, Element blkStep) {

        if (h.getActions().getChildren().size() == 0) {
            return;
        }
        Block acBlock = (Block) h.getActions().getChildren().get(0);
        String device = acBlock.getCapability().getDevice().getDeviceName();
        String devLocation = Integer.toString(acBlock.getCapability().getDevice().getAddress());
        String capabilityId = acBlock.getCapability().getCapID();

        System.out.println("<block>ConditionBlock</block>");
        System.out.println("<device name>" + device);
        System.out.println("<location>" + devLocation);
        System.out.println("<cap id>" + capabilityId);

        Element block = doc.createElement("Block");
        block.appendChild(doc.createTextNode("ConditionalBlock"));
        blkStep.appendChild(block);

        Element deviceName = doc.createElement("DeviceName");
        deviceName.appendChild(doc.createTextNode(device));

        Element location = doc.createElement("Location");
        location.appendChild(doc.createTextNode(devLocation));

        Element capId = doc.createElement("CapId");
        capId.appendChild(doc.createTextNode(capabilityId));

        block.appendChild(deviceName);
        block.appendChild(location);
        block.appendChild(capId);

    }

    public void loadFile(String filePath, VBox vbox) {

        Element mainEd = fileHandler.readFromEditorFile(filePath);
        NodeList nodeList = mainEd.getElementsByTagName("Steps").item(0).getChildNodes();

        System.out.println("list 000 - " + nodeList.getLength());

      //  int numSteps = editorBox.getChildren().size();
        //  System.out.println("steps - " + numSteps);
//        for (int i = 0; i < nodeList.getLength(); i++) {
//            
//            String blockValue = nodeList.item(i).getChildNodes().getLength();
//            Holder h = (Holder) editorBox.getChildren().get(i);
//
//            if (h.getClass().getName().contains("ConditionBlock")) {
//
//                System.out.println("<Step>");
//                Element conStep = doc.createElement("Step");
//                stepElement.appendChild(conStep);
//                ConditionBlock cb = (ConditionBlock) editorBox.getChildren().get(i);
//                saveConditionBlocks(cb, conStep);
//
//            } else if (h.getClass().getName().contains("RepeatBlock")) {
//
//                System.out.println("<Step>");
//                Element repeatStep = doc.createElement("Step");
//                stepElement.appendChild(repeatStep);
//                RepeatBlock rb = (RepeatBlock) editorBox.getChildren().get(i);
//                saveRepeatBlocks(rb, repeatStep);
//
//            } else if (h.getClass().getName().contains("IfBlock")) {
//
//                IfBlock ib = (IfBlock) editorBox.getChildren().get(i);
//                System.out.println("<Step>");
//                Element ifStep = doc.createElement("Step");
//                stepElement.appendChild(ifStep);
//                saveIfBlock(ib, ifStep);
//
//            } else {
//
//                System.out.println("<Step>");
//                Element blkStep = doc.createElement("Step");
//                stepElement.appendChild(blkStep);
//                saveBlock(h, blkStep);
//            }
//
//        }
    }

}
