package my.jalal.catalogue.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import my.jalal.catalogue.BuildConfig;
import my.jalal.catalogue.api.JsonPlaceholderApi;
import my.jalal.catalogue.R;
import my.jalal.catalogue.model.Movie;
import my.jalal.catalogue.model.MovieList;
import my.jalal.catalogue.model.NotificationItem;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DailyMovieService extends IntentService {
    private int idMovieNotification = 0;
    private static List<NotificationItem> stackNotif = new ArrayList<>();
    public static final String TAG = DailyMovieService.class.getSimpleName();
    private static final int MAX_NOTIFICATION = 4;
    private static final String GROUP_KEY_MOVIES = "group_key_movies";
    private static final CharSequence CHANNEL_NAME = "jalal channel";

    public DailyMovieService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent : Mulai.....");
        if (intent != null) {
            idMovieNotification = 0;
            stackNotif.clear();
            Calendar calendar = Calendar.getInstance();
            String day = calendar.get(Calendar.DATE) < 10 ? "0" + calendar.get(Calendar.DATE) : String.valueOf(calendar.get(Calendar.DATE));
            String month = calendar.get(Calendar.MONTH) < 9 ? "0" + (calendar.get(Calendar.MONTH) + 1) : String.valueOf(calendar.get(Calendar.MONTH) + 1);
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            String date = year + "-" + month + "-" + day;
            Log.d("date", date);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS);
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BuildConfig.TMDB_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
            builder.client(httpClient.build());
            Retrofit retrofit = builder.build();
            JsonPlaceholderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceholderApi.class);
            Call<MovieList> call = jsonPlaceHolderApi.getTodayMovieRelease(BuildConfig.TMDB_API_KEY, date, date);
            call.enqueue(new Callback<MovieList>() {
                @Override
                public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                    if (!response.isSuccessful()) {
                        Log.d("Failed load movie: ", String.valueOf(response.code()));
                        return;
                    }
                    List<Movie> movies = response.body().getMovieList();
                    if (!movies.isEmpty()) {
                        for (Movie movie : movies) {
                            stackNotif.add(new NotificationItem(idMovieNotification, movie.getName()));
                            Log.d("movie name", movie.getName());
                            sendMovieNotif(getApplicationContext());
                            idMovieNotification++;
                        }
                    }
                }

                @Override
                public void onFailure(Call<MovieList> call, Throwable t) {
                    Log.d("Failed get Data: ", (t.getMessage()));
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    private void sendMovieNotif(Context context) {
        Log.d("ini", "disini222");
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder;
        String CHANNEL_ID = "channel 01";
        if (idMovieNotification < MAX_NOTIFICATION) {
            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(context.getResources().getString(R.string.new_movie_notive_message))
                    .setContentText(stackNotif.get(idMovieNotification).getMessage())
                    .setSmallIcon(R.drawable.ic_local_movies_white_24dp)
                    .setGroup(GROUP_KEY_MOVIES)
                    .setAutoCancel(true);
        } else {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                    .addLine(stackNotif.get(idMovieNotification).getMessage())
                    .addLine(stackNotif.get(idMovieNotification - 1).getMessage())
                    .setBigContentTitle(context.getResources().getString(R.string.new_movie_notive_message));
            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(context.getResources().getString(R.string.new_movie_notive_message))
                    .setContentText(stackNotif.get(idMovieNotification).getMessage())
                    .setSmallIcon(R.drawable.ic_local_movies_white_24dp)
                    .setGroup(GROUP_KEY_MOVIES)
                    .setGroupSummary(true)
                    .setStyle(inboxStyle)
                    .setAutoCancel(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //CREATE OR UPDATE
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mBuilder.setChannelId(CHANNEL_ID);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }
        Notification notification = mBuilder.build();
        if (mNotificationManager != null) {
            mNotificationManager.notify(idMovieNotification, notification);
        }
    }
}
