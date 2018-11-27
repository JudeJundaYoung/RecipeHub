package edu.neu.recipehub.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import edu.neu.recipehub.R;
import edu.neu.recipehub.utils.UIUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ImageView mHottestThisWeekImageView;
    private ImageView mNewlyAddedImageView;
    private ImageView mCocktailImageView;
    private ImageView mHighestRatedImageView;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeImageViews();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
    }

    private void initializeImageViews(){
        mHottestThisWeekImageView = getView().findViewById(R.id.hottestThisWeekImageView);
        mNewlyAddedImageView = getView().findViewById(R.id.newlyAddedImageView);
        mCocktailImageView = getView().findViewById(R.id.cocktailImageView);
        mHighestRatedImageView = getView().findViewById(R.id.highestRatedImageView);

        mHottestThisWeekImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.showToast(getActivity(),"Hottest This Week!");
            }
        });
        mNewlyAddedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.showToast(getActivity(),"Newly Added!");
            }
        });
        mCocktailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.showToast(getActivity(),"Cocktail!");
            }
        });
        mHighestRatedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.showToast(getActivity(),"Highest Rated!");
            }
        });
    }
}
