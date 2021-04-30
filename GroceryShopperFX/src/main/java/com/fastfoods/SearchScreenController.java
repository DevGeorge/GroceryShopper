package com.fastfoods;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SearchScreenController implements Initializable {

    private final List<Items> items = new ArrayList<>();
    public static List<CartItems> cartItemsList = new ArrayList<>();
    private CartItems cartItem;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private Label itemNameSelected;
    @FXML
    private Label itemPriceSelected;
    @FXML
    private Label itemDescriptionSelected;
    @FXML
    private Label selectedType;
    @FXML
    private Label stockLabel;
    @FXML
    private Label addToCartStatus;
    @FXML
    private ImageView imageViewSelected;
    @FXML
    private ComboBox<Integer> quantityComboBox;
    @FXML
    private Button addToCartButton;
    @FXML
    private ScrollPane scroll;
    @FXML
    private Label cartNumberLabel;
    @FXML
    private GridPane grid;
    private Image image;
    private Listener listener;
    private List<Items> searchedItems = new ArrayList<>();
    private static Items selectedItem;
    private int qty = 0;
    public static int cartQuantity = 0;

    private List<Items> getAllItems() throws ClassNotFoundException, SQLException {
        DatabaseController connection = new DatabaseController();
        Connection connectDatabase = connection.getConnection();
        Statement statement = connectDatabase.createStatement();
        String sqlStatement = "Select * From groceryshopper.items";
        ResultSet result = statement.executeQuery(sqlStatement);
        while (result.next()) {
            Items item = new Items(result.getInt("item_id"), result.getString("item_name"), result.getString("item_description"), result.getString("img_path"), result.getString("item_type"), result.getInt("item_stock"), result.getDouble("item_price"));
            items.add(item);
        }
        connectDatabase.close();
        return items;
    }

    private void setSelectedItemCard(Items item) {
        selectedItem = item;
        itemNameSelected.setText(item.getName());
        itemDescriptionSelected.setText(item.getDescription());
        itemPriceSelected.setText("$" + item.getPrice());
        selectedType.setText("Item Category: " + item.getType());
        stockLabel.setText("Quantity Remaining: " + item.getStock());
        quantityComboBox.getItems().clear();
        for (int i = 0; i < item.getStock(); i++) {
            quantityComboBox.getItems().add(i + 1);
        }
        try {
            image = new Image(getClass().getResourceAsStream(item.getImgSrc()));
            imageViewSelected.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @FXML
    public void addToCartButtonOnAction(ActionEvent event) {
        int index = -1;
        CartItems temp;
        if (quantityComboBox.getValue() == null) {
            qty = 1;
        } else {
            qty = quantityComboBox.getValue();
        }
        if ((selectedItem.getStock() - qty) < 0) {
            addToCartStatus.setText("Sorry, Not enough of Stock");
            return;
        }
        for (CartItems item: cartItemsList){
            if(item.getItem().getId() == selectedItem.getId()){
                index = cartItemsList.indexOf(item);
                break;
            }
        }
        if (index > -1) {
            temp = cartItemsList.get(index);
            temp.setQuantity(temp.getQuantity()+qty);


        }else{
            cartItemsList.add(new CartItems(selectedItem, qty));
        }
        cartQuantity += qty;
        selectedItem.setStock(selectedItem.getStock() - qty);
        setLabels();
        updateGrid();
    }
    public void setLabels(){
        stockLabel.setText("Quantity Remaining: " + selectedItem.getStock());
        addToCartStatus.setText("Item Added Successfully");
        cartNumberLabel.setText(cartQuantity +"");
    }

    @FXML
    public void searchButtonOnAction(ActionEvent event) {
        ResultSet result = null;
        if (!searchField.getText().isEmpty()) {
            String sqlStatement = "Select * from groceryshopper.items WHERE item_id LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.items WHERE item_name LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.items WHERE item_description LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.items WHERE item_type LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.items WHERE item_stock LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.items WHERE img_path LIKE '%" + searchField.getText() + "%'"
                    + " UNION select * from groceryshopper.items WHERE item_price LIKE '%" + searchField.getText() + "%'";
            DatabaseController database = new DatabaseController();
            Connection connection = database.getConnection();
            searchedItems.clear();
            try {
                Statement statement = connection.createStatement();
                result = statement.executeQuery(sqlStatement);
                while (result.next()) {
                    searchedItems.add(new Items(result.getInt("item_id"), result.getString("item_name"), result.getString("item_description"), result.getString("img_path"), result.getString("item_type"), result.getInt("item_stock"), result.getDouble("item_price")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            searchedItems = new ArrayList<>(items);
            searchField.setPromptText("Fill me in First");
        }
        updateGrid();
    }

    @FXML
    public void goToCheckoutOnAction(MouseEvent event) {
        Parent root = null;
        Stage primaryStage =  new Stage();
        try {
            root = FXMLLoader.load(getClass().getResource("checkoutScreen.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Checkout");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
    public void hideScene(){
        Stage stage = (Stage) addToCartButton.getScene().getWindow();
        stage.hide();
    }
    public void showScene(){
        Stage stage = (Stage) addToCartButton.getScene().getWindow();
        stage.show();
    }
    public void updateGrid() {
        int col = 0;
        int row = 1;

        grid.getChildren().clear();

        try {
            for (int i = 0; i < searchedItems.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                ItemController itemController = fxmlLoader.getController();
                itemController.setData(searchedItems.get(i), listener);

                if (col == 4) {
                    col = 0;
                    row++;
                }
                grid.add(anchorPane, col++, row);
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);


                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void closeScene() {
        Stage stage = (Stage) addToCartButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            getAllItems();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if (items.size() > 0) {
            setSelectedItemCard(items.get(0));
            listener = new Listener() {
                @Override
                public void onClick(Items item) {
                    setSelectedItemCard(item);
                }
            };
            searchedItems = new ArrayList<>(items);
            updateGrid();
        }
        setLabels();
    }
}
