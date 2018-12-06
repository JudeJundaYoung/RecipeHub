package edu.neu.recipehub.fragments.forks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import edu.neu.recipehub.R;
import edu.neu.recipehub.fragments.GoBackFragment;
import edu.neu.recipehub.fragments.home.HomeFragment;


public class DescriptionFragment extends Fragment {

    public static String Description = "";
    public static String RECIPE_NAME = "";


    private HomeFragment.OnFragmentInteractionListener mListener;
    private View rootView;
    private Button nextBtn;
    private EditText descriptionBox;
    private EditText nameBox;

    public DescriptionFragment() {
        // Required empty public constructor
    }


    public static DescriptionFragment newInstance() {
        DescriptionFragment fragment = new DescriptionFragment();
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
        Description = "";
        RECIPE_NAME = "";
        rootView = inflater.inflate(R.layout.fragment_description, container, false);
        descriptionBox = rootView.findViewById(R.id.descriptionBox);
        nextBtn = rootView.findViewById(R.id.next2);
        nameBox = rootView.findViewById(R.id.nameBox);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.upperFrameLayout, GoBackFragment.newInstance("Description")).commit();



        setonTextChangeListener(descriptionBox);
        setonTextChangeListener(nameBox);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Description = descriptionBox.getText().toString();
                RECIPE_NAME = nameBox.getText().toString();
                mListener.changeFragmentInHomeFragment(AddPhotoFragment.newInstance());
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
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

    private void setonTextChangeListener(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!descriptionBox.getText().toString().isEmpty()
                        && !nameBox.getText().toString().isEmpty()) nextBtn.setEnabled(true);
                else nextBtn.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

}
