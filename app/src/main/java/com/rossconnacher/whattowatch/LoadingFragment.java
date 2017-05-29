package com.rossconnacher.whattowatch;

import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rossconnacher.whattowatch.models.Media;
import com.rossconnacher.whattowatch.models.Movie;
import com.rossconnacher.whattowatch.models.TVShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoadingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoadingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoadingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "LoadingFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private static final String ARG_PARAM7 = "param7";
    private static final String ARG_PARAM8 = "param8";
    // TODO: Rename and change types of parameters
    private int numDataElements;

    private OnFragmentInteractionListener mListener;
    private String[] mSources;
    private boolean isMovie;
    private String mGenre;
    private String mRating;
    private String mActorName;
    private String mRelatedTo;
    private boolean hasActor;
    private int mTitleID;
    private int allDataLength;
    private Context mContext;
  //  private ArrayList<Movie> mMovies;
   // private ArrayList<TVShow> mShows;
    private ArrayList<Media> mMedia;
    private JSONArray allData;
    private ArrayList<Integer> allDataIDs;
    private ArrayList<String> allDataSources;
    private int numItemsInFilteredData;

    private WhatToWatchEngine mEngine;
    private FragmentManager fragmentManager;

    public LoadingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment LoadingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoadingFragment newInstance(boolean isMovie,int numDataElements,String genre,boolean hasActor,String actorName,String relatedTo, String rating, String[] sources) {
        LoadingFragment fragment = new LoadingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, numDataElements);
        args.putStringArray(ARG_PARAM2,sources);
        args.putBoolean(ARG_PARAM3,isMovie);
        args.putBoolean(ARG_PARAM4, hasActor);
        args.putString(ARG_PARAM5,actorName);
        args.putString(ARG_PARAM6,relatedTo);
        args.putString(ARG_PARAM7,rating);
        args.putString(ARG_PARAM8,genre);
        fragment.setArguments(args);
        Log.d(TAG, "args: " + isMovie+ " "+numDataElements);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            numDataElements = getArguments().getInt(ARG_PARAM1);
            mSources = getArguments().getStringArray(ARG_PARAM2);
            isMovie = getArguments().getBoolean(ARG_PARAM3);
            hasActor = getArguments().getBoolean(ARG_PARAM4);
            mActorName = getArguments().getString(ARG_PARAM5);
            mRelatedTo = getArguments().getString(ARG_PARAM6);
            mRelatedTo = mRelatedTo.replaceAll("\\s+","");
            mRating = getArguments().getString(ARG_PARAM7);
            mGenre = getArguments().getString(ARG_PARAM8);
            Log.d(TAG, "args relatedTo: " + mRelatedTo);
        }
        allData = new JSONArray();
        allDataIDs = new ArrayList<Integer>();
        allDataSources = new ArrayList<String>();
        mEngine = new WhatToWatchEngine();
       // mShows = new ArrayList<>();
        //mMovies = new ArrayList<>();
        mMedia = new ArrayList<>();
        mContext = getActivity();
        allDataLength = 0;
        numItemsInFilteredData =0;
        fragmentManager =getFragmentManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loading, container, false);
        ButterKnife.inject(this,view);
        search(isMovie,mRelatedTo);
        /*
        if(isMovie){
            getAllMoviesForSources();
        } else {
            getAllTVShowsForSources();
        }
        */
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    public void search(final boolean isMovie, String title){
        Call<String> call = mEngine.search(isMovie,title);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "search Success: " + response.toString());
                Log.d(TAG, "search Success: " + response.body());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body());
                    JSONArray resultsJson = jsonObject.getJSONArray("results");
                    if(resultsJson.length()==0){
                        Log.d(TAG, "search resultJSON is empty");
                        mTitleID = -1;
                        Toast.makeText(getActivity(), "No results. Please try again.",
                                Toast.LENGTH_LONG).show();

                        Fragment filterFrag = FilterFragment.newInstance(isMovie,mSources);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contentFrame, filterFrag).commit();
                    } else {
                        Log.d(TAG, "search resultJSON" +resultsJson);
                        int id = resultsJson.getJSONObject(0).getInt("id");
                        Log.d(TAG, "search titleID: " +id);
                        mTitleID = id;
                        getRelatedMediaForSources();
                    }


                } catch(JSONException e) {
                    Log.w(TAG, e.toString());
                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG,"Get channel data fail");
            }
        });
    }

    public void getRelatedMediaForSources(){
        Call<String> call;
        if(isMovie){
            call = mEngine.getRelatedMovies(mTitleID,mSources);
        } else {
            call = mEngine.getRelatedShows(mTitleID,mSources);
        }
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "getRelatedMoviesForSources Success: " + response.toString());
                Log.d(TAG, "getRelatedMoviesForSources Success: " + response.body());
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(response.body());
                    JSONArray resultsJson = jsonObj.getJSONArray("results");
                    Log.d(TAG, "resultJSON" + resultsJson.toString());
                    allData = resultsJson;
                    allDataLength = allData.length();

                    for (int i = 0; i < allData.length(); i++) {
                        String strID = (((JSONObject) allData.get(i)).get("id")).toString();
                        Log.d(TAG, "data" + i + ": " + strID);
                        int id = Integer.parseInt(strID);
                        getMediaData(i,id);
                        /*
                        //check if ID is in cache
                        if(isMovie){
                            for (int j = 0; j<mSources.length;j++){
                                String jsonString;
                                try {
                                    jsonString = Cache.readObject(mContext,mSources[j]+"movie").toString();
                                    numItemsInFilteredData +=1;
                                    if(jsonString.contains("\"id\":"+id)){
                                        allDataIDs.add(id);
                                        allDataSources.add(mSources[j]);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            for (int j = 0; j<mSources.length;j++){
                                String jsonString;
                                try {
                                    jsonString = Cache.readObject(mContext,mSources[j]+"show").toString();
                                    if(jsonString.contains("\"id\":"+id)){
                                        getMediaData(i,id,mSources[j]);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        for(int k = 0;k<allDataIDs.size();k++){
                            getMediaData(allDataIDs.size(),allDataIDs.get(k),allDataSources.get(k));
                            Log.d(TAG,"ALLELEMENTS: "+allDataIDs.get(k)+" "+allDataSources.get(k));
                        }
                        */
                    }

                } catch (JSONException e) {

                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG,"getTVShowsForSources fail");
            }
        });

    }

    public void getMediaData(final int counter, int mediaID){
        Call<String> call;
        if(isMovie){
            call = mEngine.getMovie(mediaID);
        } else {
            call = mEngine.getTVShow(mediaID);
        }

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "getMovie Success: " + response.toString());
                Log.d(TAG, "getMovie Success: " + response.body());
                if(response.body()==null){
                    return;
                }
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(response.body());
                    //JSONArray resultsJson = jsonObj.getJSONArray("results");
                    Log.d(TAG, "resultJSON" +jsonObj.toString(4));


                    String title,rating,overview,imgUrl;
                    int id = jsonObj.getInt("id");
                    title =jsonObj.getString("title");
                    rating =jsonObj.getString("rating");
                    overview =jsonObj.getString("overview");
                    if(isMovie){
                        imgUrl = jsonObj.getString("poster_240x342");
                    }else {
                        imgUrl = jsonObj.getString("artwork_448x252");
                    }





                    Media media = new Media(title,id,"",rating,overview,imgUrl);
                    mMedia.add(media);
                    Log.d(TAG,"getTitle: "+ media.getTitle());
                    if(counter==allDataLength-1){
                        Fragment resultFrag = SearchResultFragment.newInstance(isMovie,mMedia);
                        fragmentManager.beginTransaction().replace(R.id.contentFrame, resultFrag).commit();
                    }

                } catch(JSONException e) {
                    Log.e(TAG,"error"+ e);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "getTVShowsForSources fail");
            }
        });

    }

    public JSONArray MergeJSONArray(JSONArray o1, JSONArray o2){
        JSONArray mergedObj = o1;
        for (int i = 0; i < o2.length(); i++) {
            try {
                mergedObj.put(o2.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mergedObj;
    }

    public void filterResults(){
        Log.d(TAG, "filterResult: datalength:" +allData.length());
        JSONArray selectedData = new JSONArray();
        for(int i = 0; i< allData.length();i++){

            String dataGenre;
            try {
                dataGenre  = allData.get(i).toString();
                Log.d(TAG,"filterResult: get(i)"+dataGenre);
                if(dataGenre.equals(mGenre)){
                    selectedData.put(allData.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Log.d(TAG, "filterResult: selectedDatalength:" +selectedData.length());
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
