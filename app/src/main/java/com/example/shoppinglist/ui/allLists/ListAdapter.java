package com.example.shoppinglist.ui.allLists;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.shoppinglist.LoginActivity;
import com.example.shoppinglist.R;
import com.example.shoppinglist.SignUpActivity;
import com.example.shoppinglist.ui.Favorites.FavoriteContact;
import com.example.shoppinglist.ui.Favorites.FavoritesFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ShoppingList> shoppingList;
    private FirebaseFirestore db = LoginActivity.db;
    private Activity activity;

    public ListAdapter(Context context, Activity activity, ArrayList<ShoppingList> shoppingList) {
        this.context = context;
        this.shoppingList = shoppingList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return shoppingList.size();
    }

    @Override
    public Object getItem(int position) {
        return shoppingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_layout, null);

        TextView tv_list_name = view.findViewById(R.id.list_name);
        ImageView iv_del_list, iv_add_cont, iv_remove_cont;
        iv_del_list = view.findViewById(R.id.iv_del_list);
        iv_add_cont = view.findViewById(R.id.iv_add_cont);
        iv_remove_cont = view.findViewById(R.id.iv_remove_cont);
        TextView tv_owner = view.findViewById(R.id.tv_owner);

        String owner = shoppingList.get(position).getOwner();
        if (owner.equals(LoginActivity.mAuth.getCurrentUser().getEmail()))
            owner = "You";
        tv_owner.setText("List Owner: " + owner);
        tv_list_name.setText(shoppingList.get(position).getList_name());

        final String finalOwner = owner;
        iv_del_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalOwner.equals("You"))
                    deleteShoppingList(shoppingList.get(position));
                else {
                    AllListFragment.sharedPrefsDelLists.addNewDeletedList(shoppingList.get(position));
                }
                Toast.makeText(context, "List deleted successfully", Toast.LENGTH_LONG).show();
                AllListFragment.refreshData(shoppingList.get(position));
            }
        });

        iv_add_cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalOwner.equals("You"))
                    showAddContributorDialog("Add New Contributor", position, true);
                else Toast.makeText(context, "Cant add contributors because you are not owner", Toast.LENGTH_LONG).show();
            }
        });

        iv_remove_cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalOwner.equals("You"))
                    showAddContributorDialog("Remove Contributor", position, false);
                else Toast.makeText(context, "Cant remove contributors because you are not owner", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    private void showAddContributorDialog(String title, final int position, final boolean add) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        // set the custom layout
        final View customLayout = activity.getLayoutInflater().inflate(R.layout.dialog_add_cont, null);
        builder.setView(customLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText et_mail = customLayout.findViewById(R.id.ed_dialog_email);
                if (et_mail.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Field cannot be empty", Toast.LENGTH_LONG).show();

                    //לפי ניק
                } else if (!SignUpActivity.isEmailValid(et_mail.getText().toString())) {
                    String mail_from_nick = getMailFromNick(et_mail.getText().toString());
                    if (mail_from_nick.equals("null")) {
                        Toast.makeText(context, "No such favorite", Toast.LENGTH_LONG).show();

                    } else addOrRemoveContributor(shoppingList.get(position), mail_from_nick, add);
                } else
                    addOrRemoveContributor(shoppingList.get(position), et_mail.getText().toString(), add);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addOrRemoveContributor(final ShoppingList shoppingList, final String mail, final boolean add) {
        db.collection("ShoppingLists")
                .document(shoppingList.getDoc_id())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> cont = (ArrayList<String>) task.getResult().get("cont");
                            if (add)
                                cont.add(mail);
                            else
                                cont.remove(mail);
                            db.collection("ShoppingLists")
                                    .document(shoppingList.getDoc_id())
                                    .update("cont", cont)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                                if (add)
                                                    Toast.makeText(context, "New contributor added successfully", Toast.LENGTH_LONG).show();
                                                else
                                                    Toast.makeText(context, "Contributor deleted successfully", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                });
    }

    private void deleteShoppingList(ShoppingList shoppingList) {
        db.collection("ShoppingLists")
                .document(shoppingList.getDoc_id())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful())
                            Toast.makeText(context, "Error occured", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private String getMailFromNick(String nick) {
        Gson gson = new Gson();
        ArrayList<FavoriteContact> arrayList = null;
        if (FavoritesFragment.sharedPreferences_fav == null)
            FavoritesFragment.sharedPreferences_fav = activity.getSharedPreferences("FavSharedPrefs", Context.MODE_PRIVATE);
        String json = FavoritesFragment.sharedPreferences_fav.getString("fav", "");
        if (!json.equals("")) {
            Type typeMyType = new TypeToken<ArrayList<FavoriteContact>>() {
            }.getType();
            arrayList = gson.fromJson(json, typeMyType);

            for (FavoriteContact favorite : arrayList) {
                if (favorite.getNickname().equals(nick))
                    return favorite.getMail();
            }
        }
        return "null";
    }
}
