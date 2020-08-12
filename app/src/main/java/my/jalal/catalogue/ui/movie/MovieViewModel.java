package my.jalal.catalogue.ui.movie;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import my.jalal.catalogue.BuildConfig;
import my.jalal.catalogue.api.JsonPlaceholderApi;
import my.jalal.catalogue.model.Movie;
import my.jalal.catalogue.model.MovieList;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Movie>> listMovies = new MutableLiveData<>();

    void setMovie(String language) {

        final ArrayList<Movie> listItems = new ArrayList<>();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        builder.client(httpClient.build());
        Retrofit retrofit = builder.build();
        JsonPlaceholderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceholderApi.class);
        Call<MovieList> call = jsonPlaceHolderApi.getMovieList(BuildConfig.TMDB_API_KEY, language);

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (!response.isSuccessful()) {
                    Log.d("Failed load movie: ", String.valueOf(response.code()));
                    return;
                }
                List<Movie> movies = response.body().getMovieList();
                listItems.addAll(movies);
                listMovies.postValue(listItems);
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Log.d("Failed get Data: ", (t.getMessage()));
            }
        });
    }

    void setMovieBySearching(String language, String keywords) {

        final ArrayList<Movie> listItems = new ArrayList<>();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        builder.client(httpClient.build());
        Retrofit retrofit = builder.build();
        JsonPlaceholderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceholderApi.class);
        Call<MovieList> call = jsonPlaceHolderApi.getMovieListBySearching(BuildConfig.TMDB_API_KEY, language, keywords);

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (!response.isSuccessful()) {
                    Log.d("Failed load movie: ", String.valueOf(response.code()));
                    return;
                }
                List<Movie> movies = response.body().getMovieList();
                listItems.addAll(movies);
                listMovies.postValue(listItems);
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Log.d("Failed get Data: ", (t.getMessage()));
            }
        });
    }


    LiveData<ArrayList<Movie>> getMovies() {
        return listMovies;
    }
}
