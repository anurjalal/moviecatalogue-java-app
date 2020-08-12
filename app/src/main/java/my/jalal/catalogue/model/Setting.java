package my.jalal.catalogue.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Setting implements Parcelable {

    public Setting() {

    }

    int appDailyReminder;
    int movieDailyReminder;

    public int getAppDailyReminder() {
        return appDailyReminder;
    }

    public void setAppDailyReminder(int appDailyReminder) {
        this.appDailyReminder = appDailyReminder;
    }

    public int getMovieDailyReminder() {
        return movieDailyReminder;
    }

    public void setMovieDailyReminder(int movieDailyReminder) {
        this.movieDailyReminder = movieDailyReminder;
    }

    public static final Creator<Setting> CREATOR = new Creator<Setting>() {
        @Override
        public Setting createFromParcel(Parcel in) {
            return new Setting(in);
        }

        @Override
        public Setting[] newArray(int size) {
            return new Setting[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    protected Setting(Parcel in) {
        appDailyReminder = in.readInt();
        movieDailyReminder = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(appDailyReminder);
        dest.writeInt(movieDailyReminder);
    }
}
