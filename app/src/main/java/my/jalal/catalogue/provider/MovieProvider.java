package my.jalal.catalogue.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import my.jalal.catalogue.db.CatalogueHelper;

import static my.jalal.catalogue.db.DatabaseContract.AUTHORITY;
import static my.jalal.catalogue.db.DatabaseContract.TABLE_NAME;


public class MovieProvider extends ContentProvider {
    private static final int FAVORITE = 1;
    private CatalogueHelper catalogueHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        //content://my.jalal.catalogue/favorite
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITE);

    }

    @Override
    public boolean onCreate() {
        catalogueHelper = CatalogueHelper.getInstance(getContext());
        catalogueHelper.open();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        if(sUriMatcher.match(uri) == FAVORITE){
            cursor = catalogueHelper.queryAllFavoriteMovie();
        }else{
            cursor = null;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }


    public MovieProvider() {
    }


}
