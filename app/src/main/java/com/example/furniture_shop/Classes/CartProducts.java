package com.example.furniture_shop.Classes;

import java.util.ArrayList;

public class CartProducts {

    public static ArrayList<CartProducts> cartProducts = new ArrayList<>();

    private String id;
    private String name;
    private String description;
    private String image;
    private String product_id;
    private String count;
    private String price;

    public CartProducts(String id, String product_id, String count, String name, String description, String image, String price) {
        this.id = id;
        this.product_id = product_id;
        this.count = count;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;

        CartProducts.cartProducts.add(this);
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


}
