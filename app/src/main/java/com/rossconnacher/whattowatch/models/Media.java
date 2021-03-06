package com.rossconnacher.whattowatch.models;

/**
 * Created by Ross on 5/26/2017.
 */

import java.io.Serializable;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Parcelable;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ross on 5/24/2017.
 */

public class Media implements Serializable {

    private String title;
    private String rating;
    private String overview;
    private String imageUrl;

    private int id;
    private String source;


    public Media(String title, int id, String source, String rating, String overview, String imageUrl){
        this.title = title;
        this.rating = rating;
        this.overview = overview;
        this.imageUrl = imageUrl;
        this.id = id;
        this.source = source;
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
    public String getSource() {return source;}
    public int getId() {return id;}

}
