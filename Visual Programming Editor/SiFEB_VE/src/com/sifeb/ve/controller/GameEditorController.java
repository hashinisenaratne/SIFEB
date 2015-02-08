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
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Pubudu
 * @author Hashini Senaratne
 */
public class GameEditorController extends MainEditorController {

    @FXML
    Button prevBtn, nextBtn, learnBtn, challengeBtn;
    @FXML
    TextArea gameText;
    @FXML
    Label progressLabel;
    @FXML
    ProgressBar progressBar;
    @FXML
    StackPane stackPane1, stackPane2;
    NodeList nodeList;
    FileHandler fileHandler;
    int storyCount, totalStories;

    private static int TutorialLevel;
    private static String GameID;
    private static String GameFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        super.initialize(location, resources);
        fileHandler = new FileHandler();
        readGameFile(GameFile);
        storyCount = 1;
        totalStories = nodeList.getLength();
        setStartingConditions();
        setEventListeners();
        double initProg = (double) storyCount / (double) totalStories;
        progressBar.setProgress(initProg);
        learnBtn.setText("Learn - Level " + TutorialLevel);
        challengeBtn.setText("Challenge - " + GameID);

    }

    public Node getStory(int storyNumber) {
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
        String gameImgText = nodeList.item(storyCount - 1).getChildNodes().item(2).getTextContent();
        gameText.setText(storyText);
        gameText.setWrapText(true);
        gameImgText += ".png";
        imgText += ".png";
        Image topImage = new Image("file:" + SifebUtil.GAME_IMG_DIR + gameImgText);
        Image img = new Image("file:" + SifebUtil.GAME_IMG_DIR + imgText);
        stackPane1.setBackground(new Background(new BackgroundImage(topImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        stackPane2.setBackground(new Background(new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
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
                    //   stackPane1.setBackground(new Background(new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
                    stackPane2.setBackground(new Background(new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

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
                    // stackPane1.setBackground(new Background(new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
                    stackPane2.setBackground(new Background(new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

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
        try {
            FXMLLoader loader = new FXMLLoader();
            TutorialController.setLevel(RootController.getTutorialLevel());
            loader.setLocation(MainApp.class.getResource(MainApp.TutorialFile));
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
    private void goToChallenge(ActionEvent event) {
    }

    public static void setLevel(int level) {
        TutorialLevel = level;
    }

    public static void setGameFile(String gameFile) {
        GameFile = gameFile;
    }

    public static void setGameID(String gameID) {
        GameID = gameID;
    }
}
