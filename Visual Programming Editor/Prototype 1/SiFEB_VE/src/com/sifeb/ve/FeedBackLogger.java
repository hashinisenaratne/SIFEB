/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 *
 * @author Udith Arosha
 */
public class FeedBackLogger {

    private static Label fbText;
    private static ImageView fbFace;
    private static final Image happyFace = new Image(FeedBackLogger.class.getResourceAsStream("/com/sifeb/ve/images/happy.png"));
    private static final Image sadFace = new Image(FeedBackLogger.class.getResourceAsStream("/com/sifeb/ve/images/sad.png"));
    private static final Duration duration = Duration.millis(3000);

    public static void setControls(ImageView fbFace, Label fbText) {
        FeedBackLogger.fbText = fbText;
        FeedBackLogger.fbFace = fbFace;        
    }
    
    public static void sendGoodMessage(String message){
        fbFace.setImage(happyFace);
        sendMessage(message);
    }
    
    public static void sendBadMessage(String message){
        fbFace.setImage(sadFace);
        sendMessage(message);
    }
    
    private static void sendMessage(String text){
        String content = text;
        Animation animation = new Transition() {
            {
                setCycleDuration(duration);
            }

            @Override
            protected void interpolate(double frac) {
                final int length = content.length();
                final int n = Math.round(length * (float) frac);
                fbText.setText(content.substring(0, n));
            }

        };        
        animation.play();
    }

}
