/* Assignment: 1.1
Campus: Ashdod
Authors:    Mordy dabah, ID: 203507017
            Lior Vaknin, ID: 208574046
            Israel Haddad, ID: 305112005
*/

package com.example.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    public static FirebaseAuth mAuth;
    public static FirebaseFirestore db;

    private EditText ed_email, ed_password;
    private Button bt_login, bt_signUp;
    private ProgressBar progressBar;
    private ImageView eyePass;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        bt_login = findViewById(R.id.bt_login);
        bt_signUp = findViewById(R.id.bt_signup);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        eyePass = findViewById(R.id.eyePassL);

        eyePass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ed_password.setTransformationMethod(null);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ed_password.setTransformationMethod(new PasswordTransformationMethod());
                }
                return true;
            }
        });

        bt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_email.requestFocus();

                //בדיקות של השדות
                if (ed_email.getText().toString().isEmpty()) {
                    ed_email.setError("Email cannot be empty");
                    ed_email.requestFocus();
                    return;
                } else if (!SignUpActivity.isEmailValid(ed_email.getText().toString())) {
                    ed_email.setError("Please enter a valid email");
                    ed_email.requestFocus();
                    return;
                } else if (ed_password.getText().toString().isEmpty()) {
                    ed_password.setError("password cannot be empty");
                    ed_password.requestFocus();
                    return;
                }

                String email = ed_email.getText().toString().trim();
                String password = ed_password.getText().toString().trim();

                progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, "Authentication Successful.",
                                            Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (mAuth.getCurrentUser() != null) {

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }
}