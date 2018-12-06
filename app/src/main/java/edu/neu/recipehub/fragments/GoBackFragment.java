package edu.neu.recipehub.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.neu.recipehub.MainActivity;
import edu.neu.recipehub.R;

public class GoBackFragment extends Fragment {


    public static final String TITLE = "title";

    private String mTitle;

    private TextView mTitleTextView;

    private ImageView mGoBackButton;

    public GoBackFragment() {
        // Required empty public constructor
    }

    public static GoBackFragment newInstance(String title) {
        GoBackFragment fragment = new GoBackFragment();
        Bundle args = new Bundle();
        args.putString(TITLE,title);
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
        return inflater.inflate(R.layout.fragment_go_back, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mTitle = getArguments().getString(TITLE);
        mTitleTextView = getView().findViewById(R.id.titleTextView);
        mTitleTextView.setText(mTitle);
        mGoBackButton = getView().findViewById(R.id.goBackImageView);
        mGoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onGoBackButtonClick();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
