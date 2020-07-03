package com.example.shoppinglist.ui.allLists;

import java.util.ArrayList;
import java.util.Map;

public class ShoppingList {

    private String owner;
    private String list_name;
    private ArrayList<String> cont;
    private String doc_id;

    public ShoppingList(String owner, String list_name, ArrayList<String> cont, String doc_id) {
        this.owner = owner;
        this.list_name = list_name;
        this.cont = cont;
        this.doc_id = doc_id;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getList_name() {
        return list_name;
    }

    public void setList_name(String list_name) {
        this.list_name = list_name;
    }

    public ArrayList<String> getCont() {
        return cont;
    }

    public void setCont(ArrayList<String> cont) {
        this.cont = cont;
    }
}
