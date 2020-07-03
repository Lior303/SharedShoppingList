package com.example.shoppinglist.ui.Favorites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shoppinglist.R;
import com.example.shoppinglist.ui.allLists.ShoppingList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavoritesFragment extends Fragment {

    private FavoritesAdapter adapter;
    private ListView lv_favorites;
    private ArrayList<FavoriteContact> arrayList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        lv_favorites = getView().findViewById(R.id.lv_favorites_contacts);
        arrayList = new ArrayList<>();
        arrayList.add(new FavoriteContact("lior@gmail.com", "Lior"));
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
        final EditText editText = new EditText(getContext());
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Add New Favorites Contact")
                .setMessage("Enter favorites contact email and nickname:")
                .setView(editText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String editTextInput = editText.getText().toString();

                        //todo add one fav to shared prefs
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
}