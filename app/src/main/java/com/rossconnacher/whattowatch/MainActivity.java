package com.rossconnacher.whattowatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private String requestToken;

    private int NetflixChannel = 202;
    private int HBOChannel = 36;
    private int Amazon_PrimeChannel = 140;
    private int HuluChannel = 211;
    private int CrunchyrollChannel = 1732;

    private JSONObject NetflixChannelData = null;
    private JSONObject HBOChannelData = null;
    private JSONObject Amazon_PrimeChannelData = null;
    private JSONObject CrunchyrollChannelData = null;
    private JSONObject HuluChannelData = null;

    private WhatToWatchEngine mEngine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEngine = new WhatToWatchEngine();
        getChannelData(NetflixChannel);

        String[] sources = new String[]{"crunchyroll"};
        getMovies(sources);

    }

    public void getChannelData(int channelID){
        Call<String> call = mEngine.getChannelData(channelID);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "GetChannelData Success: " + response.toString());
                Log.d(TAG, "GetChannelData Success: " + response.body());
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(response.body());
                } catch(JSONException e) {

                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG,"Get channel data fail");
            }
        });

    }

    public void getShowsForChannel(int channelID){
        Call<String> call = mEngine.getShowsForChannel(channelID);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "GetShowsForChannel Success: " + response.toString());
                Log.d(TAG, "GetShowsForChannel Success: " + response.body());
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(response.body());
                } catch(JSONException e) {

                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG,"GetShowsForChannel fail");
            }
        });
    }
    public void getMovies(String[] sources){
        Call<String> call = mEngine.getMoviesForSources(sources);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "GetMovies Success: " + response.toString());
                Log.d(TAG, "GetMovies Success: " + response.body());
                JSONObject jsonObj = null;
                if(response.body()!=null){
                    try {
                        jsonObj = new JSONObject(response.body());
                    } catch(JSONException e) {

                    }
                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG,"GetMovies fail");
            }
        });
    }


}
