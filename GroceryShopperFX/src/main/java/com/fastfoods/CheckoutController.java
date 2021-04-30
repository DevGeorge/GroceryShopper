package com.fastfoods;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class CheckoutController implements Initializable {

    @FXML
    private Font x1;

    @FXML
    private Button clearButton;

    @FXML
    private Label firstNameField;

    @FXML
    private Label lastNameField;

    @FXML
    private Label streetLabel;

    @FXML
    private Label cityLabel;

    @FXML
    private Label postalLabel;
    @FXML
    private Label totalLabel;

    @FXML
    private RadioButton visaRadio;

    @FXML
    private RadioButton masterRadio;

    @FXML
    private RadioButton cashRadio;

    @FXML
    private Button returnToProducts;

    @FXML
    private Label statusLabel;

    @FXML
    private GridPane grid;
    @FXML
    private VBox checkoutBox;

    ToggleGroup payment = new ToggleGroup();
    private List<CartItems> cartItems = SearchScreenController.cartItemsList;
    private Carts cart;
    private Accounts account;
    double totalPrice = 0.00;
    @FXML
    void clearButtonOnAction(ActionEvent event) {
        cartItems.clear();
        updateGrid();
        totalPrice = 0.00;
        SearchScreenController.cartQuantity = 0;
        setLabels();
    }

    @FXML
    void returnToProductsOnAction(ActionEvent event) {
        hideScene();
    }

    private void hideScene() {
        Stage stage = (Stage) returnToProducts.getScene().getWindow();
        stage.hide();
    }
    private void showScene(){
        Stage stage = (Stage) returnToProducts.getScene().getWindow();
        stage.show();
    }
    @FXML
    void orderOnAction(ActionEvent event) throws InterruptedException {
        RadioButton selected;
        selected = (RadioButton) payment.getSelectedToggle();
        String sql;
        PreparedStatement preparedStatement;
        DatabaseController connection = new DatabaseController();
        Connection connectDatabase = connection.getConnection();

            if (selected != null) {
                for (CartItems temp : cartItems) {
                    cart = new Carts(account.getId(), temp.getItem().getId(), temp.getQuantity(), selected.getText());
                    sql = "INSERT INTO groceryshopper.carts (`account_id`, `item_id`, `quantity`,`payment_type`) VALUES ('" +
                            cart.getAccountId() + "','" + cart.getItemId() + "','" + cart.getQuantity() + "','" + cart.getPaymentType() + "')";
                    try {
                        preparedStatement = connectDatabase.prepareStatement(sql);
                        preparedStatement.execute();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                statusLabel.setText("Order Complete. Please restart to place another order.");
                checkoutBox.setDisable(true);

            }else{
                statusLabel.setText("Please select a payment method");
            }

    }


    public void updateGrid() {
        int col = 0;
        int row = 1;

        grid.getChildren().clear();

        try {
            for (int i = 0; i < cartItems.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("cartItem.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                anchorPane.setMaxWidth(250);
                CartItemController itemController = fxmlLoader.getController();
                itemController.setData(cartItems.get(i));
                grid.add(anchorPane, col, row++);
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(200);
                grid.setMaxWidth(250);

                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(100);
                grid.setMaxHeight(200);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void closeScene() {
        Stage stage = (Stage) returnToProducts.getScene().getWindow();
        stage.close();
    }
    private void setLabels() {
        firstNameField.setText(account.getFirstName());
        lastNameField.setText(account.getLastName());
        streetLabel.setText(account.getStreetNumber());
        cityLabel.setText(account.getCity());
        postalLabel.setText(account.getPostal());
        CartItems temp;
        Items tempItem;
        for (int i = 0; i < cartItems.size(); i++) {
            temp = cartItems.get(i);
            tempItem = temp.getItem();
            totalPrice += (tempItem.getPrice() * temp.getQuantity());
            System.out.println(totalPrice);
        }
        totalLabel.setText("" + totalPrice);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        visaRadio.setToggleGroup(payment);;
        masterRadio.setToggleGroup(payment);;
        cashRadio.setToggleGroup(payment);
        account = App.LOGGEDINACCOUNT;
        updateGrid();
        setLabels();
    }


}
