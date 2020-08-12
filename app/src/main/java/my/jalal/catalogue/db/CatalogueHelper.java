package my.jalal.catalogue.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static my.jalal.catalogue.db.DatabaseContract.FavoriteColumn.IMAGE_URL;
import static my.jalal.catalogue.db.DatabaseContract.FavoriteColumn.ISFAVORITE;
import static my.jalal.catalogue.db.DatabaseContract.FavoriteColumn.TYPE;
import static my.jalal.catalogue.db.DatabaseContract.TABLE_NAME;

public class CatalogueHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DatabaseHelper dataBaseHelper;
    private static CatalogueHelper INSTANCE;
    private static SQLiteDatabase database;

    public CatalogueHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static CatalogueHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CatalogueHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
            database = dataBaseHelper.getReadableDatabase();
    }

    public void close() {
        dataBaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    public Cursor queryAllFavoriteMovie() {
        return database.query(
                DATABASE_TABLE,
                null,
                TYPE + " =?" + " AND " + ISFAVORITE + " = ?",
                new String[]{"MOVIE", String.valueOf(1)},
                null,
                null,
                null,
                null);
    }

    public Cursor queryAll() {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC");
    }

    public Cursor queryById(int id) {
        return database.query(
                DATABASE_TABLE,
                null,
                _ID + " = ?",
                new String[]{Integer.toString(id)},
                null,
                null,
                null,
                null);
    }

    public long insert(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int update(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }

    public ArrayList<String> getAllMoviePosterURL(CatalogueHelper catalogueHelper) {
        catalogueHelper.open();
        ArrayList<String> listMoviePoster = new ArrayList<>();
        try (Cursor c = database.rawQuery("SELECT " + IMAGE_URL + " FROM " + TABLE_NAME + " WHERE " + TYPE + " = ? AND " + ISFAVORITE + " = ?", new String[]{"MOVIE", String.valueOf(1)})) {
            while (c.moveToNext()) {
                listMoviePoster.add(c.getString(c.getColumnIndex(IMAGE_URL)));
            }
        }
        catalogueHelper.close();
        return listMoviePoster;
    }

    public ArrayList<String> getAllFavoriteId(CatalogueHelper catalogueHelper, String type) {
        catalogueHelper.open();
        ArrayList<String> listID = new ArrayList<>();
        try (Cursor c = database.rawQuery("SELECT " + _ID + " FROM " + TABLE_NAME + " WHERE " + TYPE + " = ? AND " + ISFAVORITE + " = ?", new String[]{type, String.valueOf(1)})) {
            while (c.moveToNext()) {
                listID.add(c.getString(c.getColumnIndex(_ID)));
            }
        }
        catalogueHelper.close();
        return listID;
    }

    public boolean isEmpty(CatalogueHelper catalogueHelper, int id) {
        catalogueHelper.open();
        try {
            Cursor c = catalogueHelper.queryById(id);
            if (c != null && c.moveToFirst()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        catalogueHelper.close();
        return true;
    }

    public boolean isFavorite(CatalogueHelper catalogueHelper, int id) {
        catalogueHelper.open();
        try {
            Cursor c = catalogueHelper.queryById(id);
            if (c != null && c.moveToFirst()) {
                int isFavorite = c.getInt(c.getColumnIndex(ISFAVORITE));
                if (isFavorite == 1) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        catalogueHelper.close();
        return false;
    }


    public int deleteById(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }
}
