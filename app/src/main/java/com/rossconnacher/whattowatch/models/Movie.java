package com.rossconnacher.whattowatch.models;

import android.net.Uri;
import android.os.Parcelable;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ross on 5/24/2017.
 */

public class Movie implements Serializable{

    private String title;
    private String rating;
    private String overview;
    private String imageUrl;


    public Movie(String title, String rating, String overview, ArrayList<String> cast, String imageUrl){
        this.title = title;
        this.rating = rating;
        this.overview = overview;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl(){
        return imageUrl;
    }
    public String getTitle(){
        return title;
    }
    public String getRating(){
        return rating;
    }
    public String getOverview(){
        return overview;
    }

}
