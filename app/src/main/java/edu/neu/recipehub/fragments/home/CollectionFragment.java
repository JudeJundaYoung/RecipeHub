package edu.neu.recipehub.fragments.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
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

import edu.neu.recipehub.R;
import edu.neu.recipehub.fragments.GoBackFragment;
import edu.neu.recipehub.fragments.adapters.CollectionAdapter;
import edu.neu.recipehub.objects.Recipe;
import edu.neu.recipehub.objects.RecipeEntry;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CollectionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CollectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionFragment extends Fragment {

    public static final String KEYS = "keys";

    private List<Map.Entry<String,Recipe>> mRecipeEntryList;

    private ArrayList<String> mKeys;

    private OnFragmentInteractionListener mListener;

    private RecyclerView mCollectionItemRecyclerView;

    private DatabaseReference mRecipeDatabaseReference;

    private CollectionAdapter mCollectionAdapter;

    public CollectionFragment() {
        // Required empty public constructor
    }

    public static CollectionFragment newInstance(ArrayList<String> keys) {
        CollectionFragment fragment = new CollectionFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(KEYS,keys);
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
        return inflater.inflate(R.layout.fragment_collection, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mKeys = getArguments().getStringArrayList(KEYS);
        mCollectionItemRecyclerView = getView().findViewById(R.id.collectionsRecyclerView);
        getRecipes();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.upperFrameLayout, GoBackFragment.newInstance("Recommendation!")).commit();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void getRecipes(){
        mRecipeDatabaseReference = FirebaseDatabase.getInstance().getReference("RecipeHub")
                .child("recipe");
        mRecipeDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRecipeEntryList = new ArrayList<>();
                for(String key: mKeys){
                    Recipe recipe = dataSnapshot.child(key).getValue(Recipe.class);
                    mRecipeEntryList.add(new RecipeEntry<>(key,recipe));
                }
                initializeAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initializeAdapter(){
        mCollectionAdapter = new CollectionAdapter(mRecipeEntryList,
                (HomeFragment.OnFragmentInteractionListener)getActivity());
        mCollectionItemRecyclerView.setAdapter(mCollectionAdapter);
        mCollectionItemRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
