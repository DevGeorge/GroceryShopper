package com.fastfoods;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;

public class RegisterController {
    @FXML
    private Button closeButton;
    @FXML
    private Button confirmButton;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private PasswordField setPasswordField;
    @FXML
    private PasswordField confirmPasswordField;

    public void closeButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void confirmButtonOnAction(ActionEvent event) {
        confirmButton.getScene();
        statusLabel.setText("");
        if (ifValidFields()) {
            registerUser();
            statusLabel.setText("You have been registered");

        }
    }

    public void registerUser() {
        DatabaseController connection = new DatabaseController();
        Connection connectDatabase = connection.getConnection();
        String registerUserSql = "INSERT INTO groceryshopper.accounts (`first_name`, `last_name`, `street_number`, `city`, `postal_code`, `email`, `password`, `account_type`) VALUES ('" +
                firstNameField.getText() + "','" + lastNameField.getText() + "','" + addressField.getText() + "','" + cityField.getText() + "','" + postalCodeField.getText() +
                "','" + emailField.getText() + "','" + setPasswordField.getText() + "',default)";


        try {
            Statement statement = connectDatabase.createStatement();
            statement.executeQuery(registerUserSql);

        } catch (Exception e) {
            e.getStackTrace();
            e.getCause();
        }
        statusLabel.setText("You have been registered");

    }

    public boolean ifValidFields() {
        DatabaseController connection = new DatabaseController();
        Connection connectDatabase = connection.getConnection();
        String checkDuplicateEmail;
        String postalCode = postalCodeField.getText();
        if (!setPasswordField.getText().equals(confirmPasswordField.getText())) {
            statusLabel.setText("Passwords do not Match");
            System.out.println("password field");
            return false;
        }
        //if any fields are blank.
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || addressField.getText().isEmpty() || cityField.getText().isEmpty() || postalCodeField.getText().isEmpty() || emailField.getText().isEmpty() || setPasswordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty()) {
            statusLabel.setText("Please fill in all the fields");
            return false;
        }
        if (postalCode.length() != 6 || !isLetter(postalCode.charAt(0)) || !isLetter(postalCode.charAt(2)) || !isLetter(postalCode.charAt(4)) || !isDigit(postalCode.charAt(1)) || !isDigit(postalCode.charAt(3)) || !isDigit(postalCode.charAt(5))) {
            statusLabel.setText("Invalid Postal code, Try Again.");
            return false;
        }
        try {
            Statement statement = connectDatabase.createStatement();
            checkDuplicateEmail = "SELECT count(1) FROM groceryshopper.accounts WHERE email = " + emailField.getText().replaceAll("\\s", "") + "'";
            ResultSet queryResult = statement.executeQuery(checkDuplicateEmail);
            if (queryResult.getInt(1) == 1) {
                statusLabel.setText("Email has already been registered.");
                return false;
            }
            connectDatabase.close();
        } catch (Exception e) {
            e.getStackTrace();
            e.getCause();
        }

        if (!isValidEmailAddress(emailField.getText())) {
            statusLabel.setText("Email is invalid.");
            return false;
        }
        return true;
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

}

