/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import javafx.scene.image.Image;
import static java.awt.SystemColor.text;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author Pubudu
 */
public final class Block extends Pane {

  
    
    public Block(String type, Image img,Button button,Image btnImg) {

        button.setMaxHeight(img.getHeight());
        button.setMaxWidth(btnImg.getWidth());
        button.setMinHeight(img.getHeight());
        button.setMinWidth(btnImg.getWidth());
        button.setCursor(Cursor.HAND);
        
        setShape(type, img);
        super.getChildren().add(button);
     
    }

    public void setShape(String type, Image img) {
        switch (type) {
            case "rectangle":
                Rectangle r = new Rectangle(img.getWidth(), img.getHeight(), new ImagePattern(img));
                r.setArcHeight(20);
                r.setArcWidth(20);
                super.getChildren().add(r);
                break;
        }

    }

}
