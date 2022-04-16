package com.company;

public class Order {
    String product;
    String brand;
    String status;
    int totalPrice;

    Order() {

    }

    Order(String product, String brand, String status, int totalPrice) {
        this.product = product;
        this.brand = brand;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return " | Product: " + product + " | Brand: " + brand + " | Order cost: " + totalPrice + "₴ | Status: " + status;
    }
}
