package com.example.shoppinglist.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shoppinglist.LoginActivity;
import com.example.shoppinglist.R;
import com.example.shoppinglist.ui.allLists.AllListFragment;
import com.example.shoppinglist.ui.allLists.ShoppingList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String mail = LoginActivity.mAuth.getCurrentUser().getEmail();

    private FirebaseFirestore db = LoginActivity.db;
    private Spinner spinner;
    private TextView tv_shoppingList_name;
    private final ArrayList<String> lists_names = new ArrayList<>();

    private ListView lv_items_list;
    private ArrayList<Item> items_array;
    private ItemsListAdapter adapter;

    private String doc_ref;

    public interface MyAdapterCallback {
        void actionWhenFinished(Item item);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tv_shoppingList_name = getView().findViewById(R.id.tv_shopingList_name);
        FloatingActionButton fab = getView().findViewById(R.id.fab);
        lv_items_list = getView().findViewById(R.id.lv_items_lists);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add new item to this list", Snackbar.LENGTH_LONG)
                        .setAction("Add", null).show();

                //todo - update firebase realtime

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                final View mView = getLayoutInflater().inflate(R.layout.dialog_add_new_item, null);
                final EditText et_quantity = mView.findViewById(R.id.ed_new_item_quantity);
                final EditText et_name = mView.findViewById(R.id.et_new_item_name);

                Button btn_ok = mView.findViewById(R.id.btn_ok_item);
                Button ban_cancel = mView.findViewById(R.id.btn_cancel_item);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String name = et_name.getText().toString();

                            ////////////////      Exception handling   ///////////////////
                            if (name.isEmpty())
                                throw new Exception("name cannot be empty");
                            int quantity = Integer.parseInt(et_quantity.getText().toString()); // can throw exception
                            ///////////////////////////////////

                            Item item = new Item(name, quantity, false);
                            items_array.add(item);
                            adapter.notifyDataSetChanged();

                            db.collection("ShoppingLists")
                                    .document(doc_ref)
                                    .collection("Items")
                                    .document(item.getName())
                                    .set(item)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Item updated on db", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_LONG).show();
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
        });

        spinner = getView().findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        updateSpinner();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String listName = parent.getItemAtPosition(position).toString();
        tv_shoppingList_name.setText("Shopping list " + listName);

        db.collection("ShoppingLists")
                .whereArrayContains("cont", mail)
                .whereEqualTo("list_name", listName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            doc_ref = task.getResult().getDocuments().get(0).getId();
                            db.collection("ShoppingLists")
                                    .document(doc_ref)
                                    .collection("Items")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                items_array = new ArrayList<>();
                                                for (DocumentSnapshot documentSnapshot: task.getResult().getDocuments()) {
                                                    items_array.add(new Item(documentSnapshot.get("name").toString(),
                                                            Integer.parseInt(documentSnapshot.get("quantity").toString()),
                                                            (Boolean) documentSnapshot.get("bought")));
                                                }
                                                Collections.sort(items_array);
                                                adapter = new ItemsListAdapter(getContext(), getActivity(), items_array, new MyAdapterCallback() {
                                                    @Override
                                                    public void actionWhenFinished(Item item) {
                                                        adapter.notifyDataSetChanged();
                                                        updateItemOnDB(item, doc_ref);
                                                    }
                                                });
                                                lv_items_list.setAdapter(adapter);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(parent.getContext(), "onNothingSelected", Toast.LENGTH_LONG).show();
    }

    private void updateItemOnDB(Item item, String doc_ref) {
        Map<String, Object> dataToUpdate = new HashMap<>();
        dataToUpdate.put("bought", item.isBought());
        dataToUpdate.put("quantity", item.getQuantity());
        db.collection("ShoppingLists")
                .document(doc_ref)
                .collection("Items")
                .document(item.getName())
                .update(dataToUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Item updated on db", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void updateSpinner() {
        db.collection("ShoppingLists")
                .whereArrayContains("cont", mail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentReference : task.getResult().getDocuments()) {
                                String name = documentReference.get("list_name").toString();
                                ShoppingList list = new ShoppingList(
                                        documentReference.get("owner").toString(),
                                        documentReference.get("list_name").toString(),
                                        (ArrayList<String>) documentReference.get("cont"),
                                        documentReference.getId()
                                );
                                if (AllListFragment.sharedPrefsDelLists != null && AllListFragment.sharedPrefsDelLists.isDeleted(list))
                                    continue;
                                lists_names.add(name);
                            }
                            ArrayAdapter<String> dataAdapter = null;
                            dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, lists_names);
                            spinner.setAdapter(dataAdapter);
                        }
                    }
                });
    }
}