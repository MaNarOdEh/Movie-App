package com.example.movie_app.Recycle;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movie_app.Activity.MovieDetails;
import com.example.movie_app.Classes.MovieDetailsClass;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.MovieAPI.FetchDataPublicAccess;
import com.example.movie_app.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<MovieDetailsClass> movieObjects;

    //adapter class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private  ImageView movieImageLinear;
        private TextView moveNameTv;
        private Context context ;

        public ViewHolder(View viewItems) {
            super(viewItems);
            movieImageLinear=viewItems.findViewById(R.id.movie_imagev);
            moveNameTv=viewItems.findViewById(R.id.move_name_tv);
            context = viewItems.getContext();
        }

    }
    public MovieAdapter(){

    }
    public MovieAdapter(List<MovieDetailsClass>Dataset) {
        if (movieObjects == null) {
            movieObjects=new ArrayList<>();
        }
        movieObjects = Dataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactView= LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card,parent,false);
        // Return a new holder instance
        return  new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // Get the data model based on position
        final MovieDetailsClass movieObject = movieObjects.get(position);
        ImageView movieImageLinear=holder.movieImageLinear;
        TextView moveNameTv=holder.moveNameTv;
        String url_img = FetchDataPublicAccess.URI_IMAGE+movieObject.getPosterPath();
        Picasso.get()
                .load(url_img)
                .placeholder(R.drawable.sorry_img)
                .error(R.drawable.sorry_img)
                .into(movieImageLinear);
        moveNameTv.setText(movieObject.getTitle());
        //I decided to make long press because I take its easier and more efficent here than  normal press
        movieImageLinear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                   Intent intent=new Intent(holder.context, MovieDetails.class);
                   intent.putExtra("Movie",  movieObject);
                   holder.context.startActivity(intent);
                   return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return movieObjects.size();
    }

}
