package my.jalal.catalogue.ui.favorite_tv_show;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import my.jalal.catalogue.R;
import my.jalal.catalogue.ui.tv_show_detail.TvShowDetailActivity;
import my.jalal.catalogue.adapter.ListTvShowAdapter;
import my.jalal.catalogue.db.CatalogueHelper;
import my.jalal.catalogue.model.TvShow;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteTvShowFragment extends Fragment {
    private FavoriteTvShowViewModel tvShowViewModel;
    private ListTvShowAdapter adapter;
    private ProgressBar progressBar;
    private CatalogueHelper catalogueHelperFavorite;
    private final String type = "TVSHOW";

    public FavoriteTvShowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        catalogueHelperFavorite = new CatalogueHelper(getContext());
        progressBar = view.findViewById(R.id.progressBar);
        showLoading(true);
        RecyclerView rvTvShow = view.findViewById(R.id.rv_tv_shows);
        rvTvShow.setHasFixedSize(true);
        rvTvShow.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new ListTvShowAdapter();
        adapter.notifyDataSetChanged();
        rvTvShow.setAdapter(adapter);
        adapter.setOnItemClickCallback(new ListTvShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShow data) {
                Intent intent = new Intent(getActivity(), TvShowDetailActivity.class);
                intent.putExtra(TvShowDetailActivity.EXTRA_DATA, data);
                startActivity(intent);
            }
        });

        tvShowViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(FavoriteTvShowViewModel.class);
        final ArrayList<String> listFavoriteTvShow = catalogueHelperFavorite.getAllFavoriteId(catalogueHelperFavorite, type);
        ArrayList<String> language = new ArrayList<>();
        language.add(Locale.getDefault().toLanguageTag());
        Map<String, ArrayList<String>> data = new HashMap<>();
        data.put("language", language);
        data.put("id", listFavoriteTvShow);
        tvShowViewModel.setTvShow(data);
        tvShowViewModel.getTvShow().observe(getViewLifecycleOwner(), new Observer<ArrayList<TvShow>>() {
            @Override
            public void onChanged(ArrayList<TvShow> tvShows) {
                if (tvShows != null) {
                    ArrayList<String> listFavoriteTvShow = catalogueHelperFavorite.getAllFavoriteId(catalogueHelperFavorite, type);
                    ArrayList<String> language = new ArrayList<>();
                    language.add(Locale.getDefault().toLanguageTag());
                    Map<String, ArrayList<String>> data = new HashMap<>();
                    data.put("language", language);
                    data.put("id", listFavoriteTvShow);
                    tvShowViewModel.setTvShow(data);
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
