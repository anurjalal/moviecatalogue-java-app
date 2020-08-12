package my.jalal.catalogue.ui.tvshow;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import my.jalal.catalogue.BuildConfig;
import my.jalal.catalogue.api.JsonPlaceholderApi;
import my.jalal.catalogue.model.TvShow;
import my.jalal.catalogue.model.TvShowList;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TvShowViewModel extends ViewModel {

    private MutableLiveData<ArrayList<TvShow>> listTvShow = new MutableLiveData<>();

    void setTvShow(String language) {

        final ArrayList<TvShow> listItems = new ArrayList<>();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        builder.client(httpClient.build());
        Retrofit retrofit = builder.build();
        JsonPlaceholderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceholderApi.class);
        Call<TvShowList> call = jsonPlaceHolderApi.getTvShowList(BuildConfig.TMDB_API_KEY, language);

        call.enqueue(new Callback<TvShowList>() {
            @Override
            public void onResponse(Call<TvShowList> call, Response<TvShowList> response) {
                if (!response.isSuccessful()) {
                    Log.d("Failed load data: ", String.valueOf(response.code()));
                    return;
                }
                List<TvShow> movies = response.body().getTvShowList();
                listItems.addAll(movies);
                listTvShow.postValue(listItems);
            }

            @Override
            public void onFailure(Call<TvShowList> call, Throwable t) {
                Log.d("Failed get Data: ", (t.getMessage()));
            }
        });
    }

    void setTvShowBySearching(String language, String keywords) {

        final ArrayList<TvShow> listItems = new ArrayList<>();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        builder.client(httpClient.build());
        Retrofit retrofit = builder.build();
        JsonPlaceholderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceholderApi.class);
        Call<TvShowList> call = jsonPlaceHolderApi.getTvShowListBySearching(BuildConfig.TMDB_API_KEY, language, keywords);

        call.enqueue(new Callback<TvShowList>() {
            @Override
            public void onResponse(Call<TvShowList> call, Response<TvShowList> response) {
                if (!response.isSuccessful()) {
                    Log.d("Failed load data: ", String.valueOf(response.code()));
                    return;
                }
                List<TvShow> movies = response.body().getTvShowList();
                listItems.addAll(movies);
                listTvShow.postValue(listItems);
            }

            @Override
            public void onFailure(Call<TvShowList> call, Throwable t) {
                Log.d("Failed get Data: ", (t.getMessage()));
            }
        });
    }

    LiveData<ArrayList<TvShow>> getTvShows() {
        return listTvShow;
    }
}
