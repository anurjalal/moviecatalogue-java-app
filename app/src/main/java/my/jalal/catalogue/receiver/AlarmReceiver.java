package my.jalal.catalogue.receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

import my.jalal.catalogue.service.DailyMovieService;
import my.jalal.catalogue.MainActivity;
import my.jalal.catalogue.R;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String TYPE_APP_ALARM = "appRepeatingAlarm";
    public static final String TYPE_MOVIE_ALARM = "movieRepeatingAlarm";
    public static final String EXTRA_TYPE = "type";
    private int ID_APP_REPEATING = 100;
    private int ID_MOVIE_REPEATING = 200;

    public AlarmReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        int notifId;
        if(type!= null) {
            if (type.equals(TYPE_MOVIE_ALARM)) {
                showMovieNotif(context);
            } else {
                notifId = ID_APP_REPEATING;
                showAppNotif(context, notifId);
            }
        }
    }

    public void showAppNotif(Context context, int notifId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";
        String title = context.getApplicationContext().getResources().getString(R.string.app_name);
        String message = context.getApplicationContext().getResources().getString(R.string.message_app_notif);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_white_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setContentIntent(pIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }
        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }
    }

    public void setRepeatingAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_TYPE, type);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MINUTE, 41);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        PendingIntent pendingIntent;
        if (type.equals(TYPE_APP_ALARM)) {
            calendar.set(Calendar.HOUR_OF_DAY, 12); //7 o'clock
            pendingIntent = PendingIntent.getBroadcast(context, ID_APP_REPEATING, intent, 0);
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, 12); //8 o'clock
            pendingIntent = PendingIntent.getBroadcast(context, ID_MOVIE_REPEATING, intent, 0);
        }
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) { // prevent alarm triggered when it set after the set-time
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        long setTime = calendar.getTimeInMillis();
        if(alarmManager!= null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, setTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode;
        PendingIntent pendingIntent;
        if (type.equals(TYPE_APP_ALARM)) {
            requestCode = ID_APP_REPEATING;
            pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        } else {
            requestCode = ID_MOVIE_REPEATING;
            pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        }
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private void showMovieNotif(Context context) {
        Intent intentService = new Intent(context, DailyMovieService.class);
        context.startService(intentService);
    }

}
