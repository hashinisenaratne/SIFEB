/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.handle;

import com.sifeb.ve.Capability;
import com.sifeb.ve.Device;
import com.sifeb.ve.controller.MainEditorController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Pubudu
 */
public class BlockCreator {

    FileHandler fileHandler;
    public MainEditorController mainEditor;
    ArrayList<String> messageQueue;
    ObservableList<String> observableList;

    public BlockCreator(MainEditorController mainEditor) {
        fileHandler = new FileHandler();
        this.mainEditor = mainEditor;
        messageQueue = new ArrayList<String>();
        observableList = FXCollections.observableList(messageQueue);

        observableList.addListener(new ListChangeListener() {

            @Override
            public void onChanged(ListChangeListener.Change c) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        processMessage();
                        System.out.println("event changed");
                        System.out.println(messageQueue.size());
                    }
                });

            }
        });

    }

    public void addMessagetoQueue(String message) {
        observableList.add(message);
    }

    // sends an integer value
    public void createBlock(String id) {
        String fileName = "dev_" + id;
        Element devElement = fileHandler.readFromDeviceFile(fileName);

        String devId = devElement.getElementsByTagName("Id").item(0).getTextContent();
        NodeList nameList = devElement.getElementsByTagName("Names").item(0).getChildNodes();
        Map<Locale, String> devNames = new HashMap<>();
        Locale[] localeList = {new Locale("en", "US"), new Locale("si", "LK")};

        for (int i = 0; i < nameList.getLength(); i++) {
            devNames.put(localeList[i], nameList.item(i).getTextContent());
        }

        String address = devElement.getElementsByTagName("Address").item(0).getTextContent();
        String type = devElement.getElementsByTagName("Type").item(0).getTextContent();
        String image = devElement.getElementsByTagName("Image").item(0).getTextContent();

        // create device
        Device device = new Device(devId, devNames, Integer.parseInt(address), type, image);
        mainEditor.addDeviceBlock(device);

        //Device device = new Device("00001", "Wheels", 10, "actuator", "Mwheels.png");
        NodeList capList = devElement.getElementsByTagName("Capabilities").item(0).getChildNodes();

        for (int j = 0; j < capList.getLength(); j++) {
            String capId = capList.item(j).getTextContent();
            createCapability(capId, device);
        }

    }

    public void createCapability(String id, Device device) {

        Element devElement = fileHandler.readFromCapabilityFile(id);

        String capId = devElement.getElementsByTagName("Id").item(0).getTextContent();
        NodeList nodeList = devElement.getElementsByTagName("Names").item(0).getChildNodes();
        Map<Locale, String> actNames = new HashMap<>();
        Locale[] localeList = {new Locale("en", "US"), new Locale("si", "LK")};

        for (int i = 0; i < nodeList.getLength(); i++) {
            actNames.put(localeList[i], nodeList.item(i).getTextContent());
        }

        String command = devElement.getElementsByTagName("Command").item(0).getTextContent();
        String type = devElement.getElementsByTagName("Type").item(0).getTextContent();
        String image = devElement.getElementsByTagName("Image").item(0).getTextContent();
        boolean hasTestButton = Boolean.parseBoolean(devElement.getElementsByTagName("HasTestButton").item(0).getTextContent());
        Capability cap = new Capability(capId, actNames, device, type, command, image);
        mainEditor.addCapabilityBlock(cap, device, hasTestButton);

        System.out.println("capability added");

    }

    public void removeBlock(String address) {
        int addr = Integer.parseInt(address);
        mainEditor.removeBlocks(addr);

    }

    public void processMessage() {

        while (messageQueue.size() > 0) {

            String readValue = messageQueue.get(0);

            System.out.println("process Msg - " + readValue);
            if (readValue.contains("#")) {
                char r = readValue.charAt(0);
//                if (r == '0') {
//                    mainEditor.ackReceived = true;
//                } else {
//                    Dialog dlg = new Dialog(null, Strings.getString("message.fromsifeb"));
//                    dlg.setResizable(false);
//                    dlg.setIconifiable(false);
//                    dlg.setMasthead("Something went wrong!!!");
//                    dlg.setContent("I2C returned status " + r);
//                    dlg.getActions().add(Dialog.Actions.CLOSE);
//                }
            } else {
                //  System.out.println(readValue);
                char command = readValue.charAt(0);

                String address = Integer.toString((int) readValue.charAt(1));
                System.out.println("address is - "+address);

                //       System.out.println("command - " + command + " add - " + address);
                switch (command) {
                    case 'c':
                        this.createBlock(address);
                        break;
                    case 'd':
                        this.removeBlock(address);
                        break;
                    case 'h':
                        int tmpVl = Integer.parseInt(address);
                        System.out.println("tmpVl - " + tmpVl);
                        this.mainEditor.hValue = tmpVl;

                        break;
                }

            }
            messageQueue.remove(0);
        }

    }

}
