package com.example.movies.movieapi;

import com.example.movies.model.MovieDetails;
import com.example.movies.model.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("movie/popular")
    Call<Movies> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int pageIndex);

    @GET("movie/top_rated")
    Call<Movies> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int pageIndex);

    @GET("movie/{id}")
    Call<MovieDetails> getMovieDetails(@Path("id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/similar")
    Call<Movies> getSimilarMovies(@Path("id") int movieId, @Query("api_key") String apiKey);
}
