package com.example.movie_app.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "FilmDetails")
public class FilmDetails {
    @PrimaryKey
    private Integer fid;

    @ColumnInfo(name = "vote_average")
    public Double voteAverage;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name="release_date")
    public String release_date;

    public Integer getFid() {
        return fid;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }
}
