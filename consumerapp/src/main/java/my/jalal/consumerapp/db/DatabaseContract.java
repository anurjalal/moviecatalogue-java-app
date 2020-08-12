package my.jalal.consumerapp.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static String TABLE_NAME = "favorite";
    public static final String AUTHORITY = "my.jalal.catalogue";
    private static final String SCHEME = "content";

    private DatabaseContract() {

    }

    public static final class FavoriteColumn implements BaseColumns {
        public static String ISFAVORITE = "isFavorite";
        public static String TYPE = "type";
        public static String IMAGE_URL = "image_url";

        //untuk membuat URI content://my.jalal.catalogue/favorite
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }
}