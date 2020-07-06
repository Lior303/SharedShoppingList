package com.example.shoppinglist.ui.Favorites;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shoppinglist.LoginActivity;
import com.example.shoppinglist.R;
import com.example.shoppinglist.ui.allLists.ShoppingList;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavoritesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<FavoriteContact> favorites;
    private Activity activity;

    public FavoritesAdapter(Context context, Activity activity, ArrayList<FavoriteContact> favorites) {
        this.context = context;
        this.activity = activity;
        this.favorites = favorites;
    }

    @Override
    public int getCount() {
        return favorites.size();
    }

    @Override
    public Object getItem(int position) {
        return favorites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorites_layout, null);

        TextView tv_mail, tv_nickname;
        ImageView iv_remove_fav;

        tv_mail = view.findViewById(R.id.tv_fav_mail);
        tv_nickname = view.findViewById(R.id.tv_fav_nickname);
        iv_remove_fav = view.findViewById(R.id.iv_remove_fav);

        tv_mail.setText("Email: " + favorites.get(position).getMail());
        tv_nickname.setText("Nickname: " + favorites.get(position).getNickname());

        iv_remove_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorites.remove(position);
                FavoritesFragment.writeToSharedPreferences();
            }
        });

        return view;
    }
}
