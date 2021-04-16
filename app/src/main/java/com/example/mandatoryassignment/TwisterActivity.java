package com.example.mandatoryassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TwisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twister);
        Intent intent = getIntent();
        Message message = (Message) intent.getSerializableExtra("Message");
        Log.d("banana", "Post Id = " + message.getId());
        TextView Jimmy = (TextView) findViewById(R.id.user);
        TextView content = (TextView) findViewById(R.id.content);
        TextView comm = (TextView) findViewById(R.id.totalComments);
        content.setText(message.getContent());
        getAndShowComments(message.getId());
        comm.setText(message.getTotalComments() + " Comments");
        Jimmy.setText(message.getUser());
    }
    public void getAndShowComments(Integer id) {
        CommentsService mess = ApiUtils.getCommentsService(id);
        Call<List<Comment>> getAllCommentsCall = mess.getAllComments();
        getAllCommentsCall.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    List<Comment> allComments = response.body();
                    populateRecyclerView(allComments);
                    Log.d("comments", allComments.toString());
                } else {
                    String message = "Problem " + response.code() + " " + response.message();
                    Log.d("comments", message);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.e("comments", t.getMessage());
            }
        });
    }
    private void populateRecyclerView(List<Comment> allComments) {
        RecyclerView recyclerView = findViewById(R.id.listOfComments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewCommentsAdapter<Comment> adapter = new RecyclerViewCommentsAdapter<>(allComments);
        recyclerView.setAdapter(adapter);
        /*
        adapter.setOnItemClickListener((view, position, item) -> {
            Comment comm = (Comment) item;
            Log.d("banana", item.toString());
            Message mess = ((RecyclerViewCommentsAdapter) recyclerView.getAdapter()).getItem(position);
            Intent GoToPost = new Intent(this, TwisterActivity.class);
            GoToPost.putExtra("Com", (Serializable) mess);
            startActivity(GoToPost);
        });
         */
    }
}