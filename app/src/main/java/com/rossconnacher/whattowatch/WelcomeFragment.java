package com.rossconnacher.whattowatch;

import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WelcomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WelcomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WelcomeFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "WelcomeFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private WhatToWatchEngine mEngine;
    private String[] allSources = {"netflix","hbo","hulu_plus","crunchyroll_premium","crackle","amazon_prime","film_struck"};
    FragmentManager fragmentManager;

    private int numNetflixTVResults;
    private int numHuluTVResults;
    private int numHboTVResults;
    private int numAmazonTVResults;
    private int numCrunchyrollTVResults;
    private int numFilmstruckTVResults;
    private int numCrackleTVResults;

    private int numNetflixMovieResults;
    private int numHuluMovieResults;
    private int numHboMovieResults;
    private int numAmazonMovieResults;
    private int numCrunchyrollMovieResults;
    private int numFilmstruckMovieResults;
    private int numCrackleMovieResults;

    private JSONArray netflixTVData;
    private JSONArray huluTVData;
    private JSONArray crackleTVData;
    private JSONArray crunchyrollTVData;
    private JSONArray hboTVData;
    private JSONArray amazonTVData;
    private JSONArray filmstruckTVData;

    private JSONArray netflixMovieData;
    private JSONArray huluMovieData;
    private JSONArray crackleMovieData;
    private JSONArray crunchyrollMovieData;
    private JSONArray hboMovieData;
    private JSONArray amazonMovieData;
    private JSONArray filmstruckMovieData;

    @InjectView(R.id.continueText)
    public TextView continueText;
    @InjectView(R.id.welcomeProgress)
    public ProgressBar welcomeProgress;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WelcomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WelcomeFragment newInstance(String param1, String param2) {
        WelcomeFragment fragment = new WelcomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mEngine = new WhatToWatchEngine();
        netflixTVData = new JSONArray();
        hboTVData = new JSONArray();
        huluTVData = new JSONArray();
        amazonTVData = new JSONArray();
        crunchyrollTVData = new JSONArray();
        crackleTVData = new JSONArray();
        filmstruckTVData = new JSONArray();

        netflixMovieData = new JSONArray();
        hboMovieData = new JSONArray();
        huluMovieData = new JSONArray();
        amazonMovieData = new JSONArray();
        crunchyrollMovieData = new JSONArray();
        crackleMovieData = new JSONArray();
        filmstruckMovieData = new JSONArray();
        fragmentManager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        ButterKnife.inject(this, view);

        /*
        for(int i = 0;i<allSources.length;i++){
            getNumberOfShowsForSource(allSources[i]);
            getNumberOfMoviesForSource(allSources[i]);
        }
        */
        if(isDataAlreadyCached()){
            continueText.setOnClickListener(this);
            Log.d(TAG,"Already cached");
        } else {
            welcomeProgress.setVisibility(View.VISIBLE);
            Log.d(TAG,"Not cached");
            for(int i = 0;i<allSources.length;i++){
                getNumberOfShowsForSource(allSources[i]);
                getNumberOfMoviesForSource(allSources[i]);
            }
        }

        return view;

    }

    public boolean isDataAlreadyCached (){
        boolean isCached = false;

        for(int i = 0; i< allSources.length;i++){
            String sourceShowDataFilePath = allSources[i]+"show";
            String sourceMovieDataFilePath = allSources[i]+"movie";
            try {
                Cache.readObject(getActivity(),sourceShowDataFilePath);
                Cache.readObject(getActivity(),sourceMovieDataFilePath);
                String cacheShowData = Cache.readObject(getActivity(),sourceShowDataFilePath).toString();
                String cacheMovieData = Cache.readObject(getActivity(),sourceMovieDataFilePath).toString();
                if(cacheMovieData.equals("[]")||cacheShowData.equals("[]")){
                    return false;
                }
                Log.d(TAG, "cacheData: "+ cacheShowData);
                return true;
            } catch (IOException e) {
                //e.printStackTrace();
                return false;
            } catch (ClassNotFoundException e) {
                return false;
            }
        }


        return isCached;
    }

    public void CacheData(){
        for(int i = 0; i< allSources.length;i++) {
            String sourceShowDataFilePath = allSources[i] + "show";
            String sourceMovieDataFilePath = allSources[i] + "movie";

            if (allSources[i].equals("netflix")) {
                try {
                    Cache.writeObject(getActivity(), sourceShowDataFilePath, netflixTVData.toString());
                    Cache.writeObject(getActivity(), sourceMovieDataFilePath, netflixMovieData.toString());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else if (allSources[i].equals("hbo")) {
                try {
                    Cache.writeObject(getActivity(), sourceShowDataFilePath, hboTVData.toString());
                    Cache.writeObject(getActivity(), sourceMovieDataFilePath, hboMovieData.toString());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else if (allSources[i].equals("amazon_prime")) {
                try {
                    Cache.writeObject(getActivity(), sourceShowDataFilePath, amazonTVData.toString());
                    Cache.writeObject(getActivity(), sourceMovieDataFilePath, amazonMovieData.toString());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else if (allSources[i].equals("crunchyroll_premium")) {
                try {
                    Cache.writeObject(getActivity(), sourceShowDataFilePath, crunchyrollTVData.toString());
                    Cache.writeObject(getActivity(), sourceMovieDataFilePath, crunchyrollMovieData.toString());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else if (allSources[i].equals("crackle")) {
                try {
                    Cache.writeObject(getActivity(), sourceShowDataFilePath, crackleTVData.toString());
                    Cache.writeObject(getActivity(), sourceMovieDataFilePath, crackleMovieData.toString());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else if (allSources[i].equals("hulu_plus")) {
                try {
                    Cache.writeObject(getActivity(), sourceShowDataFilePath, huluTVData.toString());
                    Cache.writeObject(getActivity(), sourceMovieDataFilePath, huluMovieData.toString());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else if (allSources[i].equals("film_struck")) {
                try {
                    Cache.writeObject(getActivity(), sourceShowDataFilePath, filmstruckTVData.toString());
                    Cache.writeObject(getActivity(), sourceMovieDataFilePath, filmstruckMovieData.toString());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        continueText.setOnClickListener(WelcomeFragment.this);
        welcomeProgress.setVisibility(View.INVISIBLE);
    }
    public void getNumberOfShowsForSource(final String source){
        Call<String> showNumberGetter = mEngine.getTVShowsForSources(1,0,new String[]{source});
        showNumberGetter.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(response.body());


                    if(source.equals("netflix")){
                        numNetflixTVResults = jsonObj.getInt("total_results");
                    } else if(source.equals("hbo")) {
                        numHboTVResults = jsonObj.getInt("total_results");
                    } else if (source.equals("amazon_prime")) {
                        numAmazonTVResults = jsonObj.getInt("total_results");
                    } else if(source.equals("crunchyroll_premium")){
                        numCrunchyrollTVResults = jsonObj.getInt("total_results");
                    } else if(source.equals("crackle")){
                        numCrackleTVResults = jsonObj.getInt("total_results");
                    } else if(source.equals("hulu_plus")) {
                        numHuluTVResults = jsonObj.getInt("total_results");
                    } else if(source.equals("film_struck")){
                        numFilmstruckTVResults = jsonObj.getInt("total_results");
                    }

                    //last one do something
                    if(allSources[allSources.length-1].equals(source)){
                        getAllShows();
                    }
                
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void getNumberOfMoviesForSource(final String source){
        Call<String> movieNumberGetter = mEngine.getMoviesForSources(1,0,new String[]{source});
        movieNumberGetter.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(response.body());


                    if(source.equals("netflix")){
                        numNetflixMovieResults = jsonObj.getInt("total_results");
                    } else if(source.equals("hbo")) {
                        numHboMovieResults = jsonObj.getInt("total_results");
                    } else if (source.equals("amazon_prime")) {
                        numAmazonMovieResults = jsonObj.getInt("total_results");
                    } else if(source.equals("crunchyroll_premium")){
                        numCrunchyrollMovieResults = jsonObj.getInt("total_results");
                    } else if(source.equals("crackle")){
                        numCrackleMovieResults = jsonObj.getInt("total_results");
                    } else if(source.equals("hulu_plus")) {
                        numHuluMovieResults = jsonObj.getInt("total_results");
                    } else if(source.equals("film_struck")){
                        numFilmstruckMovieResults = jsonObj.getInt("total_results");
                    }

                    //last one do something
                    if(allSources[allSources.length-1].equals(source)){
                        getAllMovies();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }
     public void getAllShows(){
         for(int i = 0;i<allSources.length;i++){
             getAllShowsForSource(allSources[i]);
         }
     }
     public void getAllMovies(){
         for(int i = 0;i<allSources.length;i++){
             getAllMoviesForSource(allSources[i]);
         }
     }


    public void getAllShowsForSource(final String source){

        int numTVResults = 0;

        if(source.equals("netflix")){
            numTVResults = numNetflixTVResults;
        } else if(source.equals("hbo")) {
            numTVResults = numHboTVResults;
        } else if (source.equals("amazon_prime")) {
            numTVResults = numAmazonTVResults;
        } else if(source.equals("crunchyroll_premium")){
            numTVResults = numCrunchyrollTVResults;
        } else if(source.equals("crackle")){
            numTVResults = numCrackleTVResults;
        } else if(source.equals("hulu_plus")) {
            numTVResults = numHuluTVResults;
        } else if(source.equals("film_struck")){
            numTVResults = numFilmstruckTVResults;
        }

        for(int i = 0; i<numTVResults; i+=250){
            final int numItemsFetched;
            if(numTVResults-i<250) {
                //final
                numItemsFetched = numTVResults-i;
            } else {
                numItemsFetched = 250;
            }
            Call<String> call = mEngine.getMoviesForSources(numItemsFetched,i,new String[]{source});
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
                        if(source.equals("netflix")){
                            netflixTVData = MergeJSONArray(netflixTVData,resultsJson);
                        } else if(source.equals("hbo")) {
                            hboTVData = MergeJSONArray(hboTVData,resultsJson);
                        } else if (source.equals("amazon_prime")) {
                            amazonTVData = MergeJSONArray(amazonTVData,resultsJson);
                        } else if(source.equals("crunchyroll_premium")){
                            crunchyrollTVData = MergeJSONArray(crunchyrollTVData,resultsJson);
                        } else if(source.equals("crackle")){
                            crackleTVData = MergeJSONArray(crackleTVData,resultsJson);
                        } else if(source.equals("hulu_plus")) {
                            huluTVData = MergeJSONArray(huluTVData,resultsJson);
                        } else if(source.equals("film_struck")){
                            filmstruckTVData = MergeJSONArray(filmstruckTVData,resultsJson);
                        }


                    } catch(JSONException e) {

                    }
                    /*
                    if(numItemsFetched==numTVResults- finalI){
                      //do something on last iteration?
                    }
                    */
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG,"Get channel data fail");
                }
            });
        }
    }
    public void getAllMoviesForSource(final String source){

        int numMovieResults = 0;

        if(source.equals("netflix")){
            numMovieResults = numNetflixMovieResults;
        } else if(source.equals("hbo")) {
            numMovieResults = numHboMovieResults;
        } else if (source.equals("amazon_prime")) {
            numMovieResults = numAmazonMovieResults;
        } else if(source.equals("crunchyroll_premium")){
            numMovieResults = numCrunchyrollMovieResults;
        } else if(source.equals("crackle")){
            numMovieResults = numCrackleMovieResults;
        } else if(source.equals("hulu_plus")) {
            numMovieResults = numHuluMovieResults;
        } else if(source.equals("film_struck")){
            numMovieResults = numFilmstruckMovieResults;
        }


        for(int i = 0; i<numMovieResults;i+=250){
            final int numItemsFetched;
            if(numMovieResults-i<250) {
                //final
                numItemsFetched = numMovieResults-i;
            } else {
                numItemsFetched = 250;
            }
            Call<String> call = mEngine.getMoviesForSources(numItemsFetched,i,new String[]{source});
            final int finalI = i;
            final int finalNumMovieResults = numMovieResults;
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
                        if(source.equals("netflix")){
                            netflixMovieData = MergeJSONArray(netflixMovieData,resultsJson);
                        } else if(source.equals("hbo")) {
                            hboMovieData = MergeJSONArray(hboMovieData,resultsJson);
                        } else if (source.equals("amazon_prime")) {
                            amazonMovieData = MergeJSONArray(amazonMovieData,resultsJson);
                        } else if(source.equals("crunchyroll_premium")){
                            crunchyrollMovieData = MergeJSONArray(crunchyrollMovieData,resultsJson);
                        } else if(source.equals("crackle")){
                            crackleMovieData = MergeJSONArray(crackleMovieData,resultsJson);
                        } else if(source.equals("hulu_plus")) {
                            huluMovieData = MergeJSONArray(huluMovieData,resultsJson);
                        } else if(source.equals("film_struck")){
                            filmstruckMovieData = MergeJSONArray(filmstruckMovieData,resultsJson);
                        }
                    } catch(JSONException e) {

                    }

                    if(numItemsFetched== finalNumMovieResults - finalI){
                        //do something on last iteration?
                        Log.d(TAG,"#hulutv: " + huluTVData.length());
                        CacheData();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG,"Get channel data fail");
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== continueText.getId()){
            Fragment movieTVFrag = MoviesTVFragment.newInstance("","");
            fragmentManager.beginTransaction().replace(R.id.contentFrame, movieTVFrag).commit();
        }
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
