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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ItemEditorController implements Initializable {
    ObservableList<Items> itemsList;
    ObservableList<Items> searchedList;
    int i;
    @FXML
    private TextField searchField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField descField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField stockField;
    @FXML
    private TextField pathField;
    @FXML
    private TextField typeField;
    @FXML
    private TableView<Items> itemTable;
    @FXML
    private TableColumn<Items, Integer> idCol;
    @FXML
    private TableColumn<Items, String> nameCol;
    @FXML
    private TableColumn<Items, String> descCol;
    @FXML
    private TableColumn<Items, Double> priceCol;
    @FXML
    private TableColumn<Items, Integer> stockCol;
    @FXML
    private TableColumn<Items, String> pathCol;
    @FXML
    private TableColumn<Items, String> typeCol;
    @FXML
    private Button addButton;
    @FXML
    private Button adminPanelButton;
    @FXML
    private Button updateButton;
    @FXML
    private Label statusLabel;
    @FXML
    private Label idLabel;

    @FXML
    public void updateTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        pathCol.setCellValueFactory(new PropertyValueFactory<>("imgSrc"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        itemsList = getItems();
        itemTable.setItems(itemsList);
    }

    private ObservableList<Items> getItems() {
        DatabaseController connection = new DatabaseController();
        Connection connectDatabase = connection.getConnection();
        Statement statement = null;
        ObservableList<Items> list = FXCollections.observableArrayList();
        try {
            statement = connectDatabase.createStatement();
            String sqlStatement = "Select * From groceryshopper.items";
            ResultSet result = statement.executeQuery(sqlStatement);
            while (result.next()) {
                list.add(new Items(result.getInt("item_id"), result.getString("item_name"), result.getString("item_description"),
                        result.getString("img_path"), result.getString("item_type"), result.getInt("item_stock"),
                        result.getDouble("item_price")));
            }
            connectDatabase.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return list;
    }

    @FXML
    public void getItem(MouseEvent event) {
        i = itemTable.getSelectionModel().getSelectedIndex();
        if (i > -1) {
            typeField.setText(typeCol.getCellData(i));
            nameField.setText(nameCol.getCellData(i));
            descField.setText(descCol.getCellData(i));
            priceField.setText("" + priceCol.getCellData(i));
            pathField.setText(pathCol.getCellData(i));
            stockField.setText("" + stockCol.getCellData(i));
            idLabel.setText("" + idCol.getCellData(i));

        }
    }

    @FXML
    public void adminPanelButtonOnAction(ActionEvent event) {
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

    private void closeStage() {
        Stage stage = (Stage) adminPanelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void searchItemOnAction(ActionEvent event) {
        searchedList = FXCollections.observableArrayList();
        ResultSet result = null;
        if (!searchField.getText().isEmpty()) {
            String sqlStatement = "Select * from groceryshopper.items WHERE item_id LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.items WHERE item_name LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.items WHERE item_description LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.items WHERE item_stock LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.items WHERE item_price LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.items WHERE item_type LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.items WHERE img_path LIKE '%" + searchField.getText() + "%'";
            DatabaseController database = new DatabaseController();

            Connection connection = database.getConnection();
            if (!searchedList.equals(null) || searchedList != null) {
                searchedList.clear();
            }

            try {
                Statement statement = connection.createStatement();
                result = statement.executeQuery(sqlStatement);
                while (result.next()) {
                    searchedList.add(new Items(result.getInt("item_id"), result.getString("item_name"), result.getString("item_description"),
                            result.getString("img_path"), result.getString("item_type"), result.getInt("item_stock"),
                            result.getDouble("item_price")));
                }
                itemTable.setItems(searchedList);
            } catch (SQLException e) {
                e.printStackTrace();
                statusLabel.setText("SQL Search user method failed");
            }

        } else {
            searchField.setPromptText("Fill me in First");
            itemTable.setItems(itemsList);
        }


        //searchField.textProperty().addListener((observable, oldValue, newValue)->{filteredList.setPredicate();});
    }

    @FXML
    public void addItemOnAction(ActionEvent event) {
        DatabaseController connection = new DatabaseController();
        Connection connectDatabase = connection.getConnection();
        if (itemIsValid()) {
            String registerUserSql = "INSERT INTO groceryshopper.items (`item_name`, `item_description`, `item_price`, `item_type`, `img_path`, `item_stock`) VALUES ('"+
                    nameField.getText() + "','" + descField.getText() + "','" + priceField.getText() + "','" + typeField.getText() + "','" +
                    pathField.getText() + "','" + stockField.getText() + "')";
            System.out.println(registerUserSql);

            Statement statement = null;
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = connectDatabase.prepareStatement(registerUserSql);
                preparedStatement.execute();
                statusLabel.setText("Item Added");
                updateTable();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                statusLabel.setText("Sorry add failed");
            }
        }
        updateTable();

    }

    private boolean itemIsValid() {
        if (nameField.getText().isEmpty() || descField.getText().isEmpty() || typeField.getText().isEmpty() || stockField.getText().isEmpty() || priceField.getText().isEmpty()) {
            statusLabel.setText("Fill In All Fields First!");
            return false;
        }
        try {
            double price = Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            statusLabel.setText("Price has to be an int or double");
            return false;
        }
        return true;
    }


    public void updateItemOnAction(ActionEvent event) {
        if (itemIsValid()) {
            String sql = "update groceryshopper.items set item_name='" +
                    nameField.getText() + "',item_description='" + descField.getText() +
                    "',item_price = '" + priceField.getText() + "', item_type='" + typeField.getText() +
                    "', img_path='" + pathField.getText() + "',item_stock= '" + stockField.getText() +
                    "' where item_id = '" + idLabel.getText() + "'";
            DatabaseController connection = new DatabaseController();
            Connection connectDatabase = connection.getConnection();
            try {
                Statement statement = connectDatabase.createStatement();
                statement.execute(sql);
            } catch (Exception e) {
                e.printStackTrace();
                statusLabel.setText("Sorry Update failed");

            }
            updateTable();
        }
    }

    //public
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateTable();
    }
}
