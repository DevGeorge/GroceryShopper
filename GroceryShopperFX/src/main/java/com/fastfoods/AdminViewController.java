package com.fastfoods;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminViewController implements Initializable {

    @FXML
    private Button customerViewButton;

    @FXML
    private Button accountEditorButton;

    @FXML
    private Button itemEditorButton;

    @FXML
    private Button viewOrdersButton;

    @FXML
    private Button closeButton;

    @FXML
    public void customerViewButtonOnAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("searchScreen.fxml"));
            Stage marketStage = new Stage();
            marketStage.setTitle("Market");
            marketStage.setScene(new Scene(root, 1300, 800));
            marketStage.show();
            closeStage();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void accountEditorOnAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("accountEditor.fxml"));
            Stage accEditorStage = new Stage();
            accEditorStage.setTitle("Account Editor");
            accEditorStage.setScene(new Scene(root, 800, 800));
            accEditorStage.show();
            closeStage();

        } catch (IOException exception) {
            exception.printStackTrace();

        }
    }

    @FXML
    public void itemEditorButtonOnAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("itemEditor.fxml"));
            Stage itemEditor = new Stage();
            itemEditor.setTitle("Item Editor");
            itemEditor.setScene(new Scene(root, 900, 800));
            itemEditor.show();
            closeStage();

        } catch (IOException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @FXML
    public void viewOrdersButtonOnAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("orderEditor.fxml"));
            Stage registerStage = new Stage();
            registerStage.setTitle("User Registration");
            registerStage.setScene(new Scene(root, 800, 650));
            registerStage.show();
        } catch (IOException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @FXML
    public void closeButtonOnAction(ActionEvent event) {
        closeStage();
    }

    public void closeStage() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}


