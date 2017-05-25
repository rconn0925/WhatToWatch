package com.rossconnacher.whattowatch.models;

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
    private URL imageUrl;


    public Movie(String title, String rating, String overview, ArrayList<String> cast, URL imageUrl){
        this.title = title;
        this.rating = rating;
        this.overview = overview;
        this.cast = cast;
        this.imageUrl = imageUrl;
    }
}
