/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import com.sifeb.ve.controller.ComPortController;
import com.sifeb.ve.resources.Strings;
import java.io.IOException;
import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Udith Arosha
 * @author Hashini Senaratne
 */
public class MainApp extends Application {

    private static Stage primaryStage;
    private static Pane rootLayout;
    public static String RootID = "RootLayout";
    public static String RootFile = "view/RootLayout.fxml";
    public static String HomeID = "HomeLayout";
    public static String HomeFile = "view/HomeLayout.fxml";
    public static String TutorialID = "TutorialLayout";
    public static String TutorialFile = "view/TutorialLayout.fxml";
    public static String LibraryID = "LibraryEditor";
    public static String LibraryFile = "view/LibraryEditor.fxml";
    public static int InitialScreenWidth = 800;
    public static int InitialScreenHeight = 600;

    @Override
    public void start(Stage primaryStage) {

        MainApp.primaryStage = primaryStage;
        MainApp.primaryStage.setTitle("SiFEB Visual Programming Editor");
        Strings.setLocale(new Locale("en", "US"));

        MainApp.primaryStage.setOnCloseRequest((WindowEvent event) -> {
        });
        MainApp.primaryStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/com/sifeb/ve/images/appIcon.png")));

        initRootLayout();

        Scene scene = new Scene(rootLayout);
        MainApp.primaryStage.setScene(scene);
        MainApp.primaryStage.setMaximized(false);
        MainApp.primaryStage.setResizable(false);
        MainApp.primaryStage.setWidth(800);
        MainApp.primaryStage.setHeight(600);
        MainApp.primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/HomeLayout.fxml"));
            rootLayout = (Pane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage getStage() {
        return primaryStage;
    }

    public static void setPane(Pane pane) {
        rootLayout = pane;
    }

    public static Pane getPane() {
        return rootLayout;
    }

}
