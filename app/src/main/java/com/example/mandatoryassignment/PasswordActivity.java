package com.example.mandatoryassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
    }
    public void SendMail(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        TextInputEditText email= findViewById(R.id.text_email);
        auth.sendPasswordResetEmail(email.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("email", "Email sent.");
                    }
                });
        finish();

    }
    public void Cancel(View view) {
        finish();
    }
}