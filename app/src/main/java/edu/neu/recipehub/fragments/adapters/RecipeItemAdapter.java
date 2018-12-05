package edu.neu.recipehub.fragments.adapters;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import edu.neu.recipehub.R;
import edu.neu.recipehub.fragments.home.HomeFragment;
import edu.neu.recipehub.fragments.home.RecipeFragment;
import edu.neu.recipehub.objects.Recipe;

public class RecipeItemAdapter extends RecyclerView.Adapter<RecipeItemAdapter.ViewHolder> {
    private List<Map.Entry<String, Recipe>> mRecipeList;
    private HomeFragment.OnFragmentInteractionListener mListener;

    public RecipeItemAdapter(List<Map.Entry<String, Recipe>> mRecipeList,
                             HomeFragment.OnFragmentInteractionListener mListener) {
        super();
        this.mRecipeList = mRecipeList;
        this.mListener = mListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View parentView;
        ImageView mRecipeIcon;
        TextView mRecipeName;
        TextView mCreator;


        ViewHolder(View v) {
            super(v);
            parentView = v;
            mRecipeIcon = v.findViewById(R.id.recipeIcon);
            mRecipeName = v.findViewById(R.id.recipeName);
            mCreator = v.findViewById(R.id.creator);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recipe_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final String recipeKey = mRecipeList.get(i).getKey();
        final Recipe recipe = mRecipeList.get(i).getValue();
        View parent = (View) viewHolder.mRecipeIcon.getParent();
        viewHolder.mRecipeName.setText(recipe.mRecipeName);
        viewHolder.mCreator.setText(recipe.userName);
        Picasso.get().load(recipe.uris.get(0)).fit().centerCrop().into(viewHolder.mRecipeIcon);
        viewHolder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.changeFragmentInHomeFragment(RecipeFragment.newInstance(recipe, recipeKey));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }
}
