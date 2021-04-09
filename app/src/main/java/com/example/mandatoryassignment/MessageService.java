package com.example.mandatoryassignment;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MessageService {
    @GET("Messages")
    Call<List<Message>> getAllMessages();

    @GET("Messages/{bookId}")
    Call<Message> getMessageById(@Path("bookId") int bookId);


    @POST("Messages")
    Call<Message> saveMessageBody(@Body Message message);

    @DELETE("Messages/{id}")
    Call<Message> deleteMessage(@Path("id") int id);


}
