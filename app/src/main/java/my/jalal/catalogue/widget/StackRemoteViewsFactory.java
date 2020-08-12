package my.jalal.catalogue.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import my.jalal.catalogue.R;
import my.jalal.catalogue.db.CatalogueHelper;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<Bitmap> mWidgetItems;
    private final Context mContext;
    CatalogueHelper catalogueHelperFavorite;

    StackRemoteViewsFactory(Context context) {
        mContext = context;

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mWidgetItems = new ArrayList<>();
        catalogueHelperFavorite = new CatalogueHelper(mContext);
        ArrayList<String> moviePosterURLs = catalogueHelperFavorite.getAllMoviePosterURL(catalogueHelperFavorite);
            try {
                for (String moviePosterURL : moviePosterURLs) {
                    URL url = new URL(moviePosterURL);
                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    mWidgetItems.add(image);
                }
            }catch(IOException e) {
                System.out.println(e.toString());
            }
        }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        if(!mWidgetItems.isEmpty()) {
            rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(position));
            Bundle extras = new Bundle();
            extras.putInt(ImageBannerWidget.EXTRA_ITEM, position);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
