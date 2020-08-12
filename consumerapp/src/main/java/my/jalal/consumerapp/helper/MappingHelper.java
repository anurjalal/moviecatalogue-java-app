package my.jalal.consumerapp.helper;

import android.database.Cursor;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

public class MappingHelper {
    public static ArrayList<String> mapCursorToArrayList(Cursor catalogueCursor) {
        ArrayList<String> listFavoriteId = new ArrayList<>();

        while (catalogueCursor.moveToNext()) {
            String id = catalogueCursor.getString(catalogueCursor.getColumnIndexOrThrow(_ID));
            listFavoriteId.add(id);
        }
        return listFavoriteId;
    }
}
