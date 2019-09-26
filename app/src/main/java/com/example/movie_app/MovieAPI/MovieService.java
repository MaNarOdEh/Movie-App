package com.example.movie_app.MovieAPI;

import com.example.movie_app.Classes.MovieResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    //this intefrace for making call and determines the attributes for calling
    @GET("3/movie/{category}")
    Call<MovieResult> movieService(
            @Path("category") String category,
            @Query("api_key") String api_key,
            @Query("language") String language
    );

}
