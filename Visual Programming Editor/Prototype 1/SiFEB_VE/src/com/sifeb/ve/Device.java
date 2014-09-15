/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 *
 * @author Udith Arosha
 */
public class Device {

    private final String deviceID;
    private final String deviceName;
    private final int address;
    private final String type;
    private final Image image;
    private final ArrayList<Capability> capabilities;
    private final DeviceBlock deviceBlock;

    public Device(String deviceID, String deviceName, int address, String type, String imageName) {
        this.deviceID = deviceID;
        this.deviceName = deviceName;
        this.address = address;
        this.type = type;
        this.image = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/" + imageName));
        this.deviceBlock = new DeviceBlock(this);        
        this.capabilities = new ArrayList<>();
    }

    public String getDeviceID() {
        return deviceID;
    }

    public int getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public Image getImage() {
        return image;
    }

    public ArrayList<Capability> getCapabilities() {
        return capabilities;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public DeviceBlock getDeviceBlock() {
        return deviceBlock;
    }

    public void addToPane(Pane parent) {
        parent.getChildren().add(this.deviceBlock);
    }
    
    public void addCapability(Capability cap){
        this.capabilities.add(cap);
    }
}
