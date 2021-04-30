package com.fastfoods;

public class Carts {
    private int cartId;
    private int accountId;
    private int itemId;
    private int quantity;
    private String dateOrdered;
    private String dateCompleted;
    private String paymentType;

    public Carts(int accountId, int itemId, int quantity, String paymentType) {
        this.accountId = accountId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.paymentType = paymentType;
    }

    public Carts(int cart_id, int account_id, int item_id, int quantity, String date_ordered, String date_completed, String payment_type) {
        this.cartId = cart_id;
        this.accountId = account_id;
        this.itemId = item_id;
        this.quantity = quantity;
        this.dateOrdered = date_ordered;
        this.dateCompleted = date_completed;
        this.paymentType = payment_type;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(String dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public String getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(String dateCompleted) {
        this.dateCompleted = dateCompleted;
    }
}
