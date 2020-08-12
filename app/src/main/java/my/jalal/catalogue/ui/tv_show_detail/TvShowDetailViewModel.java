package my.jalal.catalogue.ui.tv_show_detail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.TimeUnit;

import my.jalal.catalogue.BuildConfig;
import my.jalal.catalogue.api.JsonPlaceholderApi;
import my.jalal.catalogue.model.TvShow;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TvShowDetailViewModel extends ViewModel {
    private MutableLiveData<TvShow> tvShowMutableLiveData = new MutableLiveData<>();

    void setTvShow(String language, int id) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        builder.client(httpClient.build());
        Retrofit retrofit = builder.build();
        JsonPlaceholderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceholderApi.class);
        Call<TvShow> call = jsonPlaceHolderApi.getTvShowDetail(id, BuildConfig.TMDB_API_KEY, language);
        call.enqueue(new Callback<TvShow>() {
            @Override
            public void onResponse(Call<TvShow> call, Response<TvShow> response) {
                if (!response.isSuccessful()) {
                    Log.d("Failed load data: ", String.valueOf(response.code()));
                    return;
                }
                TvShow tvShow = response.body();
                tvShowMutableLiveData.postValue(tvShow);
            }

            @Override
            public void onFailure(Call<TvShow> call, Throwable t) {
                Log.d("Failed get Data: ", (t.getMessage()));
            }
        });
    }

    LiveData<TvShow> getTvShow() {
        return tvShowMutableLiveData;
    }
}
