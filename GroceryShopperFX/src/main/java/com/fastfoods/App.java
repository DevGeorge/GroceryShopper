package com.fastfoods;
//CODED BY GEORGE AL-HADDAD

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */

public class App extends Application {
    public static Accounts LOGGEDINACCOUNT;
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("loginScreen.fxml"));
        primaryStage.setTitle("Grocery Shopper");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

}