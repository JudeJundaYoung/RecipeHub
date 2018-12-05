package edu.neu.recipehub.fragments.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.neu.recipehub.R;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    private List<Map.Entry<String, String>> mIngredients;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mIngredientTextView;
        public TextView mAmountTextView;

        public ViewHolder(View view) {
            super(view);
            mIngredientTextView = view.findViewById(R.id.ingredientTextView);
            mAmountTextView = view.findViewById(R.id.ingredientAmountTextView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public IngredientsAdapter(Map<String, String> ingredients) {
        mIngredients = new ArrayList<>();
        mIngredients.addAll(ingredients.entrySet());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredients_recyclerview_row, parent, false);
        //
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Map.Entry<String, String> entry = mIngredients.get(position);
        holder.mIngredientTextView.setText(entry.getKey());
        holder.mAmountTextView.setText(entry.getValue());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mIngredients.size();
    }
}

