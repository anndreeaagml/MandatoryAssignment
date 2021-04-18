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

    @POST("Messages/{postId}/comments")
    Call<Comment> saveCommentBody(@Path("postId") int postId,@Body Comment comment);

    @DELETE("Messages/{postId}/comments/{id}")
    Call<Comment> deleteComment(@Path("postId") int postId,@Path("id") int id);
}
