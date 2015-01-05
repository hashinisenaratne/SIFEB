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
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Udith Arosha
 * @author Hashini Senaratne
 */
public class MainApp extends Application {

    private static Stage primaryStage;
    private BorderPane rootLayout;
    public static String screen0ID = "RootLayout";
    public static String screen0File = "view/RootLayout.fxml";
    public static String screen1ID = "main";
    public static String screen1File = "view/Screen1.fxml";
    public static String screen2ID = "screen2";
    public static String screen2File = "view/Screen2.fxml";
    public static String screen3ID = "screen3";
    public static String screen3File = "view/Screen3.fxml";

    @Override
    public void start(Stage primaryStage) {

        MainApp.primaryStage = primaryStage;
        MainApp.primaryStage.setTitle("SiFEB Visual Programming Editor");
        Strings.setLocale(new Locale("en", "US"));        

        primaryStage.setOnCloseRequest((WindowEvent event)->{
            ComPortController.statusQuery.cancel();
            ComPortController.timer.cancel();
        });
        primaryStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/com/sifeb/ve/images/appIcon.png" ))); 

//        //initRootLayout();
//        
//        Scene scene = new Scene(rootLayout);
//        primaryStage.setScene(scene);
//        primaryStage.setMaximized(true);
//        primaryStage.show();
        
        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(screen0ID, screen0File);
        mainContainer.loadScreen(screen1ID, screen1File);
        mainContainer.loadScreen(screen2ID, screen2File);
        mainContainer.loadScreen(screen3ID, screen3File);
        
        mainContainer.setScreen(screen1ID);
        
        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
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
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
    
    public static Stage getStage()
    {
        return primaryStage;
    }
    
}
