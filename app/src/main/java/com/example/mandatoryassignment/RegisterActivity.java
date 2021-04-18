package com.example.mandatoryassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText user;
    private TextInputEditText password;
    private TextInputEditText repeatpassword;
    private TextView errorMessage;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiser);
        user=findViewById(R.id.registerUsername);
        password=findViewById(R.id.registerPassword);
        repeatpassword=findViewById(R.id.repeatPassword);
        errorMessage=findViewById(R.id.errorText);
        mAuth=FirebaseAuth.getInstance();
    }

    public void Register(View v)
    {
        if(password.getText().toString().equals(repeatpassword.getText().toString())) {
            mAuth.createUserWithEmailAndPassword(user.getText().toString(), password.getText().toString())
                    .addOnSuccessListener(this, authResult -> {
                        Log.d("banana", "createUserWIthEmail : success");
                        Toast.makeText(RegisterActivity.this, "Successfully Created User.", Toast.LENGTH_SHORT).show();
                        FirebaseUser u = authResult.getUser();
                        goToLogInPage();

                    }).addOnFailureListener(this, e -> {
                Log.w("banana", "createUserWithEmail : failure", e);
                Toast.makeText(RegisterActivity.this, "Registration Failed.", Toast.LENGTH_SHORT).show();
                errorMessage.setText("Registration error: \n" + e.getMessage());
                errorMessage.setVisibility(View.VISIBLE);
            });
        }
        else
        {
            errorMessage.setText("Passwords do not mach");
            errorMessage.setVisibility(View.VISIBLE);
        }
    }
    public void goToLogInPage()
    {
        Intent gotoLogIn= new Intent(this, MainActivity.class);
        startActivity(gotoLogIn);
    }
}