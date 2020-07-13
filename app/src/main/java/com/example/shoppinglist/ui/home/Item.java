package com.example.shoppinglist.ui.home;

import androidx.annotation.Nullable;

public class Item implements Comparable {

    private String name;
    private int quantity;
    private boolean bought;

    public Item(String item_name, int quantity, boolean bought) {
        this.name = item_name;
        this.quantity = quantity;
        this.bought = bought;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Item) {
            Item temp = (Item) o;
            return Boolean.compare(isBought(), temp.isBought());
        }
        return -1;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Item) {
            Item temp = (Item) obj;
            return this.getName().equals(temp.getName());
        }
        return false;
    }
}
