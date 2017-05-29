package com.rossconnacher.whattowatch;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        SelectSourceFragment.OnFragmentInteractionListener,
        FilterFragment.OnFragmentInteractionListener,
        SearchResultFragment.OnFragmentInteractionListener,
        MoviesTVFragment.OnFragmentInteractionListener,
        LoadingFragment.OnFragmentInteractionListener,
        WelcomeFragment.OnFragmentInteractionListener
{

    private static final String TAG = "MainActivity";
    private String requestToken;

    private int NetflixChannel = 202;
    private int HBOChannel = 36;
    private int AmazonChannel = 140;
    private int HuluChannel = 211;
    private int CrunchyrollChannel = 1732;
    private int CrackleChannel = 107;
    private int TubiTVChannel = 1441;

    private WhatToWatchEngine mEngine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        mEngine = new WhatToWatchEngine();
        //getChannelData(AmazonChannel);
        getGenres();

        Fragment welcomeFrag = new WelcomeFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentFrame, welcomeFrag).commit();

    }
    public void getMovie(int movieID){
        Call<String> call = mEngine.getMovie(movieID);
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


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void getGenres() {
        Call<String> call = mEngine.getGenres();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "GetGenres Success: " + response.toString());
                Log.d(TAG, "GetGenres Success: " + response.body());
                JSONObject jsonObj = null;
                if(response.body()!=null){
                    try {
                        jsonObj = new JSONObject(response.body());
                        SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                        editor.putString("GenreData",jsonObj.toString());
                        editor.commit();
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
