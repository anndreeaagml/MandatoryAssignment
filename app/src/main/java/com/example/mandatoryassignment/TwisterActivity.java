package com.example.mandatoryassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TwisterActivity extends AppCompatActivity {

    private int messId;
    private RecyclerViewCommentsAdapter commentsAdapter;

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
        messId=message.getId();
        comm.setText(message.getTotalComments() + " Comments");
        Jimmy.setText(message.getUser());
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            findViewById(R.id.postComment).setVisibility(View.VISIBLE);
        }
    }
    public void getAndShowComments(Integer id) {
        CommentsService mess = ApiUtils.getCommentsService();
        Call<List<Comment>> getAllCommentsCall = mess.getAllComments(id);
        getAllCommentsCall.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    List<Comment> allComments = response.body();
                    Log.d("comments", allComments.toString());
                    populateRecyclerView(allComments);

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
        commentsAdapter = new RecyclerViewCommentsAdapter<>(allComments);
        recyclerView.setAdapter(commentsAdapter);
        commentsAdapter.setOnItemClickListener((view, position, item) -> {
            Comment comm = (Comment) item;
            Log.d("getcomm", item.toString());
            Comment mess = ((RecyclerViewCommentsAdapter) recyclerView.getAdapter()).getItem(position);
            //Intent GoToPost = new Intent(this, TwisterActivity.class);
            //GoToPost.putExtra("Com", (Serializable) mess);
            //startActivity(GoToPost);
        });
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                if (FirebaseAuth.getInstance().getCurrentUser()==null)
                    return;
                final int position = viewHolder.getAdapterPosition();
                if (position >= 0) {
                    int id = ((RecyclerViewCommentsAdapter)recyclerView.getAdapter()).getItem(position).getId();
                    String user = ((RecyclerViewCommentsAdapter)recyclerView.getAdapter()).getItem(position).getUser();
                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(user)) {
                        deleteComment(id);
                    }
                }
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // view the background view
                if (FirebaseAuth.getInstance().getCurrentUser()==null)
                    return;
                final int position = viewHolder.getAdapterPosition();
                if (position >= 0) {
                    String user = commentsAdapter.getItem(position).getUser();
                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(user)) {

                        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                                .addSwipeLeftBackgroundColor(ContextCompat.getColor(TwisterActivity.this, R.color.design_default_color_error))
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
    private void deleteComment(int position) {
        CommentsService mess = ApiUtils.getCommentsService();
        Call<Comment> deleteComment = mess.deleteComment(messId,position);
        deleteComment.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    Comment delCom = response.body();
                    getAndShowComments(delCom.getMessageId());
                    Log.d("comment","Deleted " + delCom.toString());
                } else {
                    String message = "Problem " + response.code() + " " + response.message();
                    Log.d("banana", message);
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.e("banana", t.getMessage());
            }
        });
    }
    public void PostComment(View view) {
        TextInputEditText input=findViewById(R.id.newComment);
        String commentText = input.getText().toString();
        if (commentText.isEmpty())
        {
            Toast.makeText(TwisterActivity.this, "Haha. No.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        CommentsService com= ApiUtils.getCommentsService();
        Comment newComment = new Comment();
        newComment.setContent(commentText);
        newComment.setUser(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        newComment.setMessageId(messId);
        //newComment.setId(0);

        Call<Comment> sendComment= com.saveCommentBody(messId,newComment);
        sendComment.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    Comment newComment = response.body();
                    Log.d("banana", newComment.toString());
                    getAndShowComments(messId);
                    input.getText().clear();
                }
                else {
                    String message = "Problem " + response.code() + " " + response.message();
                    Log.d("banana", message);
                }
            }
            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.e("banana", t.getMessage());
            }
        });
    }
}