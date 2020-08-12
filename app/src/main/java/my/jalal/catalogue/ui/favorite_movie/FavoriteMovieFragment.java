package my.jalal.catalogue.ui.favorite_movie;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import my.jalal.catalogue.ui.movie_detail.MovieDetailActivity;
import my.jalal.catalogue.R;
import my.jalal.catalogue.adapter.ListMovieAdapter;
import my.jalal.catalogue.db.CatalogueHelper;
import my.jalal.catalogue.model.Movie;

public class FavoriteMovieFragment extends Fragment {
    private FavoriteMovieViewModel movieViewModel;
    private ListMovieAdapter adapter;
    private ProgressBar progressBar;
    private CatalogueHelper catalogueHelperFavorite;
    private final String type = "MOVIE";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        catalogueHelperFavorite = new CatalogueHelper(getContext());
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

        movieViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(FavoriteMovieViewModel.class);
        final ArrayList<String> listFavoriteMovie = catalogueHelperFavorite.getAllFavoriteId(catalogueHelperFavorite, type);
        ArrayList<String> language = new ArrayList<>();
        language.add(Locale.getDefault().toLanguageTag());
        Map<String, ArrayList<String>> data = new HashMap<>();
        data.put("language", language);
        data.put("id", listFavoriteMovie);
        movieViewModel.setMovie(data);
        movieViewModel.getMovies().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                if (movies != null) {
                    ArrayList<String> listFavoriteMovie = catalogueHelperFavorite.getAllFavoriteId(catalogueHelperFavorite, type);
                    ArrayList<String> language = new ArrayList<>();
                    language.add(Locale.getDefault().toLanguageTag());
                    Map<String, ArrayList<String>> data = new HashMap<>();
                    data.put("language", language);
                    data.put("id", listFavoriteMovie);
                    movieViewModel.setMovie(data);
                    showLoading(true);
                    adapter.setData(movies);
                    showLoading(false);
                }
            }
        });
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(false);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}