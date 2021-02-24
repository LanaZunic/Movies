package com.example.movies.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movies.R;
import com.example.movies.model.Movie;
import com.example.movies.model.Movies;
import com.example.movies.movieapi.Client;
import com.example.movies.movieapi.RetrofitService;
import com.example.movies.utils.PaginationAdapter;
import com.example.movies.utils.PaginationScrollListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularMoviesFragment extends Fragment {

    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 1;
    private int currentPage = PAGE_START;
    private RetrofitService movieService;

    public PopularMoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.popular_movies_recycler_view);
        adapter = new PaginationAdapter(getContext());
        movieService = Client.getClient().create(RetrofitService.class);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                loadNextPage();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        loadFirstPage();
        return view;
    }

    private void loadFirstPage() {

        movieService.getPopularMovies(getResources().getString(R.string.api_key), PAGE_START).enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                List<Movie> results = response.body().getMovies();
                adapter.addAll(results);
                TOTAL_PAGES = response.body().getTotalPages();
                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {

            }

        });
    }

    private void loadNextPage() {

        movieService.getPopularMovies(getResources().getString(R.string.api_key), currentPage).enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                List<Movie> results = response.body().getMovies();
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
