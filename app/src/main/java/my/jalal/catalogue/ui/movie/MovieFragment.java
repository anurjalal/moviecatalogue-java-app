package my.jalal.catalogue.ui.movie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import my.jalal.catalogue.ui.movie_detail.MovieDetailActivity;
import my.jalal.catalogue.R;
import my.jalal.catalogue.adapter.ListMovieAdapter;
import my.jalal.catalogue.model.Movie;

public class MovieFragment extends Fragment implements SearchView.OnQueryTextListener {

    private MovieViewModel movieViewModel;
    private ListMovieAdapter adapter;
    private ProgressBar progressBar;
    private String language;

    public MovieFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);
        showLoading(true);
        RecyclerView rvMovies = view.findViewById(R.id.rv_movies);
        rvMovies.setHasFixedSize(true);
        rvMovies.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new ListMovieAdapter();
        adapter.notifyDataSetChanged();
        rvMovies.setAdapter(adapter);
        adapter.setOnItemClickCallback(new ListMovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie data) {
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra(MovieDetailActivity.EXTRA_DATA, data);
                startActivity(intent);
            }
        });

        movieViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MovieViewModel.class);
        language = Locale.getDefault().toLanguageTag();
        movieViewModel.setMovie(language);
        movieViewModel.getMovies().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                if (movies != null) {
                    showLoading(true);
                    Log.d("jumlahdata", String.valueOf(movies.size()));
                    adapter.setData(movies);
                    showLoading(false);
                }
            }
        });
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()) {
            movieViewModel.setMovieBySearching(language, newText);
            movieViewModel.getMovies().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
                @Override
                public void onChanged(ArrayList<Movie> movies) {
                    if (movies != null) {
                        showLoading(true);
                        Log.d("jumlahdata", String.valueOf(movies.size()));
                        adapter.setData(movies);
                        showLoading(false);
                    }
                }
            });
        } else {
            movieViewModel.setMovie(language);
        }
        return false;
    }
}
