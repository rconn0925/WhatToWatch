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

public class MediaViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "MediaViewHolder";


    @InjectView(R.id.mediaTitle)
    public TextView mediaTitle;
    @InjectView(R.id.mediaOverview)
    public TextView mediaOverview;
    @InjectView(R.id.mediaRating)
    public TextView mediaRating;
    @InjectView(R.id.mediaImage)
    public ImageView mediaImage;

    public MediaViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this,itemView);
    }
}
