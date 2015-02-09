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
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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

    public void addDefaultCapabilities() {
        String[] defaultCaps = {"cap_def1", "cap_def2"};
        for (String capId : defaultCaps) {
            Capability cap = fileHandler.readFromCapabilityFile(capId);
            mainEditor.addCapabilityBlock(cap);
        }
    }

    public void addLevel2Capabilities() {
        String[] level2Caps = {"cap_def3"};
        for (String capId : level2Caps) {
            Capability cap = fileHandler.readFromCapabilityFile(capId);
            mainEditor.addCapabilityBlock(cap);
        }
    }

    public void addLevel3Capabilities() {
        String[] level3Caps = {"cap_def3", "cap_def4"};
        for (String capId : level3Caps) {
            Capability cap = fileHandler.readFromCapabilityFile(capId);
            mainEditor.addCapabilityBlock(cap);
        }
    }

    // sends an integer value
    public void createDeviceBlock(String id, String address) {
        String fileName = "dev_" + id;
        Device device = fileHandler.readFromDeviceFile(fileName, address);
        mainEditor.addDeviceBlock(device);

        for (Capability cap : device.getCapabilities()) {
            mainEditor.addCapabilityBlock(cap);
        }

    }

//    public void createCapabilityBlock(Capability capability) {        
//        mainEditor.addCapabilityBlock(cap, device);
//    }
    public void removeBlock(String address) {
        int addr = Integer.parseInt(address);
        mainEditor.removeBlocks(addr);
    }

    public void processMessage() {

        while (messageQueue.size() > 0) {

            String[] readValue = messageQueue.get(0).split(",");

            System.out.println("process Msg - " + readValue);

            char command = readValue[0].charAt(0);

            //  String address = Integer.toString((int) readValue.charAt(1));
            // String type = Integer.toString((int) readValue.charAt(2));
            // System.out.println("address is - " + address);
            //       System.out.println("command - " + command + " add - " + address);
            switch (command) {
                case 'c':
                    String address = readValue[1];
                    String type = readValue[2];
                    if (!mainEditor.checkDeviceAddress(address)) {
                        this.createDeviceBlock(type, address);
                    }
                    break;
                case 'd':
                    String disAddress = readValue[1];
                    this.removeBlock(disAddress);
                    break;
                case 'h':
//                    int tmpVl = Integer.parseInt(address);
//                    System.out.println("tmpVl - " + tmpVl);
//                    this.mainEditor.hValue = tmpVl;

                    break;
            }

            messageQueue.remove(0);
        }

    }

}
