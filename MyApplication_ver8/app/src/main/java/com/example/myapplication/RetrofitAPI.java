package com.example.myapplication;

import com.example.myapplication.DataClass.Gamsang;
import com.example.myapplication.DataClass.ReadDays;
import com.example.myapplication.DataClass.Taste;
import com.example.myapplication.DataClass.TestItem;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitAPI {

    @GET("/v3/search/book?target=title")
    Call<TestItem> getBookInfo(
            @Header("Authorization") String token,
            @Query("query") String title,
            @Query("page") int page,
            @Query("size") int size
    );

    @FormUrlEncoded
    @POST("BookList_Insert.php")
    Call<ResponseBody> insertBookInfo(
            @Field("btitle") String btitle,
            @Field("bauthor") String bauthor,
            @Field("byear") String byear,
            @Field("bimage") String bimage,
            @Field("uid") String uid,
            @Field("date") String date
    );

    @FormUrlEncoded
    @POST("ReadList_Select.php")
    Call<Gamsang> getReadInfo(
      @Field("uid") String uid,
      @Field("rdate") String rdate
    );

    @FormUrlEncoded
    @POST("Emotion_Analysis")
    Call<ResponseBody> emotionAnalysis(
            @Field("title") String title,
            @Field("content") String content,
            @Field("uid") String uid
    );

    @FormUrlEncoded
    @POST("Similarity_Analysis")
    Call<Taste> similarityAnalysis(
            @Field("btitle") String btitle,
            @Field("authors") String authors,
            @Field("thumbnail") String thumbnail
    );

    @FormUrlEncoded
    @POST("ReadDays_Select")
    Call<ReadDays> getReadDays(
            @Field("uid") String uid
    );
}
