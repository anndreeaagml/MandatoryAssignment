package com.example.mandatoryassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class TwisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twister);
        int x = getIntent().getIntExtra("PostId",0);
        Log.d("banana", "Post Id = " + x);
    }
}