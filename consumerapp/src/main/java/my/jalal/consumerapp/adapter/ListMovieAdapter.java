package my.jalal.consumerapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import my.jalal.consumerapp.R;
import my.jalal.consumerapp.model.Movie;


public class ListMovieAdapter extends RecyclerView.Adapter<ListMovieAdapter.ListViewHolder> {
    private ArrayList<Movie> listMovie = new ArrayList<>();

    public ListMovieAdapter(){

    }
    public void setData(ArrayList<Movie> items) {
        listMovie.clear();
        listMovie.addAll(items);
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_movie, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListMovieAdapter.ListViewHolder holder, final int position) {
        final Movie movie = listMovie.get(position);
        holder.score.setText(movie.getScore());
        holder.name.setText(movie.getName());
        holder.description.setText(movie.getDescription());
        Picasso.get()
                .load(movie.getPhotoUrl())
                .placeholder(R.drawable.ic_broken_image_black_24dp)
                .into(holder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        return listMovie.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView name, score, description;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photo);
            name = itemView.findViewById(R.id.tv_name);
            score = itemView.findViewById(R.id.tv_score);
            description = itemView.findViewById(R.id.tv_description);
        }
    }
}
