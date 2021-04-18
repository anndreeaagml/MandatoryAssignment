package com.example.mandatoryassignment;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Comment implements Serializable{
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("messageId")
    @Expose
    private int messageId;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("user")
    @Expose
    private String user;
    public Comment(){}

    public Comment(int id, int messageId, String content, String user)
    {
        this.id= id;
        this.messageId=messageId;
        this.content=content;
        this.user=user;

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) { this.messageId = messageId; }

    @NonNull
    @Override
    public String toString() {
        return id + ": " +messageId+" " + content + " " + user + ", " ;
    }


}
