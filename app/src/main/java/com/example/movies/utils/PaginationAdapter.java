package com.example.movies.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.movies.MovieActivity;
import com.example.movies.R;
import com.example.movies.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private List<Movie> movieResults;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;


    public PaginationAdapter(Context context) {
        this.context = context;
        movieResults = new ArrayList<>();
    }

    public void setMovies(List<Movie> movieResults) {
        this.movieResults = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.movie_card_item, parent, false);
                viewHolder = new MovieVH(viewItem);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Movie result = movieResults.get(position);
        String imagePathe=context.getResources().getString(R.string.image_path);

        switch (getItemViewType(position)) {
            case ITEM:
                MovieVH movieVH = (MovieVH) holder;
                movieVH.mMovieTitle.setText(result.getTitle());
                Glide.with(context).load(imagePathe + result.getPosterPath()).apply(RequestOptions.centerCropTransform()).into(movieVH.mPosterImg);
                break;

            case LOADING:
                LoadingVH loadingViewHolder = (LoadingVH) holder;
                loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return movieResults == null ? 0 : movieResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void add(Movie r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(List<Movie> moveResults) {
        for (Movie result : moveResults) {
            add(result);
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Movie());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = movieResults.size() - 1;
        Movie result = getItem(position);
        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Movie getItem(int position) {
        return movieResults.get(position);
    }

    protected class MovieVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mMovieTitle;
        private ImageView mPosterImg;

        public MovieVH(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mMovieTitle = (TextView) itemView.findViewById(R.id.movie_title_text_view);
            mPosterImg = (ImageView) itemView.findViewById(R.id.movie_image_view);
        }

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(context, MovieActivity.class);
            intent.putExtra("movie", getItem(getAdapterPosition()));
            context.startActivity(intent);

        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder  {
        private ProgressBar progressBar;

        public LoadingVH(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
        }
    }

}
