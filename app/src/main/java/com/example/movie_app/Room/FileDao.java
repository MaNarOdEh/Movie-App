package com.example.movie_app.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FileDao {

    @Query("SELECT * FROM FilmDetails")
    List<FilmDetails> getAll();

    @Query("SELECT fid FROM FilmDetails")
    List<Integer> getALLDID();

    @Query("SELECT * FROM FilmDetails WHERE fid IN (:userIds)")
    List<FilmDetails> loadAllByIds(Integer[] userIds);

    @Insert
    void insertAll(FilmDetails... users);

    @Insert
    void inserUser(FilmDetails filmDetails);

    @Delete
    void delete(FilmDetails user);

    @Query("SELECT * FROM FilmDetails WHERE  fid = :taskId")
    FilmDetails getTask(Integer taskId);

    @Query("Delete FROM FilmDetails WHERE  fid = :taskId")
    void deleteTask(Integer taskId);
}

