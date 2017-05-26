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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private JSONArray allData;
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
            mRating = getArguments().getString(ARG_PARAM7);
            mGenre = getArguments().getString(ARG_PARAM8);
            Log.d(TAG, "args: " + isMovie+ " "+numDataElements);
        }
        allData = new JSONArray();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loading, container, false);
        ButterKnife.inject(this,view);
        if(isMovie){
            getAllMoviesForSources();
        } else {
            getAllTVShowsForSources();
        }
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

    public void getAllMoviesForSources(){
        WhatToWatchEngine mEngine = new WhatToWatchEngine();
        for(int i = 0; i<numDataElements;i+=250){
            final int numItemsFetched;
            if(numDataElements-i<250) {
                //final
                numItemsFetched = numDataElements-i;
            } else {
                numItemsFetched = 250;
            }
            Call<String> call = mEngine.getMoviesForSources(numItemsFetched,i,mSources);
            final int finalI = i;
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d(TAG, "getMoviesForSources Success: " + response.toString());
                    Log.d(TAG, "getMoviesForSources Success: " + response.body());
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(response.body());
                        JSONArray resultsJson = jsonObj.getJSONArray("results");
                        Log.d(TAG, "resultJSON" +resultsJson);
                        allData = MergeJSONArray(allData,resultsJson);

                    } catch(JSONException e) {

                    }
                    if(numItemsFetched==numDataElements- finalI){
                        filterResults();
                        Fragment resultFrag = new SearchResultFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contentFrame, resultFrag).commit();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG,"Get channel data fail");
                }
            });
        }
    }
    public void getAllTVShowsForSources(){
        WhatToWatchEngine mEngine = new WhatToWatchEngine();
        for(int i = 0; i<numDataElements;i+=250) {
            final int numItemsFetched;
            if (numDataElements - i < 250) {
                //final
                numItemsFetched = numDataElements - i;
            } else {
                numItemsFetched = 250;
            }
            Call<String> call = mEngine.getTVShowsForSources(numItemsFetched,i,mSources);
            final int finalI = i;
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d(TAG, "getTVShowsForSources Success: " + response.toString());
                    Log.d(TAG, "getTVShowsForSources Success: " + response.body());
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(response.body());
                        JSONArray resultsJson = jsonObj.getJSONArray("results");
                        Log.d(TAG, "resultJSON" +resultsJson);
                        allData = MergeJSONArray(allData,resultsJson);

                    } catch(JSONException e) {

                    }

                    if(numItemsFetched==numDataElements- finalI){
                        filterResults();
                        Fragment resultFrag = new SearchResultFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contentFrame, resultFrag).commit();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG,"getTVShowsForSources fail");
                }
            });
        }
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

        }
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
