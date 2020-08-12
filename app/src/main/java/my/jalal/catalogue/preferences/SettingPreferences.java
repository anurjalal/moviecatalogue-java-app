package my.jalal.catalogue.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import my.jalal.catalogue.model.Setting;

public class SettingPreferences {

    private static final String PREFS_NAME = "setting_pref";
    private static final String APP_DAILY_REMINDER = "app_daily_reminder";
    private static final String MOVIE_DAILY_REMINDER = "movie_daily_reminder";
    private final SharedPreferences preferences;

    public SettingPreferences(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setAppDailyReminder(int value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(APP_DAILY_REMINDER, value);
        editor.apply();
    }

    public void setMovieDailyReminder(int value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(MOVIE_DAILY_REMINDER, value);
        editor.apply();
    }

    public Setting getSetting(){
        Setting setting = new Setting();
        setting.setAppDailyReminder(preferences.getInt(APP_DAILY_REMINDER,0));
        setting.setMovieDailyReminder(preferences.getInt(MOVIE_DAILY_REMINDER, 0));
        return setting;
    }

}
