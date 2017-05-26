package com.rossconnacher.whattowatch.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rossconnacher.whattowatch.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Ross on 5/24/2017.
 */

public class MovieViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "MovieViewHolder";

    @InjectView(R.id.movieSource)
    public TextView movieSource;
    @InjectView(R.id.movieTitle)
    public TextView movieTitle;
    @InjectView(R.id.movieCast)
    public TextView movieCast;
    @InjectView(R.id.movieOverview)
    public TextView movieOverview;
    @InjectView(R.id.movieRating)
    public TextView movieRating;
    @InjectView(R.id.movieImage)
    public ImageView movieImage;

    public MovieViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this,itemView);
    }
}
