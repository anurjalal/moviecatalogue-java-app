package my.jalal.consumerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import my.jalal.consumerapp.adapter.ListMovieAdapter;
import my.jalal.consumerapp.db.DatabaseContract;
import my.jalal.consumerapp.helper.MappingHelper;
import my.jalal.consumerapp.model.Movie;

public class MainActivity extends AppCompatActivity implements LoadMoviesCallback {
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private ArrayList<String> listId = new ArrayList<>();
    private MainViewModel mainViewModel;
    private ListMovieAdapter adapter;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        showLoading(true);
        RecyclerView rvMovies = findViewById(R.id.rv_movies);
        rvMovies.setHasFixedSize(true);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListMovieAdapter();
        adapter.notifyDataSetChanged();
        rvMovies.setAdapter(adapter);
        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        ArrayList<String> language = new ArrayList<>();
        language.add(Locale.getDefault().toLanguageTag());
        Map<String, ArrayList<String>> data = new HashMap<>();
        data.put("language", language);
        data.put("id", listId);
        mainViewModel.setMovie(data);
        mainViewModel.getMovies().observe(this, new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                ArrayList<String> language = new ArrayList<>();
                language.add(Locale.getDefault().toLanguageTag());
                Map<String, ArrayList<String>> data = new HashMap<>();
                data.put("language", language);
                data.put("id", listId);
                mainViewModel.setMovie(data);
                showLoading(true);
                adapter.setData(movies);
                showLoading(false);
            }
        });

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(DatabaseContract.FavoriteColumn.CONTENT_URI, true, myObserver);
        if (savedInstanceState == null) {
            new LoadIdListFavoriteAsync(this, this).execute();
        } else {
            ArrayList<String> list = savedInstanceState.getStringArrayList(EXTRA_STATE);
            if (list != null) {
                listId.addAll(list);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(EXTRA_STATE, listId);
    }

    public static class DataObserver extends ContentObserver {
        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadIdListFavoriteAsync(context, (LoadMoviesCallback) context).execute();
        }
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(ArrayList<String> idListFavorite) {
        listId.addAll(idListFavorite);

    }

    private static class LoadIdListFavoriteAsync extends AsyncTask<Void, Void, ArrayList<String>> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMoviesCallback> weakCallback;

        private LoadIdListFavoriteAsync(Context context, LoadMoviesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
            Log.d("SSS", "SSS");
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(DatabaseContract.FavoriteColumn.CONTENT_URI, null, null, null, null);
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<String> idListFavorite) {
            super.onPostExecute(idListFavorite);
            weakCallback.get().postExecute(idListFavorite);
        }
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}

interface LoadMoviesCallback {
    void preExecute();

    void postExecute(ArrayList<String> idListFavorite);
}
