package com.rossconnacher.whattowatch.adapters;

/**
 * Created by Ross on 5/26/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rossconnacher.whattowatch.R;
import com.rossconnacher.whattowatch.models.Media;
import com.rossconnacher.whattowatch.models.Movie;
import com.rossconnacher.whattowatch.viewholders.MediaViewHolder;
import com.rossconnacher.whattowatch.viewholders.MovieViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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

public class MediaAdapter extends RecyclerView.Adapter<MediaViewHolder> {

    private Context mContext;
    private List<Media> mMedias;

    public MediaAdapter(Context context, ArrayList<Media> medias){
        this.mContext = context;
        this.mMedias = medias;
    }


    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflator = LayoutInflater.from(parent.getContext());
        View view = inflator.inflate(R.layout.media_item, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MediaViewHolder holder, int position) {
        final Media media = mMedias.get(position);
        holder.mediaOverview.setText(media.getOverview());
        holder.mediaTitle.setText(media.getTitle());
        holder.mediaRating.setText(media.getRating());
        //Picasso image setting will probably need changing
        Picasso.with(mContext).load(media.getImageUrl()).into(holder.mediaImage);
    }

    @Override
    public int getItemCount() {
        return mMedias.size();
    }
}
