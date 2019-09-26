package com.example.movie_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.movie_app.Classes.MovieDetailsClass;
import com.example.movie_app.Classes.MovieReviews;
import com.example.movie_app.Classes.MovieVideosDetails;
import com.example.movie_app.Classes.ReviewResult;
import com.example.movie_app.Classes.VideoResult;
import com.example.movie_app.MovieAPI.FetchDataPublicAccess;
import com.example.movie_app.MovieAPI.MovieReview;
import com.example.movie_app.MovieAPI.MovieVideos;
import com.example.movie_app.R;
import com.example.movie_app.Recycle.ReviewAdapter;
import com.example.movie_app.Recycle.VideoAdapter;
import com.example.movie_app.Room.FilmDetails;
import com.example.movie_app.Room.FilmRepository;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetails extends AppCompatActivity {

    @BindView(R.id.move_name_tv)  TextView moveNameTv2;
    @BindView(R.id.movie_name_tv)  TextView  movieNameTv;
    @BindView(R.id.movie_rating_tv)  TextView movieRatingTv;
    @BindView(R.id.description_tv)  TextView descriptionTv;
    @BindView(R.id.movie_imagev)  ImageView movieImagev;
    @BindView(R.id.make_fav_btn)  Button makeFavBtn;
    @BindView(R.id.movie_trailer_rv)  RecyclerView movieTrailerTv;
    @BindView(R.id.movie_review_rv) RecyclerView movieReviewRv;
    @BindView(R.id.trailer_fame) LinearLayout trailer_fame;
    @BindView(R.id.review_frame) LinearLayout review_frame;
    private VideoAdapter videoAdapter;
    private List<MovieVideosDetails> movieObjects=new ArrayList<>();
    private ReviewAdapter reviewAdapter;
    private  List<MovieReviews>movieReviews=new ArrayList<>();
    private MovieDetailsClass movieDetailsClass;
    FilmDetails   userLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        setTitle("Movie Details");
        Intent intent=getIntent();
        if(intent==null){
           finish();
        }
        movieDetailsClass=  intent.getParcelableExtra("Movie");
      /*  Toast.makeText(this, movieDetailsClass.getId()+"\n"
                +movieDetailsClass.getVoteAverage()+"\n"+movieDetailsClass.getOriginalTitle()+"\n"+movieDetailsClass.getPosterPath()

                , Toast.LENGTH_LONG).show();*/

        if(movieDetailsClass!=null) {
            setInfo(movieDetailsClass);
        }
        initialize();
        makeEvent();
        fetchMovieVideos();
        fetchMovieReview();

    }
    private void initialize() {
        videoAdapter=new VideoAdapter();
        movieTrailerTv.setAdapter(videoAdapter);
        if(movieDetailsClass!=null) {
            userLiveData = new FilmRepository(getApplicationContext()).getTask(movieDetailsClass.getId());
            if (userLiveData != null && (userLiveData.getFid() + "").equals("" + movieDetailsClass.getId())) {
                makeFavBtn.setText("Un Favorite");
            }
        }

        movieTrailerTv.setLayoutManager(new LinearLayoutManager(MovieDetails.this));
        movieTrailerTv.setHasFixedSize(true);
        movieTrailerTv.setNestedScrollingEnabled(false);
        videoAdapter.notifyDataSetChanged();

        reviewAdapter=new ReviewAdapter(movieReviews);
        movieReviewRv.setAdapter(reviewAdapter);
        movieReviewRv.setLayoutManager( new LinearLayoutManager(MovieDetails.this));
        movieReviewRv.setHasFixedSize(true);
        movieReviewRv.setNestedScrollingEnabled(false);
        reviewAdapter.notifyDataSetChanged();
    }
    private void setInfo(MovieDetailsClass movieDetailsClass) {
        movieNameTv.setText(check_correctance(movieDetailsClass.getOriginalTitle())?"UnKnown":movieDetailsClass.getOriginalTitle());
        moveNameTv2.setText(check_correctance(movieDetailsClass.getTitle())?""+""+movieDetailsClass.getReleaseDate():movieDetailsClass.getTitle()+"\n"+movieDetailsClass.getReleaseDate());
        movieRatingTv.setText(movieDetailsClass.getVoteAverage()+"/10");
        String url_img =FetchDataPublicAccess.URI_IMAGE+movieDetailsClass.getPosterPath();
        if(movieDetailsClass.getBackdropPath()!=null) {
            Picasso.get()
                    .load(url_img)
                    .placeholder(R.drawable.sorry_img)
                    .error(R.drawable.sorry_img)
                    .into(movieImagev);
        }
        descriptionTv.setText(check_correctance(movieDetailsClass.getOverview())?"":movieDetailsClass.getOverview());
    }
    private  boolean check_correctance(String str){
        return (str==null||str.isEmpty());
    }

    private void makeEvent() {
        makeFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(makeFavBtn.getText().equals("Make As Favorite")){
                    FilmRepository filmRepository = new FilmRepository(getApplicationContext());
                    filmRepository.insertTask(movieDetailsClass.getId(),movieDetailsClass.getTitle(),
                            movieDetailsClass.getVoteAverage(),movieDetailsClass.getTitle()
                            );
                    makeFavBtn.setText("Un Favorite");
                }else{
                    makeFavBtn.setText("Make As Favorite");
                    FilmRepository filmRepository = new FilmRepository(getApplicationContext());
                    filmRepository.deleteTask(movieDetailsClass.getId());
                }
            }
        });
    }
    private void fetchMovieVideos(){
        if(isOnline()){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(FetchDataPublicAccess.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieVideos servicesInterface = retrofit.create(MovieVideos.class);
            Call<VideoResult> call = servicesInterface.movieService(movieDetailsClass.getId()+"",FetchDataPublicAccess.API_KEY,FetchDataPublicAccess.LANGUGE);
            call.enqueue(new Callback<VideoResult>() {
                @Override
                public void onResponse(Call<VideoResult> call, Response<VideoResult> response) {

                    if(response.body()!=null&&response.body().getResults()!=null) {
                        movieObjects=response.body().getResults();
                        videoAdapter=new VideoAdapter(movieObjects);
                        if(movieObjects.size()>0){
                           trailer_fame.setVisibility(View.VISIBLE);
                        }
                        movieTrailerTv.setAdapter(videoAdapter);
                    }
                }
                @Override
                public void onFailure(Call<VideoResult> call, Throwable t) {
                    Toast.makeText(MovieDetails.this, t.getMessage()+"   "+t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }else{

        }
    }
    private void fetchMovieReview(){
        if(isOnline()){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(FetchDataPublicAccess.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieReview servicesInterface = retrofit.create(MovieReview.class);
            Call<ReviewResult> call = servicesInterface.movieService(movieDetailsClass.getId()+"",
                    FetchDataPublicAccess.API_KEY,FetchDataPublicAccess.LANGUGE);
            call.enqueue(new Callback<ReviewResult>() {
                @Override
                public void onResponse(Call<ReviewResult> call, Response<ReviewResult> response) {
                    if(response.body()!=null&&response.body().getResults()!=null) {
                        movieReviews=response.body().getResults();
                        reviewAdapter=new ReviewAdapter(movieReviews);
                        if(movieReviews.size()>0){
                            review_frame.setVisibility(View.VISIBLE);
                        }
                        movieReviewRv.setAdapter(reviewAdapter);
                    }
                }

                @Override
                public void onFailure(Call<ReviewResult> call, Throwable t) {
                    Toast.makeText(MovieDetails.this, t.getMessage()+"   "+t.toString(), Toast.LENGTH_LONG).show();


                }
            });
        }else{

        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() { super.onDestroy(); }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
