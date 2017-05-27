package com.rossconnacher.whattowatch;

import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rossconnacher.whattowatch.adapters.MediaAdapter;
import com.rossconnacher.whattowatch.models.Media;
import com.rossconnacher.whattowatch.models.Movie;
import com.rossconnacher.whattowatch.models.TVShow;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchResultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "SearchResultFragment" ;

    // TODO: Rename and change types of parameters
    private ArrayList<Movie> mMovies;
    private ArrayList<TVShow> mShows;
    private ArrayList<Media> mMedia;
    private boolean isMovie;

    @InjectView(R.id.searchAgainButton)
    public Button searchAgainButton;
    @InjectView(R.id.displayResultsView)
    public RecyclerView mView;

    private OnFragmentInteractionListener mListener;
    private GridLayoutManager mLayoutManager;
    private MediaAdapter mAdapter;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment SearchResultFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static SearchResultFragment newInstance(boolean isMovie,ArrayList<Media> medias) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, medias);
        args.putBoolean(ARG_PARAM2, isMovie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mMovies = new ArrayList<>();
       // mShows = new ArrayList<>();
        mMedia = new ArrayList<>();
        if (getArguments() != null) {
            isMovie = getArguments().getBoolean(ARG_PARAM2);
            mMedia = (ArrayList<Media>) getArguments().getSerializable(ARG_PARAM1);
           Log.d(TAG,"media: "+mMedia.get(0).getTitle());

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        ButterKnife.inject(this,view);
        searchAgainButton.setOnClickListener(this);

        mLayoutManager = new GridLayoutManager(getActivity(), 1);
        mView.setLayoutManager(mLayoutManager);
        mView.addItemDecoration(new SimpleDividerItemDecoration(this.getActivity()));
        mAdapter = new MediaAdapter(getActivity(), mMedia);
        mView.setAdapter(mAdapter);

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==searchAgainButton.getId()){
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
