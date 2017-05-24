package com.rossconnacher.whattowatch;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ross on 5/23/2017.
 */

public interface WhatToWatchService {

    /* authenticate for the MovieDB
    @GET("/3/authentication/token/new")
    Call<String> authenticate(@Query("api_key") String apiKey);
    */
    @GET("/v2/channels")
    Call<String> getAllChannels(@Query("type") String type,@Query("limit") int limit,@Query("offset") int offset,@Query("api_key") String apiKey);

    @GET("/v2/channels/{id}")
    Call<String> getChannelData(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("/v2/shows/")
    Call<String> getShowsForChannel(@Query("id") int id, @Query("api_key") String apiKey);

    @GET("/v2/movies/")
    Call<String> getMoviesForSources(@Query("sources") String sources,@Query("limit") int limit, @Query("api_key") String apiKey);

    @GET("/v2/movies/{id}")
    Call<String> getMovie(@Path("id") int id, @Query("api_key") String apiKey);

    //filter - valid options include: thumbnails, posters, banners, backgrounds
    @GET("/v2/movies/{id}/images")
    Call<String> getMovieImages(@Path("id") int id,@Query("filter") String filter, @Query("api_key") String apiKey);

    @GET("/v2/movies/{id}/related")
    Call<String> getRelatedMovies(@Path("id") int id, @Query("api_key") String apiKey);

    //do I need to getMovieVideos(trailers)
    @GET("/v2/genres/")
    Call<String> getGenres(@Query("api_key") String apiKey);

    @GET("/v2/regions/")
    Call<String> getRegions(@Query("api_key") String apiKey);

    //maybe add retrieve all credits for single person

}

