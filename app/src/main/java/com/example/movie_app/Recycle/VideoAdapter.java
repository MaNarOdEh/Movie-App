package com.example.movie_app.Recycle;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movie_app.Classes.MovieVideosDetails;
import com.example.movie_app.R;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private List<MovieVideosDetails> movieObjects;

    //adapter class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView youtubeIconIm;
        private TextView videoDescTv;
        private Context context ;

        public ViewHolder(View viewItems) {
            super(viewItems);
            youtubeIconIm=viewItems.findViewById(R.id.youtube_icon_im);
            videoDescTv=viewItems.findViewById(R.id.video_desc_tv);
            context = viewItems.getContext();
        }

    }
    public VideoAdapter(){
        movieObjects=new ArrayList<>();
    }
    public VideoAdapter(List<MovieVideosDetails>Dataset) {
        movieObjects = Dataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View contactView= LayoutInflater.from(parent.getContext()).inflate(R.layout.trailser_view,parent,false);
        return new  ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final MovieVideosDetails movieDetailsClass=movieObjects.get(position);
        holder.videoDescTv.setText(movieDetailsClass.getName());
        holder.youtubeIconIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + movieDetailsClass.getKey()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + movieDetailsClass.getKey()));
                try {
                    holder.context.startActivity(appIntent);

                }catch (Exception e){
                    try {

                    }catch (Exception ee){
                        holder.context.startActivity(webIntent);
                    }
                }

            }
        });

    }
    @Override
    public int getItemCount() {
        return movieObjects.size();
    }
}
