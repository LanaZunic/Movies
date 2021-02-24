package com.example.movies.model;

import com.google.gson.annotations.SerializedName;

public class Generes {
    @SerializedName("id")
    private int movieId;
    @SerializedName("name")
    private String genereName;

    public int getMovieId() {
        return movieId;
    }

    public String getGenereName() {
        return genereName;
    }

}
