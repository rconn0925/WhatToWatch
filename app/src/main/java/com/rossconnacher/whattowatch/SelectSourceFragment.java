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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectSourceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectSourceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectSourceFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "isMovie";
    private static final String TAG = "SelectSourceFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @InjectView(R.id.NetflixCheck)
    public CheckBox netflixCheck ;
    @InjectView(R.id.HuluCheck)
    public CheckBox huluCheck ;
    @InjectView(R.id.AmazonCheck)
    public CheckBox amazonCheck;
    @InjectView(R.id.HBOgoCheck)
    public CheckBox HBOgoCheck;
    @InjectView(R.id.CrackleCheck)
    public CheckBox crackleCheck ;
    @InjectView(R.id.CrunchyRollCheck)
    public CheckBox crunchyRollCheck;
    @InjectView(R.id.TubiTVCheck)
    public CheckBox tubiTVCheck;

    @InjectView(R.id.selectSourceNextButton)
    public Button nextButton;
    @InjectView(R.id.selectSourceBack)
    public ImageView backButton;
    private OnFragmentInteractionListener mListener;
    private String[] mSources;

    boolean isMovie = false;

    public SelectSourceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     *
     * @return A new instance of fragment SelectSourceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectSourceFragment newInstance(boolean isMovie) {
        SelectSourceFragment fragment = new SelectSourceFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, isMovie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isMovie = getArguments().getBoolean(ARG_PARAM1);
            Log.d(TAG,"getting WhatToWatch: "+ isMovie);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_source, container, false);
        ButterKnife.inject(this, view);
        nextButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        if(isMovie){
            huluCheck.setVisibility(View.GONE);
            crunchyRollCheck.setVisibility(View.GONE);
        } else {
            Log.d(TAG,"whatToWatch is TVSHOW");
        }

        return view;
    }
    public String[] getSources(){
        ArrayList<String> sourceList = new ArrayList<>();
        if(netflixCheck.isChecked()){
            sourceList.add("netflix");
        }
        if(huluCheck.isChecked()){
            sourceList.add("hulu");
        }
        if(amazonCheck.isChecked()){
            sourceList.add("amazon_prime");
        }
        if(HBOgoCheck.isChecked()){
            sourceList.add("hbo");
        }
        if(crackleCheck.isChecked()){
            sourceList.add("crackle");
        }
        if(crunchyRollCheck.isChecked()){
            sourceList.add("crunchyroll");
        }
        if(tubiTVCheck.isChecked()){
            sourceList.add("tubi");
        }
        String[] mSources = new String[sourceList.size()];
        mSources = sourceList.toArray(mSources);
        for(int i = 0; i<mSources.length;i++){
            Log.d(TAG,"Source" +i+": "+mSources[i]);
        }
        return mSources;
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
        if(v.getId()==nextButton.getId()){
            if(!netflixCheck.isChecked()
                    &&!crackleCheck.isChecked()
                    &&!amazonCheck.isChecked()
                    &&!huluCheck.isChecked()
                    &&!HBOgoCheck.isChecked()
                    &&!tubiTVCheck.isChecked()
                    &&!crunchyRollCheck.isChecked()){
                Toast.makeText(getActivity(), "Please select at least one", Toast.LENGTH_SHORT).show();
            } else {
                Fragment inputFrag = FilterFragment.newInstance(isMovie,getSources());
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentFrame, inputFrag).commit();
            }
        } else if(v.getId()==backButton.getId()) {
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
