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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class OrderEditorController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private Button resetTable;

    @FXML
    private Label cartLabel;

    @FXML
    private Label customerLabel;

    @FXML
    private Label itemLabel;

    @FXML
    private Label qtyLabel;

    @FXML
    private Label paymentLabel;

    @FXML
    private TableView<Carts> orderTable;

    @FXML
    private TableColumn<Carts, Integer> cartCol;

    @FXML
    private TableColumn<Carts, Integer> numberCol;

    @FXML
    private TableColumn<Carts, Integer> itemCol;

    @FXML
    private TableColumn<Carts, Integer> qtyCol;

    @FXML
    private TableColumn<Carts, String> orderedCol;

    @FXML
    private TableColumn<Carts, String> completedCol;

    @FXML
    private TableColumn<Carts, String> typeCol;

    @FXML
    private Button completeButton;

    @FXML
    private Button cancelOrder;

    @FXML
    private Button adminPanelButton;

    @FXML
    private Label statusLabel;

    ObservableList<Carts> allCarts;
    ObservableList<Carts> searchedCarts;
    int i;

    @FXML
    void adminPanelButtonOnAction(ActionEvent event) {
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
    void cancelOnAction(ActionEvent event) {
        DatabaseController connection = new DatabaseController();
        Connection connectDatabase = connection.getConnection();
        String sql = "update groceryshopper.carts set quantity='0' where cart_id = '"+ cartLabel.getText() +"'";
        try {
            Statement statement = connectDatabase.createStatement();
            statement.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Sorry Cancel failed");
        }
        updateTable();
    }

    @FXML
    void completeOnAction(ActionEvent event) {
        DatabaseController connection = new DatabaseController();
        Connection connectDatabase = connection.getConnection();
        String sql = "update groceryshopper.carts set date_completed = NOW() where cart_id = '"+ cartLabel.getText() +"'";
        try {
            Statement statement = connectDatabase.createStatement();
            statement.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Sorry complete failed");
        }
        updateTable();
    }

    @FXML
    void getItem(MouseEvent event) {
        i = orderTable.getSelectionModel().getSelectedIndex();
        if (i > -1){
            cartLabel.setText(""+ cartCol.getCellData(i));
            customerLabel.setText("Customer ID: "+ numberCol.getCellData(i));
            itemLabel.setText("Item ID: "+ itemCol.getCellData(i));
            qtyLabel.setText("Quantity: "+ qtyCol.getCellData(i));
            paymentLabel.setText("Payment Method: " + typeCol.getCellData(i));
        }
    }

    @FXML
    void searchItemOnAction(ActionEvent event) {
        searchedCarts = FXCollections.observableArrayList();
        ResultSet result = null;
        if (!searchField.getText().isEmpty()) {
            String sqlStatement = "Select * from groceryshopper.carts WHERE cart_id LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.carts WHERE account_id LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.carts WHERE item_id LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.carts WHERE quantity LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.carts WHERE date_ordered LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.carts WHERE date_completed LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.carts WHERE payment_type LIKE '%" + searchField.getText() + "%'";
            DatabaseController database = new DatabaseController();

            Connection connection = database.getConnection();
            if (!searchedCarts.equals(null) || searchedCarts != null) {
                searchedCarts.clear();
            }

            try {
                Statement statement = connection.createStatement();
                result = statement.executeQuery(sqlStatement);
                while (result.next()) {
                    searchedCarts.add(new Carts(result.getInt("cart_id"), result.getInt("account_id"), result.getInt("item_id"),
                            result.getInt("quantity"), result.getString("date_ordered"), result.getString("date_completed"),
                            result.getString("payment_type")));
                }
                orderTable.setItems(searchedCarts);
            } catch (SQLException e) {
                e.printStackTrace();
                statusLabel.setText("SQL Search user method failed");
            }

        } else {
            searchField.setPromptText("Fill me in First");
            orderTable.setItems(allCarts);
        }
    }

    @FXML
    void updateTable() {
        cartCol.setCellValueFactory(new PropertyValueFactory<>("cartId"));
        numberCol.setCellValueFactory(new PropertyValueFactory<>("accountId"));
        itemCol.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderedCol.setCellValueFactory(new PropertyValueFactory<>("dateOrdered"));
        completedCol.setCellValueFactory(new PropertyValueFactory<>("dateCompleted"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("paymentType"));

        allCarts = getOrders();
        orderTable.setItems(allCarts);

    }
    public ObservableList<Carts> getOrders(){
        DatabaseController connection = new DatabaseController();
        Connection connectDatabase = connection.getConnection();
        Statement statement;
        ObservableList<Carts> list = FXCollections.observableArrayList();
        try {
            statement = connectDatabase.createStatement();
            String sqlStatement = "Select * From groceryshopper.carts";
            ResultSet result;
            result = statement.executeQuery(sqlStatement);
            while (result.next()) {
                list.add(new Carts(result.getInt("cart_id"), result.getInt("account_id"), result.getInt("item_id"),
                        result.getInt("quantity"), result.getString("date_ordered"), result.getString("date_completed"),
                        result.getString("payment_type")));
            }
            connectDatabase.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateTable();
    }
}
