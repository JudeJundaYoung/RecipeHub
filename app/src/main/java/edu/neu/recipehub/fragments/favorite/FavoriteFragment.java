package edu.neu.recipehub.fragments.favorite;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import edu.neu.recipehub.objects.Recipe;
import edu.neu.recipehub.objects.RecipeEntry;
import edu.neu.recipehub.utils.SearchResultFinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoriteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {
    private List<Map.Entry<String, Recipe>> mRecipeList;
    private RecyclerView mFavoritesRecycleView;
    private SearchResultFinder searchResultFinder;

    private DatabaseReference mDatabaseRef;

    private MainActivity mListener;

    public FavoriteFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FavoriteFragment newInstance() {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mRecipeList = new ArrayList<>();
        mFavoritesRecycleView = getView().findViewById(R.id.favoriteRecyclerView);

        final Map<String,String> favorites = mListener.getmFavorites();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("RecipeHub")
                .child("recipe");
        final List<Map.Entry<String, Recipe>> myRecipe = new ArrayList<>();
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String recipeKey = snapshot.getKey();
                            Recipe recipe = snapshot.getValue(Recipe.class);
                            if(!favorites.containsKey(recipeKey)) continue;
                            myRecipe.add(new RecipeEntry<>(recipeKey, recipe));
                        }
                        RecipeItemAdapter recipeItemAdapter = new RecipeItemAdapter(myRecipe, mListener);
                        mFavoritesRecycleView.setAdapter(recipeItemAdapter);
                        mFavoritesRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(),
                                LinearLayoutCompat.VERTICAL, false));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
    }
}
