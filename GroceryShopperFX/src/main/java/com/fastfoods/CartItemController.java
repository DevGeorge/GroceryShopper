package com.fastfoods;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class CartItemController {
    @FXML
    private ImageView image;

    @FXML
    private Label nameLabel;

    @FXML
    private Label descLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label quantityLabel;

    private Items item;

    public void setData(CartItems cartItem) {
        this.item = cartItem.getItem();
        nameLabel.setText(item.getName());
        priceLabel.setText("$" + item.getPrice());
        try {
            Image img = new Image(getClass().getResourceAsStream(item.getImgSrc()));
            image.setImage(img);
        } catch (Exception e) {
            e.getStackTrace();
        }
        quantityLabel.setText("Selected: " + cartItem.getQuantity());
        descLabel.setText(item.getDescription());
        typeLabel.setText(item.getType());
    }
}
