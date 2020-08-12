package my.jalal.catalogue.ui.tv_show_detail;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import my.jalal.catalogue.R;
import my.jalal.catalogue.db.CatalogueHelper;
import my.jalal.catalogue.model.TvShow;

import static android.provider.BaseColumns._ID;
import static my.jalal.catalogue.db.DatabaseContract.FavoriteColumn.IMAGE_URL;
import static my.jalal.catalogue.db.DatabaseContract.FavoriteColumn.ISFAVORITE;
import static my.jalal.catalogue.db.DatabaseContract.FavoriteColumn.TYPE;

public class TvShowDetailActivity extends AppCompatActivity {
    public static final String EXTRA_DATA = "extra_tv_show";
    private ProgressBar progressBar;
    private ImageView imgPhoto;
    private TextView name;
    private TextView score;
    private TextView status;
    private TextView description;
    private TvShowDetailViewModel tvShowDetailViewModel;
    private ToggleButton toggleButtonFavorite;
    private CatalogueHelper catalogueHelper;
    private final String CONTENT_TYPE = "TVSHOW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_detail);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        init();
        showLoading(true);
        catalogueHelper = new CatalogueHelper(getApplicationContext());
        TvShow tvShow = getIntent().getParcelableExtra(EXTRA_DATA);
        final int id = tvShow.getId();
        final String url = tvShow.getPhotoUrl();
        final String language = Locale.getDefault().toLanguageTag();
        tvShowDetailViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(TvShowDetailViewModel.class);
        tvShowDetailViewModel.setTvShow(language, id);
        tvShowDetailViewModel.getTvShow().observe(this, new Observer<TvShow>() {
            @Override
            public void onChanged(TvShow tvShow) {
                showLoading(true);
                Picasso.get()
                        .load(tvShow.getPhotoUrl())
                        .placeholder(R.drawable.ic_broken_image_black_24dp)
                        .into(imgPhoto);
                name.setText(tvShow.getName());
                score.setText(tvShow.getScore());
                status.setText(tvShow.getStatus());
                description.setText(tvShow.getDescription());
                showLoading(false);
            }
        });
        toggleFavoriteAction(id, url);
    }


    private void init() {
        progressBar = findViewById(R.id.progressBar);
        imgPhoto = findViewById(R.id.img_photo);
        name = findViewById(R.id.tv_name);
        score = findViewById(R.id.tv_score);
        status = findViewById(R.id.tv_status);
        description = findViewById(R.id.tv_description);
        toggleButtonFavorite = findViewById(R.id.toggle_favorite);
    }

    private void toggleFavoriteAction(final int id, final String imageUrl) {
        if (!catalogueHelper.isEmpty(catalogueHelper, id)) {
            if (catalogueHelper.isFavorite(catalogueHelper, id)) {
                toggleButtonFavorite.setChecked(true);
            } else {
                toggleButtonFavorite.setChecked(false);
            }
        } else {
            toggleButtonFavorite.setChecked(false);
            ContentValues values = new ContentValues();
            values.put(_ID, id);
            values.put(ISFAVORITE, 0);
            values.put(TYPE, CONTENT_TYPE);
            values.put(IMAGE_URL, imageUrl);
            catalogueHelper.open();
            catalogueHelper.insert(values);
        }
        toggleButtonFavorite.setText(null);
        toggleButtonFavorite.setTextOff(null);
        toggleButtonFavorite.setTextOn(null);
        toggleButtonFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                catalogueHelper.open();
                if (isChecked) {
                    ContentValues values = new ContentValues();
                    values.put(ISFAVORITE, 1);
                    long result = catalogueHelper.update(Integer.toString(id), values);
                    if (result > 0) {
                        Toast.makeText(TvShowDetailActivity.this, R.string.query_success_insert_favorite_dialogue, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(TvShowDetailActivity.this, R.string.query_fail_insert_favorite_dialogue, Toast.LENGTH_LONG).show();
                        toggleButtonFavorite.setChecked(false);
                    }
                } else {
                    ContentValues values = new ContentValues();
                    values.put(ISFAVORITE, 0);
                    long result = catalogueHelper.update(Integer.toString(id), values);
                    if (result > 0) {
                        Toast.makeText(TvShowDetailActivity.this, R.string.query_success_remove_favorite_dialogue, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(TvShowDetailActivity.this, R.string.query_fail_remove_favorite_dialogue, Toast.LENGTH_LONG).show();
                        toggleButtonFavorite.setChecked(true);
                    }
                }
            }
        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }


}
