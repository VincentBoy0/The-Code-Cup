package com.example.codecup;

import java.util.List;

public class Category {
    private String name;
    private List<MenuItem> items;

    public Category(String name, List<MenuItem> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public List<MenuItem> getItems() {
        return items;
    }
}

