package my.jalal.catalogue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import my.jalal.catalogue.model.Setting;
import my.jalal.catalogue.preferences.SettingPreferences;
import my.jalal.catalogue.receiver.AlarmReceiver;

public class SettingActivity extends AppCompatActivity {
    ToggleButton toggleAppReminder;
    ToggleButton toggleMovieReminder;
    AlarmReceiver alarmReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggleAppReminder = findViewById(R.id.toggle_app_reminder);
        toggleMovieReminder = findViewById(R.id.toggle_movie_reminder);
        toggleAppReminder.setTextOff(null);
        toggleAppReminder.setTextOn(null);
        toggleAppReminder.setText(null);
        toggleMovieReminder.setTextOff(null);
        toggleMovieReminder.setTextOn(null);
        toggleMovieReminder.setText(null);

        final SettingPreferences settingPreferences = new SettingPreferences(this);
        final Setting setting = settingPreferences.getSetting();

        if (setting.getAppDailyReminder() == 1) {
            toggleAppReminder.setChecked(true);
        } else {
            toggleAppReminder.setChecked(false);
        }
        if (setting.getMovieDailyReminder() == 1) {
            toggleMovieReminder.setChecked(true);
        } else {
            toggleMovieReminder.setChecked(false);
        }

        alarmReceiver = new AlarmReceiver();

        toggleAppReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alarmReceiver.setRepeatingAlarm(getApplicationContext(), AlarmReceiver.TYPE_APP_ALARM);
                    settingPreferences.setAppDailyReminder(1);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_set_app_alarm), Toast.LENGTH_SHORT).show();
                } else {
                    alarmReceiver.cancelAlarm(getApplicationContext(), AlarmReceiver.TYPE_APP_ALARM);
                    settingPreferences.setAppDailyReminder(0);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_cancel_app_alarm), Toast.LENGTH_SHORT).show();
                }
            }
        });

        toggleMovieReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alarmReceiver.setRepeatingAlarm(getApplicationContext(), AlarmReceiver.TYPE_MOVIE_ALARM);
                    settingPreferences.setMovieDailyReminder(1);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_set_movie_alarm), Toast.LENGTH_SHORT).show();
                } else {
                    alarmReceiver.cancelAlarm(getApplicationContext(), AlarmReceiver.TYPE_MOVIE_ALARM);
                    settingPreferences.setMovieDailyReminder(0);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_cancel_movie_alarm), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
