/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sifeb.ve.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

/**
 *
 * @author Pubudu
 */
public class GameEditorController extends MainEditorController  {
    
    @FXML
    Button prevBtn,nextBtn;
    @FXML
    ImageView gameImg;
    @FXML
    TextArea gameText;
    @FXML
    Label progressLabel;
    @FXML
    ProgressBar progressBar;
    
     @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        super.initialize(location, resources);
        prevBtn.setVisible(false);
        progressLabel.setText("work");
        gameText.setEditable(false);
        gameText.setText("flasklfjkasl fksj fkladj klaj fkljasdklfj \nadklsfjkladjfkljd fkl alfkj afkla jfklajl \nadklsfjkladjfkljd fkl alfkj afkla jfklajl \nadklsfjkladjfkljd fkl alfkj afkla jfklajl \nadklsfjkladjfkljd fkl alfkj afkla jfklajl \nadklsfjkladjfkljd fkl alfkj afkla jfklajl \nadklsfjkladjfkljd fkl alfkj afkla jfklajl \nadklsfjkladjfkljd fkl alfkj afkla jfklajl \nadklsfjkladjfkljd fkl alfkj afkla jfklajl \nadklsfjkladjfkljd fkl alfkj afkla jfklajl \nadklsfjkladjfkljd fkl alfkj afkla jfklajl");
    }
    
    ///public void add
    
}
