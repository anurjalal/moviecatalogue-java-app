package my.jalal.catalogue.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "dbCatalogue";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_NOTE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY,"
                    + "%s TEXT NOT NULL,"
                    + "%s TEXT NOT NULL,"
                    + "%s TEXT NOT NULL)",
            DatabaseContract.TABLE_NAME,
            DatabaseContract.FavoriteColumn._ID,
            DatabaseContract.FavoriteColumn.TYPE,
            DatabaseContract.FavoriteColumn.ISFAVORITE,
            DatabaseContract.FavoriteColumn.IMAGE_URL
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_NAME);
        onCreate(db);
    }
}