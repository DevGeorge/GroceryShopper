package com.fastfoods;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;

public class AccountEditorController implements Initializable {

    ToggleGroup radioGroup = new ToggleGroup();
    ObservableList<Accounts> accountsList;
    ObservableList<Accounts> searchedList;
    int i;
    @FXML
    private TextField searchField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField streetNumberField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField postalField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TableView<Accounts> accountTable;
    @FXML
    private TableColumn<Accounts, Integer> numberCol;
    @FXML
    private TableColumn<Accounts, String> firstCol;
    @FXML
    private TableColumn<Accounts, String> lastCol;
    @FXML
    private TableColumn<Accounts, String> streetCol;
    @FXML
    private TableColumn<Accounts, String> cityCol;
    @FXML
    private TableColumn<Accounts, String> postalCol;
    @FXML
    private TableColumn<Accounts, String> emailCol;
    @FXML
    private TableColumn<Accounts, Integer> typeCol;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button adminPanelButton;
    @FXML
    private Button updateButton;
    @FXML
    private RadioButton customerRadio;
    @FXML
    private RadioButton adminRadio;
    @FXML
    private Label statusLabel;
    @FXML
    private Label idLabel;

    @FXML
    public void updateTable() {
        numberCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        streetCol.setCellValueFactory(new PropertyValueFactory<>("streetNumber"));
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        postalCol.setCellValueFactory(new PropertyValueFactory<>("postal"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("account_type"));

        accountsList = getAccounts();
        accountTable.setItems(accountsList);
    }

    private ObservableList<Accounts> getAccounts() {
        DatabaseController connection = new DatabaseController();
        Connection connectDatabase = connection.getConnection();
        Statement statement;
        ObservableList<Accounts> list = FXCollections.observableArrayList();
        try {
            statement = connectDatabase.createStatement();
            String sqlStatement = "Select * From groceryshopper.accounts";
            ResultSet result;
            result = statement.executeQuery(sqlStatement);
            while (result.next()) {
                list.add(new Accounts(result.getInt("account_number"), result.getString("first_name"), result.getString("last_name"),
                        result.getString("street_number"), result.getString("city"), result.getString("postal_code"),
                        result.getString("email"), result.getInt("account_type")));
            }
            connectDatabase.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    @FXML
    public void getAccount() {
        i = accountTable.getSelectionModel().getSelectedIndex();
        if (i > -1) {
            int temp = typeCol.getCellData(i);
            firstNameField.setText(firstCol.getCellData(i));
            lastNameField.setText(lastCol.getCellData(i));
            streetNumberField.setText(streetCol.getCellData(i));
            cityField.setText(cityCol.getCellData(i));
            emailField.setText(emailCol.getCellData(i));
            postalField.setText(postalCol.getCellData(i));
            idLabel.setText(numberCol.getCellData(i).toString());
            passwordField.setText("");
            if (temp == 1) {
                customerRadio.setSelected(true);
            } else {
                adminRadio.setSelected(true);
            }
        }
    }

    @FXML
    public void searchUserOnAction(ActionEvent event) {
        searchedList = FXCollections.observableArrayList();
        ResultSet result = null;
        if (!searchField.getText().isEmpty()) {
            String sqlStatement = "Select * from groceryshopper.accounts WHERE account_number LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.accounts WHERE first_name LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.accounts WHERE last_name LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.accounts WHERE street_number LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.accounts WHERE city LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.accounts WHERE postal_code LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.accounts WHERE email LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.accounts WHERE account_type LIKE '%" + searchField.getText() + "%'";
            DatabaseController database = new DatabaseController();
            Connection connection = database.getConnection();
            if (!searchedList.equals(null) || searchedList != null) {
                searchedList.clear();
            }

            try {
                Statement statement = connection.createStatement();
                result = statement.executeQuery(sqlStatement);
                while (result.next()) {
                    searchedList.add(new Accounts(result.getInt("account_number"), result.getString("first_name"), result.getString("last_name"),
                            result.getString("street_number"), result.getString("city"), result.getString("postal_code"),
                            result.getString("email"), result.getInt("account_type")));
                }
                accountTable.setItems(searchedList);
            } catch (SQLException e) {
                e.printStackTrace();
                statusLabel.setText("SQL Search user method failed");
            }

        } else {
            searchField.setPromptText("Fill me in First");
            accountTable.setItems(accountsList);
        }


        //searchField.textProperty().addListener((observable, oldValue, newValue)->{filteredList.setPredicate();});
    }

    @FXML
    public void addUserOnAction() {
        int type = 1;

        if (adminRadio.isSelected()) {
            type = 2;
        }
        if (ifValidFields()) {
            DatabaseController connection = new DatabaseController();
            Connection connectDatabase = connection.getConnection();
            String registerUserSql = "INSERT INTO groceryshopper.accounts (`first_name`, `last_name`, `street_number`, `city`, `postal_code`, `email`, `password`, `account_type`) VALUES ('" +
                    firstNameField.getText() + "','" + lastNameField.getText() + "','" + streetNumberField.getText() + "','" + cityField.getText() + "','" + postalField.getText() +
                    "','" + emailField.getText() + "','" + passwordField.getText() + "','" + type + "')";

            PreparedStatement preparedStatement;
            try {
                preparedStatement = connectDatabase.prepareStatement(registerUserSql);
                preparedStatement.execute();
                statusLabel.setText("Account Created");
                updateTable();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                statusLabel.setText("Sorry add failed");
            }
        }
        updateTable();

    }

    public void updateUserOnAction() {
        int type = 1;

        if (adminRadio.isSelected()) {
            type = 2;
        }

        if (ifValidFields()) {
            String sql = "update groceryshopper.accounts set first_name='" +
                    firstNameField.getText() + "',last_name='" + lastNameField.getText() +
                    "',street_number = '" + streetNumberField.getText() + "', city='" + cityField.getText() +
                    "', postal_code='" + postalField.getText() + "',email= '" + emailField.getText() +
                    "', account_type =" + type + "where account_number='" + idLabel.getText() + "'";
            DatabaseController connection = new DatabaseController();
            Connection connectDatabase = connection.getConnection();
            try {
                Statement statement = connectDatabase.createStatement();
                statement.execute(sql);

            } catch (Exception e) {
                e.printStackTrace();
                statusLabel.setText("Sorry Update failed");
            }
        }
        updateTable();

    }

    @FXML
    public void setAdminPanelButtonOnAction(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("adminView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Admin Page");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
        closeStage();
    }

    public boolean ifValidFields() {
        DatabaseController connection = new DatabaseController();
        Connection connectDatabase = connection.getConnection();
        String checkDuplicateEmail;
        String postalCode = postalField.getText();

        //if any fields are blank.
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || streetNumberField.getText().isEmpty() || cityField.getText().isEmpty() || postalField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
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

    public void closeStage() {
        Stage stage = (Stage) adminPanelButton.getScene().getWindow();
        stage.close();
    }

    //public
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateTable();
        customerRadio.setToggleGroup(radioGroup);
        adminRadio.setToggleGroup(radioGroup);
    }
}