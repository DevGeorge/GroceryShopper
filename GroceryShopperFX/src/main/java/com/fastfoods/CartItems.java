package com.fastfoods;

public class CartItems{
    private Items item;
    private Integer quantity;

    public CartItems(Items selectedItem, int qty) {
        setItem(selectedItem);
        setQuantity(qty);
    }

    public Items getItem() {
        return item;
    }

    public void setItem(Items item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
