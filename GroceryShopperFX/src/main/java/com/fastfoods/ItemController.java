package com.fastfoods;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ItemController {
    @FXML
    private Label itemNameLabel;
    @FXML
    private Label itemPriceLabel;
    @FXML
    private ImageView image;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label quantityLabel;

    private Items item;

    private Listener listener;

    @FXML
    private void clickOnAction(MouseEvent event) {
        listener.onClick(item);
    }

    public void setData(Items item, Listener listener) {
        this.item = item;
        this.listener = listener;
        itemNameLabel.setText(item.getName());
        itemPriceLabel.setText("$" + item.getPrice());
        try {
            Image img = new Image(getClass().getResourceAsStream(item.getImgSrc()));
            image.setImage(img);
        } catch (Exception e) {
            e.getStackTrace();
        }
        quantityLabel.setText("Qty: " + item.getStock());
        descriptionLabel.setText(item.getDescription());
    }
}
