package com.example.shoppinglist.ui.SignOut;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.shoppinglist.LoginActivity;
import com.example.shoppinglist.MainActivity;
import com.example.shoppinglist.R;

public class SignOutFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LoginActivity.mAuth.signOut();
        Toast.makeText(getContext(), "Signing Out, Goodbye!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        return inflater.inflate(R.layout.fragment_exit, container, false);
    }

}
