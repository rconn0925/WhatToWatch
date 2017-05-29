package com.rossconnacher.whattowatch;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "Sources";
    private static final String ARG_PARAM2 = "WhatToWatch";
    private static final String TAG = "FilterFragment" ;

    // TODO: Rename and change types of parameters
    private String[] mSources;
    private String[] mGenres;
    private String[] TVratings = {"TV-MA","TV-14","TV-PG","TV-G","TV-Y7","TV-Y"};
    private String[] movieRatings = {"NC-17","R","PG-13","PG","G"};
    private boolean isMovie;
    private JSONObject resultData = null;
    private int totalResults;

    @InjectView(R.id.filterFragLayout)
    public RelativeLayout mView;

    /*
    @InjectView(R.id.genreSpinner)
    public Spinner genreSpinner;
    @InjectView(R.id.ratingSpinner)
    public Spinner ratingSpinner;
    */
    @InjectView(R.id.searchButton)
    public Button searchButton;
    @InjectView(R.id.filterBack)
    public ImageView backButton;
    @InjectView(R.id.relatedLabel)
    public TextView relatedLabel;
    @InjectView(R.id.relatedEditText)
    public EditText relatedEditText;
    /*
    @InjectView(R.id.actorEditText)
    public EditText actorEditText ;
    */
    //JSONArray genreData = genreJson.getJSONArray("results");

    private OnFragmentInteractionListener mListener;

    public FilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment FilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterFragment newInstance(boolean isMovie,String[] sources) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1, sources);
        args.putBoolean(ARG_PARAM2,isMovie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSources = getArguments().getStringArray(ARG_PARAM1);
            isMovie = getArguments().getBoolean(ARG_PARAM2);

        }
        SharedPreferences mPrefs = getActivity().getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        String genreJsonStr = mPrefs.getString("GenreData","0");
        if(genreJsonStr!=null){
            try {
                JSONObject genreJson = new JSONObject(genreJsonStr);
                JSONArray genreData = genreJson.getJSONArray("results");

                String[] genreArray = new String[genreData.length()];
                for(int i = 0; i <genreData.length();i++){
                    genreArray[i] = ((JSONObject)genreData.get(i)).getString("genre");
                    //Log.d(TAG,"Genres : " + genreArray[i]);
                }
                mGenres = genreArray;
                Log.d(TAG,"Genres : " + genreData.length());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        ButterKnife.inject(this,view);
        ArrayAdapter<String> genreAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mGenres);
      //  genreSpinner.setAdapter(genreAdapter);
        searchButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
       // actorEditText.setHint("Enter actor or actress");
        if(isMovie){
            ArrayAdapter<String> movieAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, movieRatings);
      //      ratingSpinner.setAdapter(movieAdapter);
            relatedLabel.setText("Enter a movie to calibrate the search");
            relatedEditText.setHint("E.g. Good Will Hunting");
        } else{
            ArrayAdapter<String> TVAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, TVratings);
       //     ratingSpinner.setAdapter(TVAdapter);
            relatedLabel.setHint("Enter TV show to calibrate the search");
            relatedEditText.setHint("E.g. Rick and Morty");
        }

        return view;
    }

    public void getMoviesForSources(){
        WhatToWatchEngine mEngine = new WhatToWatchEngine();
        Call<String> call = mEngine.getMoviesForSources(1,0,mSources);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "getMoviesForSources Success: " + response.toString());
                Log.d(TAG, "getMoviesForSources Success: " + response.body());
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(response.body());
                    totalResults = jsonObj.getInt("total_results");
                    Log.d(TAG,"numresults: "+totalResults);
                    boolean hasActor = false;
                 //   String actorName = actorEditText.getText().toString();
                    String relateTo = relatedEditText.getText().toString();
                 //   String genre = genreSpinner.getSelectedItem().toString();
                  //  String rating = ratingSpinner.getSelectedItem().toString();
                    Fragment loadingFrag = LoadingFragment.newInstance(isMovie,totalResults,"",hasActor,"",relateTo,"",mSources);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentFrame, loadingFrag).commit();
                } catch(JSONException e) {

                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG,"Get channel data fail");
            }
        });
    }
    public void getTVShowsForSources(){
        WhatToWatchEngine mEngine = new WhatToWatchEngine();
        Call<String> call = mEngine.getTVShowsForSources(1,0,mSources);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "getTVShowsForSources Success: " + response.toString());
                Log.d(TAG, "getTVShowsForSources Success: " + response.body());
                JSONObject jsonObj = null;
                try {
                    resultData = new JSONObject(response.body());
                    totalResults = resultData.getInt("total_results");
                    Log.d(TAG,"numresults: "+totalResults);
                    boolean hasActor = false;
                   // String actorName = actorEditText.getText().toString();
                    String relateTo = relatedEditText.getText().toString();
                   // String rating = ratingSpinner.getSelectedItem().toString();
                    //String genre = genreSpinner.getSelectedItem().toString();
                    Fragment loadingFrag = LoadingFragment.newInstance(isMovie,totalResults,"",hasActor,"",relateTo,"",mSources);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentFrame, loadingFrag).commit();
                } catch(JSONException e) {

                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG,"getTVShowsForSources fail");
            }
        });
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
        if(v.getId()==searchButton.getId()){
            /*
            if(isMovie){
                getMoviesForSources();
            }else{
                getTVShowsForSources();
            }
            */
            boolean hasActor = false;
            // String actorName = actorEditText.getText().toString();
            String relateTo = relatedEditText.getText().toString();
            // String rating = ratingSpinner.getSelectedItem().toString();
            //String genre = genreSpinner.getSelectedItem().toString();
            Fragment loadingFrag = LoadingFragment.newInstance(isMovie,totalResults,"",hasActor,"",relateTo,"",mSources);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentFrame, loadingFrag).commit();
        }
        else if(v.getId()==backButton.getId()) {
            Fragment moviesTVfrag = new MoviesTVFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentFrame, moviesTVfrag).commit();
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
