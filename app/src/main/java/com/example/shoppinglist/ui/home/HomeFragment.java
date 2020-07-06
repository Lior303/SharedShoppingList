package com.example.shoppinglist.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.shoppinglist.LoginActivity;
import com.example.shoppinglist.R;
import com.example.shoppinglist.ui.allLists.AllListFragment;
import com.example.shoppinglist.ui.allLists.SharedPrefsDelLists;
import com.example.shoppinglist.ui.allLists.ShoppingList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private FirebaseFirestore db = LoginActivity.db;
    private Spinner spinner;
    private TextView tv_shopingList_name;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv_shopingList_name= getView().findViewById(R.id.tv_shopingList_name);
        FloatingActionButton fab = getView().findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        spinner = getView().findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        final ArrayList<String> shoppingList = new ArrayList<>();

        db.collection("ShoppingLists")
                .whereArrayContains("cont", LoginActivity.mAuth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentReference: task.getResult().getDocuments()) {
                                String name = documentReference.get("list_name").toString();
                                ShoppingList list = new ShoppingList(
                                        documentReference.get("owner").toString(),
                                        documentReference.get("list_name").toString(),
                                        (ArrayList<String>) documentReference.get("cont"),
                                        documentReference.getId()
                                );
                                if (AllListFragment.sharedPrefsDelLists != null && AllListFragment.sharedPrefsDelLists.isDeleted(list))
                                    continue;
                                shoppingList.add(name);
                            }
                            ArrayAdapter<String> dataAdapter = null;
                            dataAdapter = new ArrayAdapter<>(getActivity() ,android.R.layout.simple_dropdown_item_1line, shoppingList);
                            spinner.setAdapter(dataAdapter);


                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String listName = parent.getItemAtPosition(position).toString();
        tv_shopingList_name.setText("Shopping list "+listName);
        try{
            //todo לקרוא מהרשימה
        }
        catch (Exception e) {}

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(parent.getContext(), "onNothingSelected", Toast.LENGTH_LONG).show();
    }
}