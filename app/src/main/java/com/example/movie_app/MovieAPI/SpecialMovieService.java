package com.example.movie_app.MovieAPI;

import com.example.movie_app.Classes.MovieDetailsClass;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpecialMovieService {

    @GET("3/movie/{id}")
    Call<MovieDetailsClass> movieService(
            @Path("id") String id,
            @Query("api_key") String api_key,
            @Query("language") String language
    );
}
