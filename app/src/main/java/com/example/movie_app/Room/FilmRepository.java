package com.example.movie_app.Room;

import android.content.Context;
import android.os.AsyncTask;
import androidx.room.Room;

import java.util.List;

public class FilmRepository {
    private String DB_NAME = "db_task";
    private AppDatabase appDatabase;


    public FilmRepository(Context context) {
         appDatabase = Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                .allowMainThreadQueries()   //Allows room to do operation on main thread
                .build();
    }


    public void insertTask(Integer fid,String title, Double voteAverage,String release_date) {
        FilmDetails filmDetails = new FilmDetails();
        filmDetails.setFid(fid);
        filmDetails.setTitle(title);
        filmDetails.setVoteAverage(voteAverage);
        filmDetails.setRelease_date(release_date);
        addFilm(filmDetails);
    }
    public void insertTask(final FilmDetails note) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.fimlDao().insertAll(note);
                return null;
            }
        }.execute();
    }

   public void deleteTask(final Integer id) {

        new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    appDatabase.fimlDao().deleteTask(id);
                    return null;
                }
            }.execute();

    }
    public void addFilm(final FilmDetails filmDetails){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.fimlDao().inserUser(filmDetails);
                return null;
            }
        }.execute();

    }

    public FilmDetails getTask(final Integer id) {
        return appDatabase.fimlDao().getTask(id);

    }

    public List<FilmDetails> getTasks() {
        return appDatabase.fimlDao().getAll();
    }
    public List<Integer>getALLMovieId(){
        return appDatabase.fimlDao().getALLDID();

    }

}
