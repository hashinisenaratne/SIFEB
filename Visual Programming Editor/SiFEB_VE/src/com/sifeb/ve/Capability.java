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
    private final Map<Locale,String> capNames;
    private final Device device;
    private final String type;
    private final String command;
    private final String imageName;
    private final Image image;
    private final Block block;

    public Capability(String capID, Map<Locale,String> capNames, Device device, String type, String command,String imageName) {
        this.capID = capID;
        this.capNames = capNames;
        this.device = device;
        this.type = type;
        this.command = command;
        this.imageName = imageName;
        this.image = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/" + imageName));
        
        this.block = new Block(this);
    }

    public String getCapID() {
        return capID;
    }

    public String getCapName() {
        Locale currentLocale = Strings.getLocale();
        return capNames.get(currentLocale);
    }

    public Device getDevice() {
        return device;
    }

    public String getType() {
        return type;
    }

    public Image getImage() {
        return image;
    }

    public Block getBlock() {
        return block;
    }

    public String getCommand() {
        return command;
    }

    public Capability cloneCapability(){
        Capability cap = new Capability(this.capID, this.capNames, this.device, this.type, this.command,this.imageName);
        System.out.println(this.block.getParent().getEffect());
        if(this.device != null){
            this.device.addCapability(cap);
        }                
        
        return cap;
    }
    
    public void addToPane(Pane parent) {
        parent.getChildren().add(this.block);
    }
}
