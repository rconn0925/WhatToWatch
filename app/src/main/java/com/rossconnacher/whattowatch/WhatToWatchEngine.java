package com.rossconnacher.whattowatch;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Ross on 5/23/2017.
 */

public class WhatToWatchEngine {

    private WhatToWatchService mService;
    //This is the original key
    //private static final String API_KEY = "8a2a11a34a589b80b304923b01e7908bfd56978b";

    //This is the 2nd key
    private static final String API_KEY = "b7f02f3cd0d18e233ec086a835aa6fe113896e15";
    public WhatToWatchEngine() {
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("https://api.themoviedb.org/3/")
                .baseUrl("http://api-public.guidebox.com/v2/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        mService = retrofit.create(WhatToWatchService.class);
    }
/*
    public Call<String> authenticate(){
        return mService.authenticate(API_KEY);
    }
    */
    public Call<String> getChannelData(int channelID){
        return mService.getChannelData(channelID,API_KEY);
    }

    public Call<String> getShowsForChannel(int channelID){
        return mService.getShowsForChannel(channelID,API_KEY);
    }
    public Call<String> getMoviesForSources(int numItemsToFetch, int offset, String[] sources){
        String sourceString = "";
        for(int i = 0; i<sources.length;i++)
        {
            if(i==sources.length-1){
                sourceString += sources[i];
            }
            else{
                sourceString += (sources[i]+",");
            }
        }
        return mService.getMoviesForSources(sourceString, numItemsToFetch,offset,API_KEY);
    }

    public Call<String> getMovie(int movieID){
        return mService.getMovie(movieID,API_KEY);
    }
    public Call<String> getMovieImages(int movieID,String filter){
        return mService.getMovieImages(movieID,filter,API_KEY);
    }
    public Call<String> getRelatedMovies(int movieID,int numItemsToFetch, int offset, String[] sources){
        String sourceString = "";
        for(int i = 0; i<sources.length;i++)
        {
            if(i==sources.length-1){
                sourceString += sources[i];
            }
            else{
                sourceString += (sources[i]+",");
            }
        }
        return mService.getRelatedMovies(movieID,sourceString,numItemsToFetch,offset,API_KEY);
    }
    public Call<String> getRelatedShows(int showID,int numItemsToFetch, int offset, String[] sources){
        String sourceString = "";
        for(int i = 0; i<sources.length;i++)
        {
            if(i==sources.length-1){
                sourceString += sources[i];
            }
            else{
                sourceString += (sources[i]+",");
            }
        }
        return mService.getRelatedShows(showID,sourceString,numItemsToFetch,offset,API_KEY);
    }
    public Call<String> getGenres(){
        return mService.getGenres(API_KEY);
    }
    public Call<String> getRegions(){
        return mService.getRegions(API_KEY);
    }

    public Call<String> getTVShowsForSources(int numItemsToFetch, int offset, String[] sources) {
        String sourceString = "";
        for(int i = 0; i<sources.length;i++)
        {
            if(i==sources.length-1){
                sourceString += sources[i];
            }
            else{
                sourceString += (sources[i]+",");
            }
        }
        return mService.getTVShowsForSources(sourceString, numItemsToFetch,offset,API_KEY);
    }


}
