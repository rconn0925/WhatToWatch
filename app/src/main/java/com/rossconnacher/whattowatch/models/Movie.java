package com.rossconnacher.whattowatch.models;

import android.net.Uri;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ross on 5/24/2017.
 */

public class Movie {

    private String title;
    private String rating;
    private String overview;
    private ArrayList<String> cast;
    private String imageUrl;
    private String source;


    public Movie(String source, String title, String rating, String overview, ArrayList<String> cast, String imageUrl){
        this.title = title;
        this.rating = rating;
        this.overview = overview;
        this.cast = cast;
        this.imageUrl = imageUrl;
        this.source = source;
    }

    public String getImageUrl(){
        return imageUrl;
    }
    public String getTitle(){
        return title;
    }
    public ArrayList<String> getCast(){
        return cast;
    }
    public String getRating(){
        return rating;
    }
    public String getOverview(){
        return overview;
    }
    public String getSource(){
        return source;
    }

}
