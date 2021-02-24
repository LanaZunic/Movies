package com.example.movies;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.movies.model.Generes;
import com.example.movies.model.Movie;
import com.example.movies.model.MovieDetails;
import com.example.movies.model.Movies;
import com.example.movies.movieapi.Client;
import com.example.movies.movieapi.RetrofitService;
import com.example.movies.utils.PaginationAdapter;
import com.example.movies.utils.PaginationScrollListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class MovieActivity extends AppCompatActivity {

    private ImageView imageViewPoster, imageViewCover;
    private TextView title, releaseDate, generes, overview;
    private RetrofitService movieService;
    Movie movieModel;
    private RecyclerView recyclerView;
    private PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        imageViewCover = findViewById(R.id.movie_backdrop);
        imageViewPoster = findViewById(R.id.movie_poster);
        releaseDate = findViewById(R.id.movie_release_date);
        generes = findViewById(R.id.movie_genere);
        title = findViewById(R.id.movie_title);
        overview = findViewById(R.id.movie_overview);
        recyclerView = (RecyclerView)findViewById(R.id.movies_recycler_view);
        adapter = new PaginationAdapter(this);

        movieService = Client.getClient().create(RetrofitService.class);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        GetDataFromIntent();
        getMovieDetails();
        loadFirstPage();

    }

    private void GetDataFromIntent() {
        if (getIntent().hasExtra("movie")) {
            movieModel = getIntent().getParcelableExtra("movie");

            title.setText(movieModel.getTitle());
            overview.setText(movieModel.getOverview());
            releaseDate.setText(movieModel.getReleaseDate());

            Glide.with(this)
                    .load(getResources().getString(R.string.image_path)
                            + movieModel.getBackdropPath())
                    .apply(RequestOptions.centerCropTransform())
                    .into(imageViewCover);

            Glide.with(this)
                    .load(getResources().getString(R.string.image_path)
                            + movieModel.getPosterPath())
                    .apply(RequestOptions.fitCenterTransform())
                    .into(imageViewPoster);
        }
    }

    private void getMovieDetails() {

        movieService.getMovieDetails(movieModel.getId(), getResources().getString(R.string.api_key)).enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                List<Generes> g = response.body().getGeneresList();
                String allGeneres = "";
                for (Generes gen : g) {
                    allGeneres += gen.getGenereName() + ',';
                }
                allGeneres = allGeneres.substring(0, allGeneres.length() - 1);
                generes.setText(allGeneres);
            }
            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {

                Toast toast = Toast.makeText(MovieActivity.this, "Unable to fetch similar movies.", Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }

    private void loadFirstPage() {
        movieService.getSimilarMovies(movieModel.getId(),getResources().getString(R.string.api_key)).enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                List<Movie> results = response.body().getMovies();
                adapter.addAll(results);
            }
            @Override
            public void onFailure(Call<Movies> call, Throwable t) {

            }

        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MovieActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }
}

