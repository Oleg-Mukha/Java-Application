package com.company;

public class Goods {
    private String product;
    private String brand;
    private int price;
    private boolean availability;

    public Goods() {

    }

    public Goods(String product, String brand, int price, boolean availability) {
        this.product = product;
        this.brand = brand;
        this.price = price;
        this.availability = availability;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean getAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "Product: " + product + " | Brand: " + brand + " | Price: " + price + "₴ | Availability: " + availability;
    }
}
