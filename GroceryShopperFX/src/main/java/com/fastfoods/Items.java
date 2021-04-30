package com.fastfoods;

public class Items {

    private int item_id;
    private String name;
    private String description;
    private String imgSrc;
    private String type;
    private int stock;
    private double price;

    public Items(int item_id, String name, String description, String imgSrc, String type, int stock, double price) {
        setId(item_id);
        setName(name);
        setDescription(description);
        setImgSrc(imgSrc);
        setType(type);
        setStock(stock);
        setPrice(price);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return item_id;
    }

    public void setId(int id) {
        this.item_id = id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
