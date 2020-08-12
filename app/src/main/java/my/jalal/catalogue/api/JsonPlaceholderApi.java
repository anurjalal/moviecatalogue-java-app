package my.jalal.catalogue.api;

import my.jalal.catalogue.model.Movie;
import my.jalal.catalogue.model.MovieList;
import my.jalal.catalogue.model.TvShow;
import my.jalal.catalogue.model.TvShowList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceholderApi {

    @GET("3/discover/movie")
    Call<MovieList> getMovieList(
            @Query("api_key") String API_KEY,
            @Query("language") String language);

    @GET("3/discover/tv")
    Call<TvShowList> getTvShowList(
            @Query("api_key") String API_KEY,
            @Query("language") String language);

    @GET("3/movie/{id}")
    Call<Movie> getMovieDetail(
            @Path("id") Integer id,
            @Query("api_key") String API_KEY,
            @Query("language") String language);

    @GET("3/tv/{id}")
    Call<TvShow> getTvShowDetail(
            @Path("id") Integer id,
            @Query("api_key") String API_KEY,
            @Query("language") String language);

    @GET("3/discover/movie")
    Call<MovieList> getTodayMovieRelease(
            @Query("api_key") String API_KEY,
            @Query("primary_release_date.gte") String todayDate,
            @Query("primary_release_date.lte") String todayDatetoo);

    @GET("3/search/movie")
    Call<MovieList> getMovieListBySearching(
            @Query("api_key") String API_KEY,
            @Query("language") String language,
            @Query("query") String keyword);

    @GET("3/search/tv")
    Call<TvShowList> getTvShowListBySearching(
            @Query("api_key") String API_KEY,
            @Query("language") String language,
            @Query("query") String keyword);

}
