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
    @GET("books")
    Call<List<Message>> getAllBooks();

    @GET("books/{bookId}")
    Call<Message> getBookById(@Path("bookId") int bookId);

    @POST("books")
    @FormUrlEncoded
        // I had problems making this work. I used saveBookBody instead
    Call<Message> saveBook(@Field("Author") String author, @Field("Title") String title,
                        @Field("Publisher") String publisher, @Field("Price") double price);

    @POST("books")
    Call<Message> saveBookBody(@Body Book book);

    @DELETE("books/{id}")
    Call<Message> deleteBook(@Path("id") int id);

    @PUT("books/{id}")
    Call<Message> updateBook(@Path("id") int id, @Body Book book);
}
