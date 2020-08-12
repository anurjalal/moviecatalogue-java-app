package my.jalal.catalogue.ui.movie_detail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.TimeUnit;

import my.jalal.catalogue.BuildConfig;
import my.jalal.catalogue.api.JsonPlaceholderApi;
import my.jalal.catalogue.model.Movie;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailViewModel extends ViewModel {
    private MutableLiveData<Movie> movieMutableLiveData = new MutableLiveData<>();

    void setMovie(String language, int id) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        builder.client(httpClient.build());
        Retrofit retrofit = builder.build();
        JsonPlaceholderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceholderApi.class);
        Call<Movie> call = jsonPlaceHolderApi.getMovieDetail(id, BuildConfig.TMDB_API_KEY, language);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (!response.isSuccessful()) {
                    Log.d("Failed load movie: ", String.valueOf(response.code()));
                    return;
                }
                Movie movie = response.body();
                movieMutableLiveData.postValue(movie);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.d("Failed get Data: ", (t.getMessage()));
            }
        });

    }

    LiveData<Movie> getMovie() {
        return movieMutableLiveData;
    }
}
