/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sifeb.ve;

import com.sifeb.ve.resources.Strings;
import java.io.IOException;
import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Udith Arosha
 */
public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("SiFEB Visual Programming Editor");
        Strings.setLocale(new Locale("en", "US"));        

        initRootLayout();
        
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
//        primaryStage.setOnCloseRequest((WindowEvent event)->{
//            FeedBackLogger.timer.cancel();
//        });
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

}
