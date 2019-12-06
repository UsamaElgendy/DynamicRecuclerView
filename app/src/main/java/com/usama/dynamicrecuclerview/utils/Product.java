package com.usama.dynamicrecuclerview.utils;


public class Product {
    private String name;
    private String value;

    // variable needed to memorize item position in recyclerview
    private int position;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int price) {
        this.position = price;
    }
}
