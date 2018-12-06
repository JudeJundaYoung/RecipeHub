package edu.neu.recipehub.fragments.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.neu.recipehub.R;
import edu.neu.recipehub.objects.Recipe;
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
    private EditText mSearchBox;

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
        initializeViews();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void changeFragmentInHomeFragment(Fragment fragment);
    }

    private void initializeViews() {
        mHottestThisWeekImageView = getView().findViewById(R.id.hottestThisWeekImageView);
        mNewlyAddedImageView = getView().findViewById(R.id.newlyAddedImageView);
        mCocktailImageView = getView().findViewById(R.id.cocktailImageView);
        mHighestRatedImageView = getView().findViewById(R.id.highestRatedImageView);
        mSearchBox = getView().findViewById(R.id.searchBox);

        final ArrayList<String> keys = new ArrayList<>();
        keys.add("-LT-KO0_TEpgkYkkOE57");
        keys.add("-LT-LkeyQG0EMG_2fRxo");
        keys.add("-LT0WFt7PRaaQ7DmudKX");

        mHottestThisWeekImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.changeFragmentInHomeFragment(CollectionFragment.newInstance(keys));
            }
        });
        mNewlyAddedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RecipeHub");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<>();
                        ArrayList<String> keys = dataSnapshot.child("recipe").getValue(t);
                        mListener.changeFragmentInHomeFragment(CollectionFragment.newInstance(keys));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        mCocktailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.changeFragmentInHomeFragment(CollectionFragment.newInstance(keys));
            }
        });
        mHighestRatedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.changeFragmentInHomeFragment(CollectionFragment.newInstance(keys));
            }
        });
        mSearchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mListener.changeFragmentInHomeFragment(SearchFragment.newInstance());
            }
        });

    }
}
