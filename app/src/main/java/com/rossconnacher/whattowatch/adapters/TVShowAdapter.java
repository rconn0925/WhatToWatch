package com.rossconnacher.whattowatch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rossconnacher.whattowatch.R;
import com.rossconnacher.whattowatch.models.Movie;
import com.rossconnacher.whattowatch.models.TVShow;
import com.rossconnacher.whattowatch.viewholders.TVShowViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ross on 5/24/2017.
 */

public class TVShowAdapter extends RecyclerView.Adapter<TVShowViewHolder>{

    private Context mContext;
    private List<TVShow> mShows;

    public TVShowAdapter(Context context, ArrayList<TVShow> shows){
        this.mContext = context;
        this.mShows = shows;
    }

    @Override
    public TVShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflator = LayoutInflater.from(parent.getContext());
        View view = inflator.inflate(R.layout.tvshow_item, parent, false);
        return new TVShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TVShowViewHolder holder, int position) {
        final TVShow show = mShows.get(position);
        holder.showOverview.setText(show.getOverview());
        holder.showTitle.setText(show.getTitle());
        holder.showRating.setText(show.getRating());
        //Picasso image setting will probably need changing
        Picasso.with(mContext).load(show.getImageUrl()).into(holder.showImage);
    }

    @Override
    public int getItemCount() {
        return mShows.size();
    }
}
