package com.example.mandatoryassignment;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class timeline extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Twister");
        getAndShowPosts();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent go_to_new_post= new Intent(timeline.this, CreateNewPostActivity.class);
               go_to_new_post.putExtra("username", FirebaseAuth.getInstance().getCurrentUser().getEmail());
               startActivity(go_to_new_post);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getAndShowPosts();
    }

    public void getAndShowPosts()
    {
        MessageService mess= ApiUtils.getMessageService();
        Call<List<Message>> getAllMessagesCall= mess.getAllMessages();
        getAllMessagesCall.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful()) {
                    List<Message> allMessages= response.body();
                    populateRecyclerView(allMessages);
                    Log.d("banana", allMessages.toString());
                }
                else {
                    String message = "Problem " + response.code() + " " + response.message();
                    Log.d("banana", message);
                }
            }
            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.e("banana", t.getMessage());
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_button:
                //Sign out here TODO
                FirebaseAuth.getInstance().signOut();
                finish();
                return true; // true: menu processing done, no further actions
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void populateRecyclerView(List<Message> allMessages) {
        RecyclerView recyclerView = findViewById(R.id.mainRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewSimpleAdapter<Message> adapter = new RecyclerViewSimpleAdapter<>(allMessages);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, position, item) -> {
            Message msg = (Message) item;
            Log.d("banana", item.toString());
           // Intent intent = new Intent(MainActivity.this, SingleBookActivity.class);
          //  intent.putExtra(SingleBookActivity.BOOK, book);
          //  Log.d(LOG_TAG, "putExtra " + book.toString());
         //  startActivity(intent);
        });
    }
}