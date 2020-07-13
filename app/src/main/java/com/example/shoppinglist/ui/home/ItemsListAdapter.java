package com.example.shoppinglist.ui.home;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppinglist.LoginActivity;
import com.example.shoppinglist.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ItemsListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Item> items_list;
    private FirebaseFirestore db = LoginActivity.db;
    private Activity activity;
    private HomeFragment.MyAdapterCallback callback;

    public ItemsListAdapter(Context context, Activity activity, ArrayList<Item> items_list, HomeFragment.MyAdapterCallback callback) {
        this.context = context;
        this.items_list = items_list;
        this.activity = activity;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return items_list.size();
    }

    @Override
    public Object getItem(int position) {
        return items_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, null);

        TextView tv_item_name = view.findViewById(R.id.tv_item_name);
        final EditText ed_quantity = view.findViewById(R.id.ed_quantity);
        final CheckBox checkBox = view.findViewById(R.id.checkBox);

        tv_item_name.setText(items_list.get(position).getName());
        ed_quantity.setText(String.valueOf(items_list.get(position).getQuantity()));

        if (items_list.get(position).isBought())
            checkBox.setChecked(true);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Item " + items_list.get(position).getName() + " Checked", Toast.LENGTH_LONG).show();
                if (!checkBox.isChecked())
                    items_list.get(position).setBought(false);
                else
                    items_list.get(position).setBought(true);
                callback.actionWhenFinished(items_list.get(position));
            }
        });

        ed_quantity.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (ed_quantity.hasFocus()) {
                    int temp = Integer.parseInt(ed_quantity.getText().toString());
                    items_list.get(position).setQuantity(temp);
                    callback.actionWhenFinished(items_list.get(position));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }
}
