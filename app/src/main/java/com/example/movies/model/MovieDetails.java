package com.example.movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieDetails {
    @SerializedName("genres")
    private List<Generes> generesList;

    public List<Generes> getGeneresList() {
        return generesList;
    }

}
