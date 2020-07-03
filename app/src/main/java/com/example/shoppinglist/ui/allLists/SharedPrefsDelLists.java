package com.example.shoppinglist.ui.allLists;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class SharedPrefsDelLists {

    private SharedPreferences sharedPref;

    public SharedPrefsDelLists(Activity activity) {
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public void addNewDeletedList(ShoppingList shoppingList) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(shoppingList.getDoc_id(), "true");
        editor.apply();
    }

    public boolean isDeleted(ShoppingList shoppingList) {
        String temp = sharedPref.getString(shoppingList.getDoc_id(), null);
        if (temp == null)
            return false;
        if (temp.equals("true"))
            return true;
        return false;
    }

}
