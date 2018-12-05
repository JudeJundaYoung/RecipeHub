package edu.neu.recipehub.fragments.forks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.neu.recipehub.MainActivity;
import edu.neu.recipehub.R;
import edu.neu.recipehub.fragments.adapters.RecipeItemAdapter;
import edu.neu.recipehub.fragments.home.HomeFragment;
import edu.neu.recipehub.objects.Recipe;
import edu.neu.recipehub.objects.RecipeEntry;

public class ForksFragment extends Fragment {
    private RecyclerView myRecipeView;
    private ImageView createBtn;
    private View rootView;
    private HomeFragment.OnFragmentInteractionListener mListener;
    private DatabaseReference mDatabaseRef;
    private TextView mHaventPublishedTextView;

    public ForksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ForksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForksFragment newInstance() {
        ForksFragment fragment = new ForksFragment();
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
        rootView = inflater.inflate(R.layout.fragment_fork, container, false);
        myRecipeView = rootView.findViewById(R.id.myRecipeView);
        createBtn = rootView.findViewById(R.id.createBtn);
        mHaventPublishedTextView = rootView.findViewById(R.id.noRecipeTextView);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.changeFragmentInHomeFragment(DescriptionFragment.newInstance());
            }
        });
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("RecipeHub")
                .child("recipe");
        final String curUser = MainActivity.USER_NAME;
        final List<Map.Entry<String, Recipe>> myRecipe = new ArrayList<>();
        mDatabaseRef.orderByChild("userName").equalTo(curUser)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String recipeKey = snapshot.getKey();
                            Recipe recipe = snapshot.getValue(Recipe.class);
                            myRecipe.add(new RecipeEntry<String, Recipe>(recipeKey, recipe));
                        }
                        if (myRecipe.size() != 0) {
                            mHaventPublishedTextView.setText("");
                        }
                        RecipeItemAdapter recipeItemAdapter = new RecipeItemAdapter(myRecipe, mListener);
                        myRecipeView.setAdapter(recipeItemAdapter);
                        myRecipeView.setLayoutManager(new LinearLayoutManager(getActivity(),
                                LinearLayoutCompat.VERTICAL, false));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragment.OnFragmentInteractionListener) {
            mListener = (HomeFragment.OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
