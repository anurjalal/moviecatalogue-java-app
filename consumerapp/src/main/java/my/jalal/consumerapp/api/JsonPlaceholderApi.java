package my.jalal.consumerapp.api;

import my.jalal.consumerapp.model.Movie;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceholderApi {
    @GET("3/movie/{id}")
    Call<Movie> getMovieDetail(
            @Path("id") Integer id,
            @Query("api_key") String API_KEY,
            @Query("language") String language);

}
