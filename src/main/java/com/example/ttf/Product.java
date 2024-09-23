package com.example.ttf;

import androidx.annotation.Keep;

public class Product {
    private String id;
    private String name;
    private int stockCount;

    @Keep
    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(Product.class)
    }

    public Product(String id, String name, int stockCount) {
        this.id = id;
        this.name = name;
        this.stockCount = stockCount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }
}

