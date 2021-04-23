package com.example.mandatoryassignment;

import android.content.Context;
import android.content.Intent;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
        LinearLayout layoutmegamain = new LinearLayout(context);

        LinearLayout layout = new LinearLayout(context);
        layoutmegamain.setPadding(10,10,10,10);
        layout.setPadding(10,10,10,10);
        layoutmegamain.setLayoutParams(params);
        layout.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_corners));
        layoutmegamain.setBackgroundColor(ContextCompat.getColor(context,R.color.ToolbarColor));
        layout.setClipToOutline(true);
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.VERTICAL);
        //Comment itself
        LinearLayout commentSection = new LinearLayout(context);
        commentSection.setOrientation(LinearLayout.VERTICAL);
        commentSection.setLayoutParams(params);

        TextView username = new TextView(context);
        username.setTextSize(18);
        username.setId(userId);
        username.setLayoutParams(params);
        username.setTextColor(ContextCompat.getColor(context,R.color.userTextColor));

        TextView userComment = new TextView(context);
        userComment.setId(commentId);
        userComment.setLayoutParams(params);
        userComment.setTextColor(ContextCompat.getColor(context,R.color.PostColor));

        TextView commentComments = new TextView(context);
        commentComments.setId(comComId);
        commentComments.setLayoutParams(params);
        commentComments.setTextColor(ContextCompat.getColor(context,R.color.CommentColor));
        commentComments.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        commentComments.setTextSize(12);

        commentSection.addView(username);
        commentSection.addView(userComment);
        commentSection.addView(commentComments);

        layout.addView(commentSection);
        layout.setId(viewId);
        layoutmegamain.addView(layout);

       // Log.d("banana", textView.toString());
        return layoutmegamain;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message dataItem = (Message) data.get(position);
        Log.d(LOG_TAG, "onBindViewHolder " + data.toString());
        //holder.view.setText(dataItem.getUser());
        ((TextView)holder.itemView.findViewById(userId)).setText(dataItem.getUser());
        ((TextView)holder.itemView.findViewById(commentId)).setText(dataItem.getContent());
        ((TextView)holder.itemView.findViewById(comComId)).setText("Comments: " +dataItem.getTotalComments().toString());
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
