package edu.neu.recipehub.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import edu.neu.recipehub.MainActivity;
import edu.neu.recipehub.R;
import edu.neu.recipehub.fragments.adapters.RecipeItemAdapter;
import edu.neu.recipehub.objects.Recipe;
import edu.neu.recipehub.utils.SearchResultFinder;


public class SearchFragment extends Fragment {
    private View rootView;
    private List<Recipe> mRecipeList;
    private RecyclerView searchResultView;
    private EditText searchWindow;
    private SearchResultFinder searchResultFinder;

    private ImageView mGoBackImageView;

    private MainActivity mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
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
        mRecipeList = new ArrayList<>();
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        searchWindow = rootView.findViewById(R.id.searchWindow);
        searchResultView = rootView.findViewById(R.id.searchResult);
        searchWindow.requestFocus();
        final RecipeItemAdapter recipeItemAdapter = new RecipeItemAdapter(mRecipeList, mListener);
        searchResultFinder = new SearchResultFinder(recipeItemAdapter,mRecipeList);
        searchResultView.setAdapter(recipeItemAdapter);
        searchResultView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutCompat.VERTICAL,false));
        searchWindow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = searchWindow.getText().toString();
                if(input.trim().isEmpty()) {
                    mRecipeList.clear();
                    recipeItemAdapter.notifyDataSetChanged();
                } else searchResultFinder.search(input);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoBackImageView = getView().findViewById(R.id.goBackImageView);

        mGoBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGoBackButtonClick();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchFragment.OnFragmentInteractionListener) {
            mListener = (MainActivity) context;
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onGoBackButtonClick();
    }

}
