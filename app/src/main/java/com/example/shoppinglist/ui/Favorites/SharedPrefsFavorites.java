package com.example.shoppinglist.ui.Favorites;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.shoppinglist.ui.allLists.ShoppingList;

import java.util.ArrayList;
import java.util.Set;

public class SharedPrefsFavorites {

    private SharedPreferences sharedPref;

    public SharedPrefsFavorites(Activity activity) {
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
    }

//    public void addNewFavorites(String mail, String nickname) {
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.apply();
//    }
//
//    public boolean isDeleted(ShoppingList shoppingList) {
//        String temp = sharedPref.getString(shoppingList.getDoc_id(), null);
//        if (temp == null)
//            return false;
//        if (temp.equals("true"))
//            return true;
//        return false;
//    }

}
