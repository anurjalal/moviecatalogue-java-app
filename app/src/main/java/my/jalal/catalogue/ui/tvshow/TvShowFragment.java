package my.jalal.catalogue.ui.tvshow;

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

import my.jalal.catalogue.R;
import my.jalal.catalogue.model.Movie;
import my.jalal.catalogue.ui.tv_show_detail.TvShowDetailActivity;
import my.jalal.catalogue.adapter.ListTvShowAdapter;
import my.jalal.catalogue.model.TvShow;

public class TvShowFragment extends Fragment implements SearchView.OnQueryTextListener {

    private ListTvShowAdapter adapter;
    private ProgressBar progressBar;
    private TvShowViewModel tvShowViewModel;
    private String language;

    public TvShowFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tvshow, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);

        showLoading(true);
        RecyclerView rvTvShows = view.findViewById(R.id.rv_tv_shows);
        rvTvShows.setHasFixedSize(true);
        rvTvShows.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new ListTvShowAdapter();
        adapter.notifyDataSetChanged();
        rvTvShows.setAdapter(adapter);
        adapter.setOnItemClickCallback(new ListTvShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShow data) {
                Intent intent = new Intent(getActivity(), TvShowDetailActivity.class);
                intent.putExtra(TvShowDetailActivity.EXTRA_DATA, data);
                startActivity(intent);
            }
        });

        tvShowViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(TvShowViewModel.class);
        language = Locale.getDefault().toLanguageTag();
        tvShowViewModel.setTvShow(language);
        tvShowViewModel.getTvShows().observe(getViewLifecycleOwner(), new Observer<ArrayList<TvShow>>() {
            @Override
            public void onChanged(ArrayList<TvShow> tvShows) {
                if (tvShows != null) {
                    showLoading(true);
                    adapter.setData(tvShows);
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
        Log.d("searchkuh", query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()) {
            tvShowViewModel.setTvShowBySearching(language, newText);
            tvShowViewModel.getTvShows().observe(getViewLifecycleOwner(), new Observer<ArrayList<TvShow>>() {
                @Override
                public void onChanged(ArrayList<TvShow> tvShows) {
                    if (tvShows != null) {
                        showLoading(true);
                        Log.d("jumlahdata", String.valueOf(tvShows.size()));
                        adapter.setData(tvShows);
                        showLoading(false);
                    }
                }
            });
        } else {
            tvShowViewModel.setTvShow(language);
        }
        return false;
    }
}