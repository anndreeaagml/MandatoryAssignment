package com.example.mandatoryassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_post);
        setTitle("Twister");
        TextView Jimmy = (TextView) findViewById(R.id.username_comment);
        Jimmy.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }

    public void Post(View view) {

        String messageText = ((TextInputEditText) findViewById(R.id.text_input)).getText().toString();
        if (messageText.isEmpty())
        {
            Toast.makeText(CreateNewPostActivity.this, "You can't create an empty post",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        MessageService mess= ApiUtils.getMessageService();
        Message newComment = new Message();
        newComment.setContent(messageText);
        newComment.setUser(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        Call<Message> sendMessage= mess.saveMessageBody(newComment);
        sendMessage.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    Message newMessage = response.body();
                    Log.d("banana", newMessage.toString());
                    finish();
                }
                else {
                    String message = "Problem " + response.code() + " " + response.message();
                    Log.d("banana", message);
                }
            }
            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.e("banana", t.getMessage());
            }
        });
    }

    public void Cancel(View view) {
        finish();
    }
}