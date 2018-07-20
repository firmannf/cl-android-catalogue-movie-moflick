package com.firmannf.moflick.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by codelabs on 16/07/18.
 */

public class MovieResultModel {
    private List<MovieModel> results;

    public List<MovieModel> getResults() {
        return results;
    }

    public void setResults(List<MovieModel> results) {
        this.results = results;
    }
}
