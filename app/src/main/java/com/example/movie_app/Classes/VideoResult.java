package com.example.movie_app.Classes;

import java.io.Serializable;
import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoResult implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<MovieVideosDetails> results = null;
    private final static long serialVersionUID = 5888783499386903811L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MovieVideosDetails> getResults() {
        return results;
    }

    public void setResults(List<MovieVideosDetails> results) {
        this.results = results;
    }

}
