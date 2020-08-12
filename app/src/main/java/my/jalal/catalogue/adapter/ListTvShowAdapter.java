package my.jalal.catalogue.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import my.jalal.catalogue.R;
import my.jalal.catalogue.model.TvShow;

public class ListTvShowAdapter extends RecyclerView.Adapter<ListTvShowAdapter.ListViewHolder> {
    private ArrayList<TvShow> listTvShow = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public void setData(ArrayList<TvShow> items) {
        listTvShow.clear();
        listTvShow.addAll(items);
        notifyDataSetChanged();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_tv_show, viewGroup, false);
        return new ListTvShowAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListTvShowAdapter.ListViewHolder holder, int position) {
        final TvShow tvShow = listTvShow.get(position);
        holder.score.setText(tvShow.getScore());
        holder.name.setText(tvShow.getName());
        holder.description.setText(tvShow.getDescription());
        Picasso.get()
                .load(tvShow.getPhotoUrl())
                .placeholder(R.drawable.ic_broken_image_black_24dp)
                .into(holder.imgPhoto);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(tvShow);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTvShow.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView score, name, description;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photo);
            name = itemView.findViewById(R.id.tv_name);
            score = itemView.findViewById(R.id.tv_score);
            description = itemView.findViewById(R.id.tv_description);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(TvShow data);
    }
}