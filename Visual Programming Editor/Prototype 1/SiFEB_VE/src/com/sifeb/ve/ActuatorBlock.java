/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author Udith Arosha
 */
public class ActuatorBlock extends StackPane {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 50;

    public ActuatorBlock(String text) {
        Rectangle r = new Rectangle(WIDTH, HEIGHT, new Color(Math.random(), Math.random(), Math.random(), 1));
        r.setArcHeight(20);
        r.setArcWidth(20);
        
        Text t = new Text(text);
        
        super.getChildren().add(r);
        super.getChildren().add(t);        
        super.setCursor(Cursor.HAND);
    }
}
