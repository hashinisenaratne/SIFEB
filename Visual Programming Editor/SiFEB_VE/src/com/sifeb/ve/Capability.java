/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import com.sifeb.ve.resources.Strings;
import java.util.Locale;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 *
 * @author Udith Arosha
 */
public class Capability {

    private final String capID;
    private final Map<Locale, String> capNames;
    private Device device;
    private final String type;
    private final String command;
    private final String imageName;
    private final Image staticImage;
    private Image dynamicImage;
    private final Block block;
    private final boolean hasTest;

    public Capability(String capID, Map<Locale, String> capNames, Device device, String type, String command, String imageName, boolean hasTest) {
        this.capID = capID;
        this.capNames = capNames;
        this.device = device;
        this.type = type;
        this.command = command;
        this.imageName = imageName;
        this.staticImage = new Image("/com/sifeb/ve/images/static/" + imageName + ".png");
        try {
            this.dynamicImage = new Image("/com/sifeb/ve/images/dynamic/" + imageName + ".gif");
        } catch (IllegalArgumentException ex) {
            this.dynamicImage = new Image("/com/sifeb/ve/images/static/" + imageName + ".png");
        }

        this.block = new Block(this);
        this.hasTest = hasTest;
    }

    public String getCapID() {
        return capID;
    }

    public String getCapName() {
        Locale currentLocale = Strings.getLocale();
        return capNames.get(currentLocale);
    }
    
    public String getCapName(Locale locale){
        return capNames.get(locale);
    }

    public Device getDevice() {
        return device;
    }

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

    public String getCommand() {
        return command;
    }

    public boolean isHasTest() {
        return hasTest;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
    

    public Capability cloneCapability() {
        Capability cap = new Capability(this.capID, this.capNames, this.device, this.type, this.command, this.imageName, true);
        System.out.println(this.block.getParent().getEffect());
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
        return this.getCapName() + " (" + this.getCapID() + ")";
    }

}
