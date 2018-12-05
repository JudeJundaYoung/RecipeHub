package edu.neu.recipehub.fragments.forks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.neu.recipehub.R;
import edu.neu.recipehub.fragments.home.HomeFragment;


public class UploadFailureFragment extends Fragment {
    private View rootView;
    private Button failureBtn;
    private HomeFragment.OnFragmentInteractionListener mListener;

    public UploadFailureFragment() {
        // Required empty public constructor
    }


    public static UploadFailureFragment newInstance() {
        UploadFailureFragment fragment = new UploadFailureFragment();
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
        rootView = inflater.inflate(R.layout.fragment_upload_success, container, false);
        failureBtn = rootView.findViewById(R.id.failureBtn);
        failureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.changeFragmentInHomeFragment(DescriptionFragment.newInstance());
            }
        });
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragment.OnFragmentInteractionListener) {
            mListener = (HomeFragment.OnFragmentInteractionListener) context;
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

}
