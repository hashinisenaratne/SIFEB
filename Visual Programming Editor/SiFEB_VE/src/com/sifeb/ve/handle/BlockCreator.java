/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.handle;

import com.sifeb.ve.Capability;
import com.sifeb.ve.Device;
import com.sifeb.ve.controller.MainEditorController;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javafx.scene.layout.VBox;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Pubudu
 */
public class BlockCreator {

    FileHandler fileHandler;
    MainEditorController mainEditor;
    VBox deviceVbox;
    VBox capabilityVbox;

    public BlockCreator(MainEditorController mainEditor) {
        fileHandler = new FileHandler();
        this.mainEditor = mainEditor;
        this.deviceVbox = this.mainEditor.getDeviceVbox();
        this.capabilityVbox = this.mainEditor.getCapabilityVbox();
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
        mainEditor.addDeviceBlock(deviceVbox, device);

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
        mainEditor.addCapabilityBlock(capabilityVbox, cap, device, hasTestButton);

        System.out.println("capability added");

    }

}
