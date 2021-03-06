// This code is created by Ahmad Nurjalal --> github.com/anurjalal
package my.jalal.catalogue.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import my.jalal.catalogue.model.Setting;
import my.jalal.catalogue.preferences.SettingPreferences;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SettingPreferences settingPreferences = new SettingPreferences(context);
            Setting setting = settingPreferences.getSetting();
            AlarmReceiver alarmReceiver = new AlarmReceiver(); //must be instanced here
            if (setting.getAppDailyReminder() == 1) {
                alarmReceiver.setRepeatingAlarm(context.getApplicationContext(), AlarmReceiver.TYPE_APP_ALARM);
            }
            if (setting.getMovieDailyReminder() == 1) {
                alarmReceiver.setRepeatingAlarm(context.getApplicationContext(), AlarmReceiver.TYPE_MOVIE_ALARM);
            }
        }
    }
}
