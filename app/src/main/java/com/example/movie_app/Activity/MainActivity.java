package com.example.movie_app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.movie_app.Classes.MovieResult;
import com.example.movie_app.MovieAPI.FetchDataPublicAccess;
import com.example.movie_app.MovieAPI.MovieService;
import com.example.movie_app.Classes.MovieDetailsClass;

import com.example.movie_app.MovieAPI.SpecialMovieService;
import com.example.movie_app.R;
import com.example.movie_app.Recycle.MovieAdapter;
import com.example.movie_app.Room.FilmRepository;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.movie_rv)@Nullable  RecyclerView movieRv;
    @BindView(R.id.interet_connection) @Nullable   LinearLayout interet_connection;
    @BindView(R.id.progress_circular) @Nullable   LinearLayout progress_circular;
    private MovieAdapter movieAdapter;
    private ArrayList<MovieDetailsClass> movieObjects=new ArrayList<MovieDetailsClass>();
    private static  String SORT_BY="popular";
    private  Dialog dialog_confirm;
    private  static final String PARC="video_details";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState == null){
            movieObjects = new ArrayList<MovieDetailsClass>();
            fetchMovie();
            // populate books from remote server or local json
        }else{
            movieObjects = savedInstanceState.getParcelableArrayList(PARC);
            if(movieObjects==null){
                fetchMovie();
            }
        }

        initializeAll();
        makeNecessaryEvent();
    }

    private void initializeAll() {
        movieAdapter=new MovieAdapter(movieObjects);
        movieRv.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
        movieRv.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
        dialog_confirm  = new Dialog(MainActivity.this);
        dialog_confirm.setCancelable(false);
        dialog_confirm.setContentView(R.layout.confirm_upload);
    }

    private void makeNecessaryEvent() {
        dialog_confirm.findViewById(R.id.btn_confirm_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchMovie();
                dimmissDialog();
            }
        });
        dialog_confirm.findViewById(R.id.btn_cancel_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dimmissDialog();
            }
        });
    }



    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    private void fetchMovie() {
        hideShowProgressBar(1);
        movieRv.setVisibility(View.GONE);
        if (isOnline()) {

            //Generate  the service && connect to the url
            interet_connection.setVisibility(View.GONE);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(FetchDataPublicAccess.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieService servicesInterface = retrofit.create(MovieService.class);
            Call<MovieResult> call = servicesInterface.movieService(SORT_BY, FetchDataPublicAccess.API_KEY, FetchDataPublicAccess.LANGUGE);
            call.enqueue(new Callback<MovieResult>() {
                @Override
                public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                      MovieResult movieResult=response.body();
                      if(movieResult!=null&&movieResult.getResults()!=null) {
                          movieObjects = (ArrayList<MovieDetailsClass>) movieResult.getResults();
                              movieRv.setVisibility(View.VISIBLE);
                              movieAdapter = new MovieAdapter(movieObjects);
                              movieRv.setAdapter(movieAdapter);
                              hideShowProgressBar(0);

                      }

                }

                @Override
                public void onFailure(Call<MovieResult> call, Throwable t) {
                   // Toast.makeText(MainActivity.this, t.getMessage()+"    "+t.toString(), Toast.LENGTH_LONG).show();

                }
            });

        }else{
            interet_connection.setVisibility(View.VISIBLE);
           Snackbar snackbar = Snackbar
                    .make(interet_connection, "You are not connected to the wifi", Snackbar.LENGTH_LONG);
            snackbar.show();
            hideShowProgressBar(0);
        }
    }
    private  void hideShowProgressBar(int i){
        switch (i){
            case 1:progress_circular.setVisibility(View.VISIBLE);break;
            case 0:progress_circular.setVisibility(View.GONE);break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.most_popular) {
            dialog_confirm.show();
            SORT_BY= FetchDataPublicAccess.SORTTYPEPOPULARITY;
            return true;
        }else if(id==R.id.most_rating){
            dialog_confirm.show();
            SORT_BY= FetchDataPublicAccess.SORTBYRATING;
        }else if(id==R.id.make_fav_btn){
            movieObjects=new ArrayList<>();
             List<Integer> favID=new FilmRepository(getApplicationContext()).getALLMovieId();
            hideShowProgressBar(1);
             for(int i=0;i<favID.size();i++) {
                 if (isOnline()) {
                     hideShowProgressBar(0);
                     Retrofit retrofit = new Retrofit.Builder()
                             .baseUrl(FetchDataPublicAccess.BASE_URL)
                             .addConverterFactory(GsonConverterFactory.create())
                             .build();
                     SpecialMovieService servicesInterface = retrofit.create(SpecialMovieService.class);
                     Call<MovieDetailsClass> call = servicesInterface.movieService(favID.get(i) + "", FetchDataPublicAccess.API_KEY, FetchDataPublicAccess.LANGUGE);
                     call.enqueue(new Callback<MovieDetailsClass>() {
                         @Override
                         public void onResponse(Call<MovieDetailsClass> call, Response<MovieDetailsClass> response) {
                             if (response != null && response.body() != null) {
                                 MovieDetailsClass movieDetailsClass = response.body();
                                 movieObjects.add(movieDetailsClass);
                                 movieAdapter=new MovieAdapter(movieObjects);
                                 movieRv.setAdapter(movieAdapter);
                             }
                         }

                         @Override
                         public void onFailure(Call<MovieDetailsClass> call, Throwable t) {
                         }
                     });
                 }else{
                     interet_connection.setVisibility(View.VISIBLE);
                     Snackbar snackbar = Snackbar
                             .make(interet_connection, "You are not connected to the wifi", Snackbar.LENGTH_LONG);
                     snackbar.show();
                     hideShowProgressBar(0);
                 }
             }

        }
        return super.onOptionsItemSelected(item);
    }
/*
    private void fetchMovieData(String id) {
        if (isOnline()) {
            //Generate  the service && connect to the url
            interet_connection.setVisibility(View.GONE);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(FetchDataPublicAccess.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            SpecialMovieService servicesInterface = retrofit.create(SpecialMovieService.class);
            Call<MovieDetailsClass> call=servicesInterface.movieService(id,FetchDataPublicAccess.API_KEY,FetchDataPublicAccess.LANGUGE);
            call.enqueue(new Callback<MovieDetailsClass>() {
                @Override
                public void onResponse(Call<MovieDetailsClass> call, Response<MovieDetailsClass> response) {
                    if(response!=null&&response.body()!=null){
                        MovieDetailsClass movieDetailsClass=response.body();
                    }

                }

                @Override
                public void onFailure(Call<MovieDetailsClass> call, Throwable t) {

                }
            });
        }else{
            interet_connection.setVisibility(View.VISIBLE);
            Snackbar snackbar = Snackbar
                    .make(interet_connection, "You are not connected to the wifi", Snackbar.LENGTH_LONG);
            snackbar.show();
            hideShowProgressBar(0);
        }
    }
*/
    private  void dimmissDialog(){
        if(dialog_confirm.isShowing()){
            dialog_confirm.dismiss();
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dimmissDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dimmissDialog();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null) {
            movieObjects = savedInstanceState.getParcelableArrayList("video_details");
            movieAdapter = new MovieAdapter(movieObjects);
            movieRv.setAdapter(movieAdapter);
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelableArrayList(PARC,(ArrayList<? extends Parcelable>) movieObjects);
    }
}










