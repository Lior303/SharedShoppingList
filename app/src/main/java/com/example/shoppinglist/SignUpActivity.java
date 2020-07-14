/* Assignment: 1.1
Campus: Ashdod
Authors:    Mordy dabah, ID: 203507017
            Lior Vaknin, ID: 208574046
            Israel Haddad, ID: 305112005
*/

package com.example.shoppinglist;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText ed_email, ed_password;
    private Button bt_signup;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth = LoginActivity.mAuth;
    private ImageView eyePass;

    // check validity of email address
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}[ ]*$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ed_email = findViewById(R.id.ed_signup_email);
        ed_password = findViewById(R.id.ed_signup_password);
        bt_signup = findViewById(R.id.bt_signup_act);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        eyePass = findViewById(R.id.eyePassS);

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
        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ed_email.requestFocus();

                // check fields
                if (ed_email.getText().toString().isEmpty()) {
                    ed_email.setError("Email cannot be empty");
                    ed_email.requestFocus();
                    return;
                } else if (!isEmailValid(ed_email.getText().toString())) {
                    ed_email.setError("Please enter a valid email");
                    ed_email.requestFocus();
                    return;
                } else if (ed_password.getText().toString().isEmpty()) {
                    ed_password.setError("Password cannot be empty");
                    ed_password.requestFocus();
                    return;
                }
                else if (ed_password.length() < 6) {
                    ed_password.setError("Password length must be at least 6 characters");
                    ed_password.requestFocus();
                    return;
                }
                else if (ed_password.length() > 12) {
                    ed_password.setError("Password length must be less than or equal to 12 characters");
                    ed_password.requestFocus();
                    return;
                }

                String email = ed_email.getText().toString().trim();
                String password = ed_password.getText().toString().trim();

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
                                            Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}