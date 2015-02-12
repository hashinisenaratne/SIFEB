/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import com.sifeb.ve.resources.Strings;
import com.sifeb.ve.resources.SifebUtil;
import java.util.Locale;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 *
 * @author Udith Arosha
 */
public class Capability {

    // Capability Type Definitions /////////////////////////
    public static final String CAP_ACTION = "action";
    public static final String CAP_ACTION_C = "actionC";
    public static final String CAP_SENSE = "sense";
    public static final String CAP_CONDITION = "condition";
    public static final String CAP_CONTROL = "control";
    public static final String CAP_IFELSE = "ifelse";
    ///////////////////////////////////////////////////////

    private final String capID;
    private final Map<Locale, String> capNames;
    private Device device;
    private final String type;
    private final String testCommand;
    private final String exeCommand;
    private final String stopCommand;
    private final String compType;
    private final String respSize;
    private final String refValue;
    private final String imageName;
    private Image staticImage;
    private Image dynamicImage;
    private final Block block;
    private final boolean hasTest;

    public Capability(String capID,
            Map<Locale, String> capNames,
            Device device,
            String type,
            String testCmd,
            String exeCommand,
            String stopCommand,
            String compType,
            String respSize,
            String refValue, String imageName, boolean hasTest) {

        this.capID = capID;
        this.capNames = capNames;
        this.device = device;
        this.type = type;
        this.testCommand = testCmd;
        this.exeCommand = exeCommand;
        this.stopCommand = stopCommand;
        this.compType = compType;
        this.respSize = respSize;
        this.refValue = refValue;
        this.imageName = imageName;

        this.staticImage = new Image("file:" + SifebUtil.STATIC_IMG_DIR + imageName + ".png");

        this.dynamicImage = new Image("file:" + SifebUtil.DYNAMIC_IMG_DIR + imageName + ".gif");
        if (dynamicImage.getWidth() == 0) {
            this.dynamicImage = this.staticImage;
        }

        this.block = new Block(this);
        this.hasTest = hasTest;
    }

    // returns the capability id
    public String getCapID() {
        return capID;
    }

    // returns the capability name 
    public String getCapName() {
        Locale currentLocale = Strings.getLocale();
        return capNames.get(currentLocale);
    }

    // returns the capability name based on the localization
    public String getCapName(Locale locale) {
        return capNames.get(locale);
    }

    // returns the device in the capability
    public Device getDevice() {
        return device;
    }

    // returns the capability type
    public String getType() {
        return type;
    }

    public Image getStaticImage() {
        return staticImage;
    }

    public Image getDynamicImage() {
        return dynamicImage;
    }

    public Block getBlock() {
        return block;
    }

    public String getTestCommand() {
        return testCommand;
    }

    public boolean isHasTest() {
        return hasTest;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getExeCommand() {
        return exeCommand;
    }

    public String getStopCommand() {
        return stopCommand;
    }

    public String getCompType() {
        return compType;
    }

    public String getRespSize() {
        return respSize;
    }

    public String getRefValue() {
        return refValue;
    }

    public String getImageName() {
        return imageName;
    }

    public Map<Locale, String> getCapNames() {
        return capNames;
    }

    // returns a cloned capability block
    public Capability cloneCapability() {
        Capability cap = new Capability(this.capID, this.capNames, this.device, this.type, this.testCommand, this.exeCommand, this.stopCommand, this.compType, this.respSize, this.refValue, this.imageName, true);
        if (this.device != null) {
            this.device.addCapability(cap);
        }

        return cap;
    }

    public void addToPane(Pane parent) {
        parent.getChildren().add(this.block);
    }

    @Override
    public String toString() {
        return this.getCapName(Locale.US) + " (" + this.getCapID() + ")";
    }

}
