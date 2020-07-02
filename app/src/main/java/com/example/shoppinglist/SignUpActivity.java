package com.example.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText ed_email, ed_password;
    private Button bt_signup;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth = LoginActivity.mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ed_email = findViewById(R.id.ed_signup_email);
        ed_password = findViewById(R.id.ed_signup_password);
        bt_signup = findViewById(R.id.bt_signup_act);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);

        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ed_email.getText().toString();
                String password = ed_password.getText().toString();

                // check fields

                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    progressBar.setVisibility(View.GONE);
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(SignUpActivity.this, "Authentication Successful.",
                                            Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}