/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import com.sifeb.ve.resources.Strings;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 *
 * @author Udith Arosha
 */
public class Device {

    // Device Type Definitions /////////////////////////
    public static final String DEV_ACTUATOR = "actuator";
    public static final String DEV_SENSOR = "sensor";
    ////////////////////////////////////////////////////

    private final String deviceID;
    private final Map<Locale, String> deviceNames;
    private final int address;
    private final String type;
    private final Image image;
    private final ArrayList<Capability> capabilities;
    private final DeviceBlock deviceBlock;

    public Device(String deviceID, Map<Locale, String> deviceNames, int address, String type, String imageName) {
        this.deviceID = deviceID;
        this.deviceNames = deviceNames;
        this.address = address;
        this.type = type;
        this.image = new Image("/com/sifeb/ve/images/devices/" + imageName + ".png");
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
        Locale currentLocale = Strings.getLocale();
        return deviceNames.get(currentLocale);
    }

    public DeviceBlock getDeviceBlock() {
        return deviceBlock;
    }

    public void addToPane(Pane parent) {
        parent.getChildren().add(this.deviceBlock);
    }

    public void addCapability(Capability cap) {
        this.capabilities.add(cap);
    }
}
