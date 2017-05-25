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

    //TheMovieDB
    //private static final String API_KEY = "aa62041da3e4dcc87c66379191390a3b";
    private static final String API_KEY = "8a2a11a34a589b80b304923b01e7908bfd56978b";
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
    public Call<String> getMoviesForSources(String[] sources){
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
        return mService.getMoviesForSources(sourceString, 1,0,API_KEY);
    }

    public Call<String> getMovie(int movieID){
        return mService.getMovie(movieID,API_KEY);
    }
    public Call<String> getMovieImages(int movieID,String filter){
        return mService.getMovieImages(movieID,filter,API_KEY);
    }
    public Call<String> getRelatedMovies(int movieID){
        return mService.getRelatedMovies(movieID,API_KEY);
    }
    public Call<String> getGenres(){
        return mService.getGenres(API_KEY);
    }
    public Call<String> getRegions(){
        return mService.getRegions(API_KEY);
    }

    public Call<String> getTVShowsForSources(String[] sources) {
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
        return mService.getTVShowsForSources(sourceString, 1,0,API_KEY);
    }
}
