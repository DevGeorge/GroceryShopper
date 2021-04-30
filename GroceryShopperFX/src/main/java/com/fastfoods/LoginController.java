package com.fastfoods;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginController {
    @FXML
    private Button registerButton;
    @FXML
    private Label loginLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button closeButton;
    //Starts image
    // public void initialize(URL url, ResourceBundle resourceBundle){
    //    Image brandImage= new Image("");
    // }

    @FXML
    void enterButtonOnAction(ActionEvent event) {
        loginButtonOnAction(event);

    }

    @FXML
    public void loginButtonOnAction(ActionEvent event) {

        if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
            validateLogin();
        } else {
            loginLabel.setText("Please enter Email and Password");
        }

    }

    public void closeButtonOnAction(ActionEvent event) {
        closeStage();
    }

    public void closeStage() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void validateLogin() {
        DatabaseController connection = new DatabaseController();
        Connection connectDB = connection.getConnection();
        String verifyLogin = "SELECT count(1) FROM groceryshopper.accounts WHERE email = '" + usernameField.getText() + "' AND password = '" + passwordField.getText() + "'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(verifyLogin);
            while (resultSet.next()) {
                if (resultSet.getInt(1) == 1) {
                    loginLabel.setText("Verified");
                    if (isAdmin()) {
                        loginLabel.setText("Please Wait.");
                        Parent root = FXMLLoader.load(getClass().getResource("adminView.fxml"));
                        Stage primaryStage = new Stage();
                        primaryStage.setTitle("Admin Page");
                        primaryStage.setScene(new Scene(root, 600, 400));
                        primaryStage.show();
                        connectDB.close();
                        closeStage();

                    } else {
                        loginLabel.setText("Please Wait.");
                        Parent root = FXMLLoader.load(getClass().getResource("searchScreen.fxml"));
                        Stage primaryStage = new Stage();
                        primaryStage.setTitle("Customer Page");
                        primaryStage.setScene(new Scene(root, 1200, 700));
                        primaryStage.show();
                        closeStage();
                    }
                    return;
                } else {
                    loginLabel.setText("Invalid Login, Please try again");
                }
            }

        } catch (Exception e) {
            e.getStackTrace();
            e.getCause();
        }
    }
    public void setAccount(ResultSet result) throws SQLException{
        App.LOGGEDINACCOUNT = new Accounts(result.getInt("account_number"), result.getString("first_name"), result.getString("last_name"),
                result.getString("street_number"), result.getString("city"), result.getString("postal_code"),
                result.getString("email"), result.getInt("account_type"));
    }

    public boolean isAdmin() {
        boolean isAdmin = false;
        int i = 1;
        Statement statement = null;
        ResultSet resultSet = null;
        DatabaseController connection = new DatabaseController();
        Connection connectDB = connection.getConnection();
        String sql = "SELECT * FROM groceryshopper.accounts where email ='" + usernameField.getText() + "' AND password = '" + passwordField.getText() + "'";
        try {
            statement = connectDB.createStatement();
            resultSet = statement.executeQuery(sql);
            resultSet.next();
            i = resultSet.getInt("account_type");
            setAccount(resultSet);
            connectDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (i == 2) {
            isAdmin = true;
        }
        return isAdmin;
    }

    public void registerButtonOnAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("registerScreen.fxml"));
            Stage registerStage = new Stage();
            registerStage.setTitle("User Registration");
            registerStage.setScene(new Scene(root, 600, 800));
            registerStage.show();
        } catch (Exception e) {
            e.getStackTrace();
            e.getCause();
        }
    }


}
