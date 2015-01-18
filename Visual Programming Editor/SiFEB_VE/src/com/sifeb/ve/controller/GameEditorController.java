/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve.controller;

import com.sifeb.ve.MainApp;
import com.sifeb.ve.handle.FileHandler;
import com.sifeb.ve.resources.SifebUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Pubudu
 */
public class GameEditorController extends MainEditorController {

    @FXML
    Button prevBtn, nextBtn;
    @FXML
    ImageView gameImg;
    @FXML
    TextArea gameText;
    @FXML
    Label progressLabel;
    @FXML
    ProgressBar progressBar;
    NodeList nodeList;
    FileHandler fileHandler;
    int storyCount, totalStories;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        super.initialize(location, resources);
        fileHandler = new FileHandler();
        readGameFile("game_001");
        storyCount = 1;
        totalStories = nodeList.getLength();
        setStartingConditions();
        setEventListeners();
        double initProg = (double) storyCount / (double) totalStories;
        progressBar.setProgress(initProg);

    }

    public Node getStory(int storyNumber) {
        //  System.out.println(nodeList.item(3).getChildNodes().item(1).getTextContent());
        //  System.out.println(nodeList.item(3).getChildNodes().item(0).getTextContent());
        return (Node) nodeList.item(storyNumber);
    }

    public void setProgressBar(int num) {
        double progress = (double) num / (double) totalStories;
        progressBar.setProgress(progress);
    }

    public void readGameFile(String gameName) {
        Element element = fileHandler.readFromGameFile(gameName);
        nodeList = element.getElementsByTagName("Stories").item(0).getChildNodes();
    }

    public void setStartingConditions() {
        prevBtn.setDisable(true);
        String imgText = nodeList.item(storyCount - 1).getChildNodes().item(0).getTextContent();
        String storyText = nodeList.item(storyCount - 1).getChildNodes().item(1).getTextContent();
        gameText.setText(storyText);
        gameText.setWrapText(true);
        imgText += ".png";
        Image img = new Image("file:" + SifebUtil.GAME_IMG_DIR + imgText);
        gameImg.setImage(img);
        gameText.setEditable(false);

    }

    public void setEventListeners() {
        prevBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                if (storyCount > 0) {

                    if (nextBtn.isDisable()) {
                        nextBtn.setDisable(false);
                    }
                    storyCount--;
                    String imgText = nodeList.item(storyCount - 1).getChildNodes().item(0).getTextContent();
                    String storyText = nodeList.item(storyCount - 1).getChildNodes().item(1).getTextContent();
                    gameText.setText(storyText);
                    imgText += ".png";
                    Image img = new Image("file:" + SifebUtil.GAME_IMG_DIR + imgText);
                    gameImg.setImage(img);
                    setProgressBar(storyCount);

                    if (storyCount - 1 == 0) {
                        prevBtn.setDisable(true);
                    }

                }

            }
        });

        nextBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                if (storyCount < totalStories + 1) {

                    if (prevBtn.isDisable()) {
                        prevBtn.setDisable(false);
                    }

                    storyCount++;
                    String imgText = nodeList.item(storyCount - 1).getChildNodes().item(0).getTextContent();
                    String storyText = nodeList.item(storyCount - 1).getChildNodes().item(1).getTextContent();
                    gameText.setText(storyText);
                    imgText += ".png";
                    Image img = new Image("file:" + SifebUtil.GAME_IMG_DIR + imgText);
                    gameImg.setImage(img);
                    setProgressBar(storyCount);

                    if (storyCount == totalStories) {
                        nextBtn.setDisable(true);
                    }
                }

            }
        });
    }

    @FXML
    private void goToHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(MainApp.HomeFile));
            MainApp.setPane((Pane) loader.load());
            Scene scene = new Scene(MainApp.getPane());
            MainApp.getStage().setScene(scene);
            MainApp.getStage().setMaximized(false);
            MainApp.getStage().setResizable(false);
            MainApp.getStage().setWidth(MainApp.InitialScreenWidth);
            MainApp.getStage().setHeight(MainApp.InitialScreenHeight);
            MainApp.getStage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToLearn(ActionEvent event) {

    }

    @FXML
    private void goToChallenge(ActionEvent event) {

    }
}
