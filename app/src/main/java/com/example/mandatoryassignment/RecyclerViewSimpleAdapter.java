package com.example.mandatoryassignment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class RecyclerViewSimpleAdapter<T> extends RecyclerView.Adapter<RecyclerViewSimpleAdapter<T>.MyViewHolder> {

    private static final String LOG_TAG = "banana";
    private final List<T> data;
    private OnItemClickListener<T> onItemClickListener;
    private final int viewId = View.generateViewId();
    private final int userId = View.generateViewId();
    private final int commentId = View.generateViewId();
    private final int comComId = View.generateViewId();
    private final int newComId = View.generateViewId();
    private final int addComId = View.generateViewId();

    public RecyclerViewSimpleAdapter(List<T> data) {
        this.data = data;
        Log.d(LOG_TAG, data.toString());

    }

    @NonNull
    @Override
    public RecyclerViewSimpleAdapter<T>.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = makeView(parent.getContext());
        Log.d(LOG_TAG, v.toString());
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    private View makeView(Context context) {
        ViewGroup.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        //main container
        LinearLayout layout = new LinearLayout(context);
        layout.setPadding(30,20,30,20);

        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.VERTICAL);
        //Comment itself
        LinearLayout commentSection = new LinearLayout(context);
        commentSection.setOrientation(LinearLayout.VERTICAL);
        commentSection.setLayoutParams(params);

        TextView username = new TextView(context);
        username.setId(userId);
        username.setLayoutParams(params);
        TextView userComment = new TextView(context);
        userComment.setId(commentId);
        userComment.setLayoutParams(params);
        TextView commentComments = new TextView(context);
        commentComments.setId(comComId);
        commentComments.setLayoutParams(params);

        commentSection.addView(username);
        commentSection.addView(userComment);
        commentSection.addView(commentComments);


        //Add comment part
        LinearLayout addCommentLayout = new LinearLayout(context);
        addCommentLayout.setOrientation(LinearLayout.HORIZONTAL);
        addCommentLayout.setLayoutParams(params);

        TextView newComment = new TextView(context);
        newComment.setId(newComId);
        newComment.setLayoutParams(params);
        newComment.setLayoutParams( new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,1f));
        Button addCmtBtn = new Button(context);
        addCmtBtn.setId(addComId);
        addCmtBtn.setLayoutParams( new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,2f));


        addCommentLayout.addView(newComment);
        addCommentLayout.addView(addCmtBtn);

        //
        layout.addView(commentSection);
        //layout.addView(addCommentLayout);



        /*
        TextView textView = new TextView(context);
        textView.setId(viewId);
        textView.setLayoutParams(params);
        layout.addView(textView);*/

        layout.setId(viewId);

       // Log.d("banana", textView.toString());
        return layout;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message dataItem = (Message) data.get(position);
        Log.d(LOG_TAG, "onBindViewHolder " + data.toString());
        //holder.view.setText(dataItem.getUser());
        ((TextView)holder.itemView.findViewById(userId)).setText(dataItem.getUser());
        ((TextView)holder.itemView.findViewById(commentId)).setText(dataItem.getContent());
        ((TextView)holder.itemView.findViewById(comComId)).setText("Comments: " +dataItem.getTotalComments().toString());
        //((TextView)holder.itemView.findViewById(newComId)).setText("Yo, I'm here");
        Log.d(LOG_TAG, "onBindViewHolder called " + position);
    }

    @Override
    public int getItemCount() {
        int count = data.size();
        Log.d(LOG_TAG, "getItemCount called: " + count);
        return count;
    }

    public Message getItem(int position)
    {
        return (Message) data.get(position);
    }

    void setOnItemClickListener(OnItemClickListener<T> itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, int position, T item);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final LinearLayout view;

        MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            view = itemView.findViewById(viewId);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getAdapterPosition(), data.get(getAdapterPosition()));

            }
        }
    }
}
