package com.example.topmovies.api;

import com.example.topmovies.model.MovieModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DataService {

    @GET("top_rated?api_key=beb00a4c8c6361b20da92f6abae277d0")
    Call<List<MovieModel>> loadInfoTop20Movies();

    @GET("/{id}?api_key=beb00a4c8c6361b20da92f6abae277d0")
    Call<MovieModel> loadInfoMovie(@Path("id") String id);
}
