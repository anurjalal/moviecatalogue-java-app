package my.jalal.catalogue.ui.favorite_tv_show;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;

import my.jalal.catalogue.BuildConfig;
import my.jalal.catalogue.api.JsonPlaceholderApi;
import my.jalal.catalogue.model.TvShow;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoriteTvShowViewModel extends ViewModel implements MyAsyncCallback {
    private MutableLiveData<ArrayList<TvShow>> listTvShows = new MutableLiveData<>();

    public void setTvShow(Map<String, ArrayList<String>> hashmap) {
        DemoAsync demoAsync = new DemoAsync(this);
        demoAsync.execute(hashmap);
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(ArrayList<TvShow> listTvShow) {
        listTvShows.postValue(listTvShow);

    }

    private static class DemoAsync extends AsyncTask<Map<String, ArrayList<String>>, Void, ArrayList<TvShow>> {
        static final String LOG_ASYNC = "DemoAsync";
        WeakReference<MyAsyncCallback> myListener;

        DemoAsync(MyAsyncCallback myListener) {
            this.myListener = new WeakReference<>(myListener);
        }

        @Override
        protected ArrayList<TvShow> doInBackground(Map<String, ArrayList<String>>... hashMaps) {
            final ArrayList<TvShow> listItems = new ArrayList<>();
            Map<String, ArrayList<String>> input = hashMaps[0];
            ArrayList<String> id = input.get("id");
            ArrayList<String> languages = input.get("language");
            String language = languages.get(0);
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BuildConfig.TMDB_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            JsonPlaceholderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceholderApi.class);
            for (int i = 0; i < id.size(); i++) {
                Call<TvShow> call = jsonPlaceHolderApi.getTvShowDetail(Integer.parseInt(id.get(i)), BuildConfig.TMDB_API_KEY, language);
                try {
                    TvShow tvShow = call.execute().body();
                    listItems.add(tvShow);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return listItems;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(LOG_ASYNC, "status : onPreExecute");

            MyAsyncCallback myListener = this.myListener.get();
            if (myListener != null) {
                myListener.onPreExecute();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<TvShow> result) {
            super.onPostExecute(result);
            Log.d(LOG_ASYNC, "status : onPostExecute");

            MyAsyncCallback myListener = this.myListener.get();
            if (myListener != null) {
                myListener.onPostExecute(result);
            }
        }
    }

    LiveData<ArrayList<TvShow>> getTvShow() {
        return listTvShows;
    }
}

interface MyAsyncCallback {
    void onPreExecute();

    void onPostExecute(ArrayList<TvShow> listTvShow);
}
