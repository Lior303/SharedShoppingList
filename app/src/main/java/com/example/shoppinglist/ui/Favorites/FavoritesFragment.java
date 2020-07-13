package com.example.shoppinglist.ui.Favorites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shoppinglist.R;
import com.example.shoppinglist.SignUpActivity;
import com.example.shoppinglist.ui.allLists.ShoppingList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FavoritesFragment extends Fragment {

    private static FavoritesAdapter adapter;
    private ListView lv_favorites;
    private static ArrayList<FavoriteContact> arrayList;

    public static final String FAV_SHARED_PREFS = "FavSharedPrefs";
    public static final String fav = "fav";
    public static SharedPreferences sharedPreferences_fav;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        lv_favorites = getView().findViewById(R.id.lv_favorites_contacts);
        sharedPreferences_fav = getActivity().getSharedPreferences(FAV_SHARED_PREFS, Context.MODE_PRIVATE);

        // read from shared prefs

        Gson gson = new Gson();
        String json = sharedPreferences_fav.getString(fav, "");
        if (json.equals(""))
            arrayList = new ArrayList<>();
        else {
            Type typeMyType = new TypeToken<ArrayList<FavoriteContact>>(){}.getType();
            arrayList = gson.fromJson(json, typeMyType);

            // Collection
            Collections.sort(arrayList);
        }

        adapter = new FavoritesAdapter(getContext(), getActivity(), arrayList);
        lv_favorites.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addList:
                addNewFavorites();
                break;
        }
        return true;
    }

    private void addNewFavorites() {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        final View mView = getLayoutInflater().inflate(R.layout.dialog_add_fav, null);
        final EditText et_Email = mView.findViewById(R.id.et_email);
        final EditText et_nick = mView.findViewById(R.id.et_nick);

        Button btn_ok = mView.findViewById(R.id.btn_ok_mail);
        Button ban_cancel = mView.findViewById(R.id.btn_cancel_mail);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = et_Email.getText().toString();
                if (mail.isEmpty()) {
                    et_Email.setError("Field cannot be empty");
                    et_Email.requestFocus();
                    return;

                } else if (!SignUpActivity.isEmailValid(mail)) {
                    et_Email.setError("Please enter a valid Email");
                    et_Email.requestFocus();
                    return;
                } else {
                    String nickname = et_nick.getText().toString();
                    if (nickname.isEmpty()) {
                        et_nick.setError("Field cannot be empty");
                        et_nick.requestFocus();
                        return;
                    }
                    arrayList.add(new FavoriteContact(mail, nickname));
                    adapter.notifyDataSetChanged();
                    writeToSharedPreferences();
                    dialog.dismiss();
                }
            }

        });
        ban_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void writeToSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences_fav.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString(fav, json);
        editor.apply();
        adapter.notifyDataSetChanged();
    }
}