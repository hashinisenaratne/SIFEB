/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Pubudu
 */
public class Holder extends Pane {

    private final Image blockImg;
    private VBox vbox;
    // private Pane pane;

    public Holder(Image blkImg) {

        this.blockImg = blkImg;
        this.vbox = new VBox();
//        this.pane = new Pane();
//        this.pane.setPrefSize(90, 60);
//        this.pane.relocate(175, 15);
        this.vbox.relocate(29, 15);
        // this.pane.setStyle("-fx-background-color:red;");
        //super.getChildren().add(this.vbox);
        //System.out.print("dfffffff");
        //Image btnImg = new Image(getClass().getResourceAsStream("/com/sifeb/ve/images/MSPlay.png"));
        // parent.getChildren().add(new ActuatorBlock("rectangle", img, null));
        //  this.pane.getChildren().add(new ActuatorBlock("rectangle", img, null));

        super.setPrefSize(this.blockImg.getWidth(), this.blockImg.getHeight());
        super.setBackground(new Background(new BackgroundImage(this.blockImg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        super.getChildren().add(this.vbox);
    }

    public void addElementToVbox(Node node) {
        vbox.getChildren().add(node);

    }

    public VBox getVbox() {
        return vbox;
    }

    public void setVbox(VBox vbox) {
        this.vbox = vbox;
    }

}
