/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.handle;

import com.sifeb.ve.Block;
import com.sifeb.ve.Capability;
import com.sifeb.ve.ConditionBlock;
import com.sifeb.ve.Device;
import com.sifeb.ve.Holder;
import com.sifeb.ve.IfBlock;
import com.sifeb.ve.MainApp;
import com.sifeb.ve.RepeatBlock;
import com.sifeb.ve.controller.MainEditorController;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
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
        String devName = acBlock.getCapability().getDevice().getDeviceID();
        String devLocation = Integer.toString(acBlock.getCapability().getDevice().getAddress());
        String capabilityId = acBlock.getCapability().getCapID();

        System.out.println("<block>ConditionBlock</block>");
        Element block = doc.createElement("Block");
        Attr attr = doc.createAttribute("id");
        attr.setValue("ConditionalBlock");
        block.setAttributeNode(attr);
        //block.appendChild(doc.createTextNode("ConditionalBlock"));
        conStep.appendChild(block);

        System.out.println("<device name>" + devName);
        Element deviceName = doc.createElement("DeviceId");
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
            Element conditionBlk = doc.createElement("Condition");
            Attr conAttr = doc.createAttribute("id");
            conAttr.setValue("Condition");
            conditionBlk.setAttributeNode(conAttr);
            //conditionBlk.appendChild(doc.createTextNode("Condition"));
            block.appendChild(conditionBlk);

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
            } else {
                Element conDevice = doc.createElement("DeviceId");
                String conDevId = conBlock.getCapability().getDevice().getDeviceID();
                conDevice.appendChild(doc.createTextNode(conDevId));
                conditionBlk.appendChild(conDevice);

                Element conAddress = doc.createElement("Location");
                String conDevAddress = Integer.toString(conBlock.getCapability().getDevice().getAddress());
                conAddress.appendChild(doc.createTextNode(conDevAddress));
                conditionBlk.appendChild(conAddress);
            }
        }
    }

    public void saveRepeatBlocks(RepeatBlock rb, Element repeatStep) {

        System.out.println("<block>RepeatBlock</block>");
        Element repeatBlk = doc.createElement("Block");
        Attr repeatAttr = doc.createAttribute("id");
        repeatAttr.setValue("RepeatBlock");
        repeatBlk.setAttributeNode(repeatAttr);
        //repeatBlk.appendChild(doc.createTextNode("RepeatBlock"));
        repeatStep.appendChild(repeatBlk);

        if ((rb.getCondition().getChildren().size() != 0)) {
            Block conBlk = (Block) rb.getCondition().getChildren().get(0);
            String capId = conBlk.getCapability().getCapID();

            System.out.println("<block>Condition</block>");
            Element conditionBlk = doc.createElement("Condition");
            Attr conAttr = doc.createAttribute("id");
            conAttr.setValue("Condition");
            conditionBlk.setAttributeNode(conAttr);
            //  conditionBlk.appendChild(doc.createTextNode("Condition"));
            repeatBlk.appendChild(conditionBlk);

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
            } else {
                Element conDevice = doc.createElement("DeviceId");
                String conDevId = conBlk.getCapability().getDevice().getDeviceID();
                conDevice.appendChild(doc.createTextNode(conDevId));
                conditionBlk.appendChild(conDevice);

                Element conAddress = doc.createElement("Location");
                String conDevAddress = Integer.toString(conBlk.getCapability().getDevice().getAddress());
                conAddress.appendChild(doc.createTextNode(conDevAddress));
                conditionBlk.appendChild(conAddress);
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
                saveConditionBlocks(cb, repeatBlk);
            } else if (rh.getClass().getName().contains("RepeatBlock")) {
                RepeatBlock rb1 = (RepeatBlock) rb.getHolders().getChildren().get(j);
                saveRepeatBlocks(rb1, repeatBlk);
            } else if (rh.getClass().getName().contains("IfBlock")) {
                IfBlock ib = (IfBlock) rb.getHolders().getChildren().get(j);
                saveIfBlock(ib, repeatBlk);
            } else {
                saveBlock(rh, repeatBlk);
            }
        }
    }

    public void saveIfBlock(IfBlock ib, Element ifStep) {

        System.out.println("<block>IfBlock</block>");

        Element ifBlk = doc.createElement("Block");
        Attr ifAttr = doc.createAttribute("id");
        ifAttr.setValue("IfBlock");
        ifBlk.setAttributeNode(ifAttr);
        //  ifBlk.appendChild(doc.createTextNode("IfBlock"));
        ifStep.appendChild(ifBlk);

        if (ib.getCondition().getChildren().size() != 0) {

            Block conBlock = (Block) ib.getCondition().getChildren().get(0);
            String capId = conBlock.getCapability().getCapID();
            System.out.println("<block>Condition</block>");
            System.out.println("<conId>" + capId);

            Element conditionBlk = doc.createElement("Condition");
            Attr conAttr = doc.createAttribute("id");
            conAttr.setValue("Condition");
            conditionBlk.setAttributeNode(conAttr);
            //conditionBlk.appendChild(doc.createTextNode("Condition"));
            ifBlk.appendChild(conditionBlk);

            Element conCapId = doc.createElement("CapId");
            conCapId.appendChild(doc.createTextNode(capId));
            conditionBlk.appendChild(conCapId);

            if (capId.equals("cap_def1") || capId.equals("cap_def2")) {

                String conditionValue = conBlock.getTextField().getText();
                System.out.println("<conValue>" + conditionValue);
                Element conValue = doc.createElement("ConValue");
                conValue.appendChild(doc.createTextNode(conditionValue));
                conditionBlk.appendChild(conValue);
            } else {
                Element conDevice = doc.createElement("DeviceId");
                String conDevId = conBlock.getCapability().getDevice().getDeviceID();
                conDevice.appendChild(doc.createTextNode(conDevId));
                conditionBlk.appendChild(conDevice);

                Element conAddress = doc.createElement("Location");
                String conDevAddress = Integer.toString(conBlock.getCapability().getDevice().getAddress());
                conAddress.appendChild(doc.createTextNode(conDevAddress));
                conditionBlk.appendChild(conAddress);
            }
        }

        int numSteps;
        VBox ieVbox;

        if (ib.getIfHolders().getChildren().size() != 0) {
            numSteps = ib.getIfHolders().getChildren().size();
            ieVbox = ib.getIfHolders();
            System.out.println("<block>IF");
            Element ifConBlk = doc.createElement("Block");
            Attr iffAttr = doc.createAttribute("id");
            iffAttr.setValue("IF");
            ifConBlk.setAttributeNode(iffAttr);
            //  ifConBlk.appendChild(doc.createTextNode("IF"));
            ifBlk.appendChild(ifConBlk);
            saveIfElse(numSteps, ieVbox, ifConBlk);
        }

        if (ib.getElseHolders().getChildren().size() != 0) {
            numSteps = ib.getElseHolders().getChildren().size();
            ieVbox = ib.getElseHolders();
            System.out.println("<block>ELSE");
            Element elseConBlk = doc.createElement("Block");
            Attr elseAttr = doc.createAttribute("id");
            elseAttr.setValue("ELSE");
            elseConBlk.setAttributeNode(elseAttr);
            //elseConBlk.appendChild(doc.createTextNode("ELSE"));
            ifBlk.appendChild(elseConBlk);
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
        String device = acBlock.getCapability().getDevice().getDeviceID();
        String devLocation = Integer.toString(acBlock.getCapability().getDevice().getAddress());
        String capabilityId = acBlock.getCapability().getCapID();

        System.out.println("<block>ConditionBlock</block>");
        System.out.println("<device name>" + device);
        System.out.println("<location>" + devLocation);
        System.out.println("<cap id>" + capabilityId);

        Element block = doc.createElement("Block");
        Attr conAttr = doc.createAttribute("id");
        conAttr.setValue("ConditionalBlock");
        block.setAttributeNode(conAttr);
        // block.appendChild(doc.createTextNode("ConditionalBlock"));
        blkStep.appendChild(block);

        Element deviceName = doc.createElement("DeviceId");
        deviceName.appendChild(doc.createTextNode(device));

        Element location = doc.createElement("Location");
        location.appendChild(doc.createTextNode(devLocation));

        Element capId = doc.createElement("CapId");
        capId.appendChild(doc.createTextNode(capabilityId));

        block.appendChild(deviceName);
        block.appendChild(location);
        block.appendChild(capId);

    }

    public void loadFile(String fileName,String filePath, MainEditorController meCtrl) {

        Element mainFile = fileHandler.readFromEditorFile(filePath);
        NodeList stepNodes = mainFile.getElementsByTagName("Steps").item(0).getChildNodes();
        meCtrl.clearEditorVbox();
        MainApp.appendTitle(fileName);

        for (int i = 0; i < stepNodes.getLength(); i++) {
            NodeList blockNodes = stepNodes.item(i).getChildNodes();

            if (blockNodes.getLength() > 0) {
                Element block = (Element) blockNodes.item(0);

                if (block.getAttribute("id").equals("ConditionalBlock")) {

                    Block capBlock = checkActionBlock(block, meCtrl);
                    if (capBlock != null) {

                        int holderIndex = meCtrl.getEditorBox().getChildren().size() - 1;
                        Holder holder = (Holder) meCtrl.getEditorBox().getChildren().get(holderIndex);
                        holder.addToHolder(capBlock);

                        NodeList childNodes = block.getElementsByTagName("Condition");
                        if (childNodes.getLength() > 0) {
                            ConditionBlock cb = (ConditionBlock) meCtrl.getEditorBox().getChildren().get(holderIndex);
                            loadCLBlock(cb, childNodes, meCtrl);
                        }

                    } else {
                        //highlight for not available
                    }

                } else if (block.getAttribute("id").equals("RepeatBlock")) {

                    String repeatBlkId = "cap_def3";
                    Capability capBlk = fileHandler.readFromCapabilityFile(repeatBlkId);
                    int holderIndex = meCtrl.getEditorBox().getChildren().size() - 1;
                    Holder holder = (Holder) meCtrl.getEditorBox().getChildren().get(holderIndex);
                    holder.addToHolder(capBlk.getBlock());
                    RepeatBlock rb = (RepeatBlock) meCtrl.getEditorBox().getChildren().get(holderIndex);

                    Element repeatCondNode = (Element) block.getChildNodes().item(0);

                    if (repeatCondNode != null) {
                        if (repeatCondNode.getAttribute("id").equals("Condition")) {
                            Capability capabilityBlk = loadCondition(repeatCondNode, meCtrl);

                            if (capabilityBlk != null) {
                                rb.addCondition(capabilityBlk.getBlock());
                            }
                        }
                    }
                    NodeList childNodes = block.getChildNodes();
                    loadRepeatBlock(rb, childNodes, meCtrl);

                } else if (block.getAttribute("id").equals("IfBlock")) {

                    String ifBlkId = "cap_def4";
                    Capability capBlk = fileHandler.readFromCapabilityFile(ifBlkId);
                    int holderIndex = meCtrl.getEditorBox().getChildren().size() - 1;
                    Holder holder = (Holder) meCtrl.getEditorBox().getChildren().get(holderIndex);
                    holder.addToHolder(capBlk.getBlock());

                    IfBlock ib = (IfBlock) meCtrl.getEditorBox().getChildren().get(holderIndex);

                    Element ifCondNode = (Element) block.getChildNodes().item(0);

                    if (ifCondNode != null) {
                        if (ifCondNode.getAttribute("id").equals("Condition")) {
                            Capability capabilityBlk = loadCondition(ifCondNode, meCtrl);

                            if (capabilityBlk != null) {
                                ib.addCondition(capabilityBlk.getBlock());
                            }
                        }
                    }

                    NodeList childNodes = block.getChildNodes();
                    loadIfBlockContent(ib, childNodes, meCtrl);

                }
            }
        }

    }

    private void loadCLBlock(ConditionBlock cb, NodeList childNodes, MainEditorController meCtrl) {

        if (childNodes.getLength() != 0) {
            Element conditionBlk = (Element) childNodes.item(0);
            Capability capBlk = loadCondition(conditionBlk, meCtrl);

            if (capBlk != null) {
                cb.addCondition(capBlk.getBlock());
            }

        }
    }

    private Capability loadCondition(Element block, MainEditorController meCtrl) {

        String capId = block.getElementsByTagName("CapId").item(0).getTextContent();

        System.out.println(" cap id" + capId);
        if (capId.equals("cap_def1") || capId.equals("cap_def2")) {
            String conValue = block.getElementsByTagName("ConValue").item(0).getTextContent();
            Capability capBlk = fileHandler.readFromCapabilityFile(capId);
            capBlk.getBlock().setTextField(conValue);
            capBlk.getBlock().disableTextField(false);
            return capBlk;

        } else {
            return checkActionBlock(block, meCtrl).getCapability();

        }

    }

    private Block checkActionBlock(Element block, MainEditorController meCtrl) {

        String deviceId = block.getElementsByTagName("DeviceId").item(0).getTextContent();
        int address = Integer.parseInt(block.getElementsByTagName("Location").item(0).getTextContent());
        String capId = block.getElementsByTagName("CapId").item(0).getTextContent();

        Device device = meCtrl.getConnectedDevice(deviceId, address);
        if (device != null) {
            return device.getCapabilityBlock(capId);
        }
        return null;

    }

    private void loadRepeatBlock(RepeatBlock rb, NodeList blockNodes, MainEditorController meCtrl) {

        for (int j = 0; j < blockNodes.getLength(); j++) {
            Element blk = (Element) blockNodes.item(j);
            System.out.println(" elements 999 - " + blockNodes.getLength());

            if (blk.getAttribute("id").equals("ConditionalBlock")) {

                Block capBlock = checkActionBlock(blk, meCtrl);
                if (capBlock != null) {
                    int childIndex = rb.getHolders().getChildren().size() - 1;
                    ((Holder) rb.getHolders().getChildren().get(childIndex)).addToHolder(capBlock);

                    NodeList childNodes = blk.getElementsByTagName("Condition");
                    if (childNodes.getLength() > 0) {
                        ConditionBlock cb = (ConditionBlock) rb.getHolders().getChildren().get(childIndex);
                        loadCLBlock(cb, childNodes, meCtrl);
                    }

                } else {
                    //highlight for not available
                }

            } else if (blk.getAttribute("id").equals("RepeatBlock")) {

                String repeatBlkId = "cap_def3";
                Capability capBlk = fileHandler.readFromCapabilityFile(repeatBlkId);
                int childIndex = rb.getHolders().getChildren().size() - 1;
                ((Holder) rb.getHolders().getChildren().get(childIndex)).addToHolder(capBlk.getBlock());
                RepeatBlock rbb = (RepeatBlock) rb.getHolders().getChildren().get(childIndex);

                Element repeatCondNode = (Element) blk.getChildNodes().item(0);

                if (repeatCondNode != null) {
                    if (repeatCondNode.getAttribute("id").equals("Condition")) {
                        Capability capabilityBlk = loadCondition(repeatCondNode, meCtrl);

                        if (capabilityBlk != null) {
                            rbb.addCondition(capabilityBlk.getBlock());
                        }
                    }
                }

                NodeList childNodes = blk.getChildNodes();
                loadRepeatBlock(rbb, childNodes, meCtrl);

            } else if (blk.getAttribute("id").equals("IfBlock")) {

                String ifBlkId = "cap_def4";
                Capability capBlk = fileHandler.readFromCapabilityFile(ifBlkId);
                int holderIndex = rb.getHolders().getChildren().size() - 1;
                Holder holder = (Holder) rb.getHolders().getChildren().get(holderIndex);
                holder.addToHolder(capBlk.getBlock());

                IfBlock ibb = (IfBlock) rb.getHolders().getChildren().get(holderIndex);

                Element ifCondNode = (Element) blk.getChildNodes().item(0);

                if (ifCondNode != null) {
                    if (ifCondNode.getAttribute("id").equals("Condition")) {
                        Capability capabilityBlk = loadCondition(ifCondNode, meCtrl);

                        if (capabilityBlk != null) {
                            ibb.addCondition(capabilityBlk.getBlock());
                        }
                    }
                }

                NodeList childNodes = blk.getChildNodes();
                loadIfBlockContent(ibb, childNodes, meCtrl);
            }
        }

    }

    private void loadIfBlockContent(IfBlock ib, NodeList blockNodes, MainEditorController meCtrl) {

        for (int j = 0; j < blockNodes.getLength(); j++) {
            Element blk = (Element) blockNodes.item(j);

            if (blk.getAttribute("id").equals("IF")) {
                NodeList ifChildNodes = blk.getChildNodes();
                loadIfBlock(ib, ifChildNodes, meCtrl);
            } else if (blk.getAttribute("id").equals("ELSE")) {
                NodeList elseChildNodes = blk.getChildNodes();
                loadElseBlock(ib, elseChildNodes, meCtrl);
            }
        }

    }

    private void loadIfBlock(IfBlock ib, NodeList ifChildNodes, MainEditorController meCtrl) {

        for (int j = 0; j < ifChildNodes.getLength(); j++) {
            Element blk = (Element) ifChildNodes.item(j);
            System.out.println(" elements 999 - " + ifChildNodes.getLength());

            System.out.println("nnnnnnnnnnn - ");
            if (blk.getAttribute("id").equals("ConditionalBlock")) {

                Block capBlock = checkActionBlock(blk, meCtrl);
                if (capBlock != null) {
                    int childIndex = ib.getIfHolders().getChildren().size() - 1;
                    Holder holder = (Holder) ib.getIfHolders().getChildren().get(childIndex);
                    holder.addToHolder(capBlock);

                    NodeList childNodes = blk.getElementsByTagName("Condition");
                    if (childNodes.getLength() > 0) {
                        ConditionBlock cb = (ConditionBlock) ib.getIfHolders().getChildren().get(childIndex);
                        loadCLBlock(cb, childNodes, meCtrl);
                    }

                } else {
                    //highlight for not available
                }

            } else if (blk.getAttribute("id").equals("RepeatBlock")) {

                String repeatBlkId = "cap_def3";
                Capability capBlk = fileHandler.readFromCapabilityFile(repeatBlkId);
                int childIndex = ib.getIfHolders().getChildren().size() - 1;
                Holder holder = (Holder) ib.getIfHolders().getChildren().get(childIndex);
                holder.addToHolder(capBlk.getBlock());
                // ((Holder) holder.getChildren().get(childIndex)).addToHolder(capBlk.getBlock());
                RepeatBlock rbb = (RepeatBlock) ib.getIfHolders().getChildren().get(childIndex);

                Element repeatCondNode = (Element) blk.getChildNodes().item(0);

                if (repeatCondNode != null) {
                    if (repeatCondNode.getAttribute("id").equals("Condition")) {
                        Capability capabilityBlk = loadCondition(repeatCondNode, meCtrl);

                        if (capabilityBlk != null) {
                            rbb.addCondition(capabilityBlk.getBlock());
                        }
                    }
                }

                NodeList childNodes = blk.getChildNodes();
                loadRepeatBlock(rbb, childNodes, meCtrl);

            } else if (blk.getAttribute("id").equals("IfBlock")) {

                String ifBlkId = "cap_def4";
                Capability capBlk = fileHandler.readFromCapabilityFile(ifBlkId);
                int holderIndex = ib.getIfHolders().getChildren().size() - 1;
                Holder holder = (Holder) ib.getIfHolders().getChildren().get(holderIndex);
                holder.addToHolder(capBlk.getBlock());

                IfBlock ibb = (IfBlock) ib.getIfHolders().getChildren().get(holderIndex);

                Element ifCondNode = (Element) blk.getChildNodes().item(0);

                if (ifCondNode != null) {
                    if (ifCondNode.getAttribute("id").equals("Condition")) {
                        Capability capabilityBlk = loadCondition(ifCondNode, meCtrl);

                        if (capabilityBlk != null) {
                            ibb.addCondition(capabilityBlk.getBlock());
                        }
                    }
                }

                NodeList childNodes = blk.getChildNodes();
                loadIfBlockContent(ibb, childNodes, meCtrl);
            }
        }

    }

    private void loadElseBlock(IfBlock ib, NodeList elseChildNodes, MainEditorController meCtrl) {

        for (int j = 0; j < elseChildNodes.getLength(); j++) {
            Element blk = (Element) elseChildNodes.item(j);
            //  System.out.println(" elements 999 - " + elseChildNodes.getLength());

            System.out.println("nnnnnnnnnnn - ");
            if (blk.getAttribute("id").equals("ConditionalBlock")) {

                Block capBlock = checkActionBlock(blk, meCtrl);
                if (capBlock != null) {
                    int childIndex = ib.getElseHolders().getChildren().size() - 1;
                    Holder holder = (Holder) ib.getElseHolders().getChildren().get(childIndex);
                    holder.addToHolder(capBlock);

                    NodeList childNodes = blk.getElementsByTagName("Condition");
                    if (childNodes.getLength() > 0) {
                        ConditionBlock cb = (ConditionBlock) ib.getElseHolders().getChildren().get(childIndex);
                        loadCLBlock(cb, childNodes, meCtrl);
                    }

                } else {
                    //highlight for not available
                }

            } else if (blk.getAttribute("id").equals("RepeatBlock")) {

                String repeatBlkId = "cap_def3";
                Capability capBlk = fileHandler.readFromCapabilityFile(repeatBlkId);
                int childIndex = ib.getElseHolders().getChildren().size() - 1;
                Holder holder = (Holder) ib.getElseHolders().getChildren().get(childIndex);
                holder.addToHolder(capBlk.getBlock());
                // ((Holder) holder.getChildren().get(childIndex)).addToHolder(capBlk.getBlock());
                RepeatBlock rbb = (RepeatBlock) ib.getElseHolders().getChildren().get(childIndex);

                Element repeatCondNode = (Element) blk.getChildNodes().item(0);

                if (repeatCondNode != null) {
                    if (repeatCondNode.getAttribute("id").equals("Condition")) {
                        Capability capabilityBlk = loadCondition(repeatCondNode, meCtrl);

                        if (capabilityBlk != null) {
                            rbb.addCondition(capabilityBlk.getBlock());
                        }
                    }
                }

                NodeList childNodes = blk.getChildNodes();
                loadRepeatBlock(rbb, childNodes, meCtrl);

            } else if (blk.getAttribute("id").equals("IfBlock")) {

                String ifBlkId = "cap_def4";
                Capability capBlk = fileHandler.readFromCapabilityFile(ifBlkId);
                int holderIndex = ib.getElseHolders().getChildren().size() - 1;
                Holder holder = (Holder) ib.getElseHolders().getChildren().get(holderIndex);
                holder.addToHolder(capBlk.getBlock());

                IfBlock ibb = (IfBlock) ib.getElseHolders().getChildren().get(holderIndex);

                Element ifCondNode = (Element) blk.getChildNodes().item(0);

                if (ifCondNode != null) {
                    if (ifCondNode.getAttribute("id").equals("Condition")) {
                        Capability capabilityBlk = loadCondition(ifCondNode, meCtrl);

                        if (capabilityBlk != null) {
                            ibb.addCondition(capabilityBlk.getBlock());
                        }
                    }
                }

                NodeList childNodes = blk.getChildNodes();
                loadIfBlockContent(ibb, childNodes, meCtrl);
            }
        }
    }
}
