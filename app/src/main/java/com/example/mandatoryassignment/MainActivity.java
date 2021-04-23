package com.example.mandatoryassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "banana";
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        error=findViewById(R.id.errorMessage);
        setTitle("Twister");
        findViewById(R.id.goToRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToRegister = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(goToRegister);
            }
        });
        findViewById(R.id.goToPasswordReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToPassword = new Intent(MainActivity.this, PasswordActivity.class);
                startActivity(goToPassword);
            }
        });
        findViewById(R.id.anonymous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToTimeline = new Intent(MainActivity.this, TimelineActivity.class);
                startActivity(goToTimeline);
            }
        });
    }

    public void LogIn(View view) {

        String username = ((TextInputEditText) findViewById(R.id.username)).getText().toString();

        String password = ((TextInputEditText) findViewById(R.id.password)).getText().toString();
        if (password.isEmpty() || username.isEmpty()) {
            error.setText("Please enter the username and password");
            error.setVisibility(View.VISIBLE);
        } else {
            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                error.setText("Wrong username/password");
                                error.setVisibility(View.VISIBLE);
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }
    }


    private void updateUI(FirebaseUser user) {
        Intent gotoFeed = new Intent(this, TimelineActivity.class);
        gotoFeed.putExtra("EMAIL", user.getEmail());
        startActivity(gotoFeed);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            updateUI(currentUser);
    }
}

