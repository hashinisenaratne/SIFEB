/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.handle;

import com.sifeb.ve.Device;
import java.io.File;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Pubudu
 */
public class BlockCreator {

    FileHandler fileHandler;

    public BlockCreator() {
        fileHandler = new FileHandler();
    }

    // sends an integer value
    public void createBlock(String id) {
        String fileName = "dev_" + id + ".xml";
        Element devElement = fileHandler.readFromDeviceFile(new File(fileName));

        String devId = devElement.getElementsByTagName("Id").item(0).getTextContent();
        int nameLength = devElement.getElementsByTagName("Names").item(0).getChildNodes().getLength();

        for (int i = 0; i < nameLength; i++) {
            // add to map
        }
        String address = devElement.getElementsByTagName("Address").item(0).getTextContent();
        String type = devElement.getElementsByTagName("Type").item(0).getTextContent();
        String image = devElement.getElementsByTagName("Image").item(0).getTextContent();

        // create device
        //Device device =new Device
        //Device device = new Device("00001", "Wheels", 10, "actuator", "Mwheels.png");
        //  int capabilities = devElement.getElementsByTagName("Capabilities").item(0).getChildNodes().getLength();
        NodeList nodeList = devElement.getElementsByTagName("Capabilities").item(0).getChildNodes();

        for (int j = 0; j < nodeList.getLength(); j++) {
            String capId = nodeList.item(j).getTextContent();
            createCapability(capId);
        }

    }

    public void createCapability(String id) {

        String fileName = id + ".xml";
        Element devElement = fileHandler.readFromCapabilityFile(new File(fileName));

        String capId = devElement.getElementsByTagName("Id").item(0).getTextContent();
        int nameLength = devElement.getElementsByTagName("Names").item(0).getChildNodes().getLength();

        for (int i = 0; i < nameLength; i++) {
            // add to map
        }
        String command = devElement.getElementsByTagName("Command").item(0).getTextContent();
        String type = devElement.getElementsByTagName("Type").item(0).getTextContent();
        String image = devElement.getElementsByTagName("Image").item(0).getTextContent();
        
        //create capability

    }

}
