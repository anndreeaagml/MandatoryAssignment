package com.example.mandatoryassignment;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimelineActivity extends AppCompatActivity {

    private RecyclerViewSimpleAdapter twisterAdapter;
    private Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Twister");
        //getAndShowPosts();
        FloatingActionButton fab = findViewById(R.id.fab);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent go_to_new_post = new Intent(TimelineActivity.this, CreateNewPostActivity.class);
                    go_to_new_post.putExtra("username", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    startActivity(go_to_new_post);
                }
            });
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Your logic here...
                getAndShowPosts();
                // When you need to modify a UI element, do so on the UI thread.
                // 'getActivity()' is required as this is being ran from a Fragment.

            }
        }, 0, 3000);

    }

    @Override
    protected void onPause() {
        timer.cancel();
        super.onPause();
    }

    public void getAndShowPosts() {
        MessageService mess = ApiUtils.getMessageService();
        Call<List<Message>> getAllMessagesCall = mess.getAllMessages();
        getAllMessagesCall.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful()) {
                    List<Message> allMessages = response.body();
                    populateRecyclerView(allMessages);
                    Log.d("update banana ", allMessages.toString());
                } else {
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

    private void deleteComment(int position) {
        MessageService mess = ApiUtils.getMessageService();
        Call<Message> deleteMessage = mess.deleteMessage(position);
        deleteMessage.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    Message delMsg = response.body();
                    getAndShowPosts();
                    Log.d("banana", "Deleted " + delMsg.toString());
                } else {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            menu.findItem(R.id.sign_out_button).setTitle("Log In");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_button:
                //Sign out here
                if (FirebaseAuth.getInstance().getCurrentUser() == null)
                    finish();
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
        twisterAdapter = new RecyclerViewSimpleAdapter<>(allMessages);
        recyclerView.setAdapter(twisterAdapter);
        twisterAdapter.setOnItemClickListener((view, position, item) -> {
            Message msg = (Message) item;
            Log.d("banana", item.toString());
            Message mess = ((RecyclerViewSimpleAdapter) recyclerView.getAdapter()).getItem(position);
            Intent GoToPost = new Intent(this, ViewPostActivity.class);
            GoToPost.putExtra("Message", (Serializable) mess);
            startActivity(GoToPost);
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                if (FirebaseAuth.getInstance().getCurrentUser() == null)
                    return;
                final int position = viewHolder.getAdapterPosition();
                if (position >= 0) {
                    int id = ((RecyclerViewSimpleAdapter) recyclerView.getAdapter()).getItem(position).getId();
                    String user = ((RecyclerViewSimpleAdapter) recyclerView.getAdapter()).getItem(position).getUser();
                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(user)) {
                        deleteComment(id);
                    }
                }
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // view the background view
                if (FirebaseAuth.getInstance().getCurrentUser() == null)
                    return;
                final int position = viewHolder.getAdapterPosition();
                if (position >= 0) {
                    String user = twisterAdapter.getItem(position).getUser();
                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(user)) {

                        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                                .addSwipeLeftBackgroundColor(ContextCompat.getColor(TimelineActivity.this, R.color.design_default_color_error))
                                .addActionIcon(R.drawable.ic_delete)
                                .create()
                                .decorate();
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }

                }
            }
        };
        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }
}