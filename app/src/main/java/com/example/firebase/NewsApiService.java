package com.example.firebase;

import com.example.firebase.ModelClass.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {
    @GET("v2/top-headlines")
    Call<NewsResponse> getSportsNews(
            @Query("category") String category,
            @Query("apiKey") String apiKey
    );
}
