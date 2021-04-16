package com.example.mandatoryassignment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentsService {
    @GET("Messages/{postId}/comments")
    Call<List<Comment>> getAllComments(@Path("postId") int postId);

    @GET("comments/{commentId}")
    Call<Comment> getCommentById(@Path("commentId") int commentId);


    @POST("comments")
    Call<Comment> saveCommentBody(@Body Comment comment);

    @DELETE("comments/{id}")
    Call<Comment> deleteMessage(@Path("id") int id);
}
