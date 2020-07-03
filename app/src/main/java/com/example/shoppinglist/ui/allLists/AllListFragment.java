package com.example.shoppinglist.ui.allLists;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shoppinglist.LoginActivity;
import com.example.shoppinglist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllListFragment extends Fragment {

    private static ListView lv_all_lists;
    private static ListAdapter adapter;
    private static ArrayList<ShoppingList> shoppingList;
    private FirebaseFirestore db = LoginActivity.db;
    private static final String mail = LoginActivity.mAuth.getCurrentUser().getEmail();
    private ProgressBar progressBar;

    public static SharedPrefsDelLists sharedPrefsDelLists;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_list, container, false);
    }

    public static void refreshData(ShoppingList ls) {
        shoppingList.remove(ls);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        lv_all_lists = getView().findViewById(R.id.lv_all_lists);
        shoppingList = new ArrayList<>();
        progressBar = getView().findViewById(R.id.progressBar3);
        sharedPrefsDelLists = new SharedPrefsDelLists(getActivity());

        readData(new MyShoppingListsCallback() {
            @Override
            public void actionWhenFinished() {
                progressBar.setVisibility(View.GONE);
                lv_all_lists.setVisibility(View.VISIBLE);
                adapter = new ListAdapter(getContext(), getActivity(), shoppingList);
                lv_all_lists.setAdapter(adapter);
            }
        });
    }

    public interface MyShoppingListsCallback {
        void actionWhenFinished();
    }

    private void readData(final MyShoppingListsCallback callback) {
        db.collection("ShoppingLists")
                .whereArrayContains("cont", mail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentReference: task.getResult().getDocuments()) {
                                ShoppingList list = new ShoppingList(
                                        documentReference.get("owner").toString(),
                                        documentReference.get("list_name").toString(),
                                        (ArrayList<String>) documentReference.get("cont"),
                                        documentReference.getId()
                                );
                                if (sharedPrefsDelLists.isDeleted(list))
                                    continue;
                                shoppingList.add(list);
                            }
                            callback.actionWhenFinished();
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addList:
                addNewList();
                break;
        }
        return true;
    }

    private void addNewList() {
        final EditText editText = new EditText(getContext());
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Add New List")
                .setMessage("Enter list name:")
                .setView(editText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String editTextInput = editText.getText().toString();
                        Map<String, Object> new_list = new HashMap<>();
                        new_list.put("list_name", editTextInput);
                        new_list.put("owner", mail);
                        //contributes
                        ArrayList<String> cont = new ArrayList<>();
                        cont.add(mail);
                        new_list.put("cont", cont);

                        final ShoppingList new_list_obj = new ShoppingList(mail, editTextInput, cont, "");

                        db.collection("ShoppingLists")
                                .add(new_list)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()) {
                                            new_list_obj.setDoc_id(task.getResult().getId());
                                            shoppingList.add(new_list_obj);
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(getContext(), "New list added successfully", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
}