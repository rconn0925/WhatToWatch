package com.rossconnacher.whattowatch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rossconnacher.whattowatch.R;
import com.rossconnacher.whattowatch.models.Movie;
import com.rossconnacher.whattowatch.viewholders.MovieViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ross on 5/24/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private Context mContext;
    private List<Movie> mMovies;

    public MovieAdapter(Context context, ArrayList<Movie> movies){
        this.mContext = context;
        this.mMovies = movies;
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflator = LayoutInflater.from(parent.getContext());
        View view = inflator.inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie = mMovies.get(position);
        holder.movieOverview.setText(movie.getOverview());
        holder.movieTitle.setText(movie.getTitle());
        holder.movieRating.setText(movie.getRating());
        //Picasso image setting will probably need changing
        Picasso.with(mContext).load(movie.getImageUrl()).into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }
}
