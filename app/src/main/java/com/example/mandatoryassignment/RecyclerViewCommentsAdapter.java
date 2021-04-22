package com.example.mandatoryassignment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class RecyclerViewCommentsAdapter<T> extends RecyclerView.Adapter<RecyclerViewCommentsAdapter<T>.MyViewHolder> {

    private static final String LOG_TAG = "banana";
    private final List<T> data;
    private RecyclerViewCommentsAdapter.OnItemClickListener<T> onItemClickListener;
    private final int viewId = View.generateViewId();
    private final int userId = View.generateViewId();
    private final int commentId = View.generateViewId();
    private final int comComId = View.generateViewId();
    private final int newComId = View.generateViewId();
    private final int addComId = View.generateViewId();

    public RecyclerViewCommentsAdapter(List<T> data) {
        this.data = data;
        Log.d(LOG_TAG, data.toString());

    }

    @NonNull
    @Override
    public RecyclerViewCommentsAdapter<T>.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = makeView(parent.getContext());
        Log.d(LOG_TAG, v.toString());
        RecyclerViewCommentsAdapter.MyViewHolder vh = new RecyclerViewCommentsAdapter.MyViewHolder(v);
        return vh;
    }

    private View makeView(Context context) {
        ViewGroup.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        //main container
        LinearLayout layoutmegamain= new LinearLayout(context);

        LinearLayout layout = new LinearLayout(context);
        layoutmegamain.setPadding(10,10,10,10);
        layout.setPadding(10,10,10,10);
        layoutmegamain.setLayoutParams(params);
        layoutmegamain.setBackgroundColor(Color.argb(255,40,40,40));
        layout.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_corners));
        layout.setClipToOutline(true);
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.VERTICAL);
        //Comment itself
        LinearLayout commentSection = new LinearLayout(context);
        commentSection.setOrientation(LinearLayout.VERTICAL);
        commentSection.setLayoutParams(params);

        TextView username = new TextView(context);
        username.setTextSize(16);
        username.setId(userId);
        username.setLayoutParams(params);
        username.setTextColor(Color.argb(255,120,120,255));

        TextView userComment = new TextView(context);
        userComment.setId(commentId);
        userComment.setLayoutParams(params);
        userComment.setTextColor(Color.argb(255,255,255,255));

        commentSection.addView(username);
        commentSection.addView(userComment);

        layout.addView(commentSection);

        layout.setId(viewId);
        layoutmegamain.addView(layout);
        return layoutmegamain;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewCommentsAdapter.MyViewHolder holder, int position) {
        Comment dataItem = (Comment) data.get(position);
        Log.d(LOG_TAG, "onBindViewHolder " + data.toString());
        ((TextView) holder.itemView.findViewById(userId)).setText(dataItem.getUser());
        ((TextView) holder.itemView.findViewById(commentId)).setText(dataItem.getContent());
        Log.d(LOG_TAG, "onBindViewHolder called " + position);
    }

    @Override
    public int getItemCount() {
        int count = data.size();
        Log.d(LOG_TAG, "getItemCount called: " + count);
        return count;
    }

    public Comment getItem(int position) {
        return (Comment) data.get(position);
    }

    void setOnItemClickListener(RecyclerViewCommentsAdapter.OnItemClickListener<T> itemClickListener) {
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

