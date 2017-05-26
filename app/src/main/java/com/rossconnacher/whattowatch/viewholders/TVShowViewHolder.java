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

public class TVShowViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "TVShowViewHolder";

    @InjectView(R.id.showTitle)
    public TextView showTitle;
    @InjectView(R.id.showSource)
    public TextView showSource;
    @InjectView(R.id.showCast)
    public TextView showCast;
    @InjectView(R.id.showOverview)
    public TextView showOverview;
    @InjectView(R.id.showRating)
    public TextView showRating;
    @InjectView(R.id.showImage)
    public ImageView showImage;


    public TVShowViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this,itemView);
    }
}
