package com.example.movie_app.Recycle;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.Classes.MovieReviews;
import com.example.movie_app.R;
import java.util.ArrayList;
import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private List<MovieReviews> movieObjects;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView authorNameTv;
        private TextView reviewContentTv;
        private CardView card_view;
         Context context ;

        public ViewHolder(View viewItems) {
            super(viewItems);
            authorNameTv=viewItems.findViewById(R.id.author_name_tv);
            reviewContentTv=viewItems.findViewById(R.id.review_content_tv);
            card_view=viewItems.findViewById(R.id.card_view);
            context = viewItems.getContext();
        }

    }
    public ReviewAdapter(){
        movieObjects=new ArrayList<>();
    }
    public ReviewAdapter(List<MovieReviews> Dataset) {
        movieObjects = Dataset;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactView= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card,parent,false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewAdapter.ViewHolder holder, int position) {
        final MovieReviews movieReviews=movieObjects.get(position);
        holder.authorNameTv.setText(movieReviews.getAuthor()!=null?movieReviews.getAuthor():"");
        holder.reviewContentTv.setText(movieReviews.getContent()!=null?movieReviews.getContent():"");
        holder.card_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(movieReviews.getUrl()));
                holder.context.startActivity(webIntent);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieObjects.size();
    }
}
