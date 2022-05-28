package com.example.furniture_shop.Classes;

public class Order {
    private String id;
    private String totalPrice;
    private String data;
    private String status;

    public Order(String id, String totalPrice, String data, String status) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.data = data;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
