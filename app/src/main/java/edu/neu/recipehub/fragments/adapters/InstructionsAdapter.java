package edu.neu.recipehub.fragments.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.neu.recipehub.R;
import edu.neu.recipehub.objects.Review;

public class InstructionsAdapter extends RecyclerView.Adapter<InstructionsAdapter.ViewHolder> {
    private List<String> mInstructions;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mCounterTextView;
        public TextView mInstructionContentTextView;

        public ViewHolder(View view) {
            super(view);
            mCounterTextView = view.findViewById(R.id.counterTextView);
            mInstructionContentTextView = view.findViewById(R.id.instructionContentTextView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public InstructionsAdapter(List<String> instructions) {
        mInstructions = new ArrayList<>(instructions);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.instructions_recyclerview_row, parent, false);
        //
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mCounterTextView.setText(String.valueOf(position + 1));
        holder.mInstructionContentTextView.setText(mInstructions.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mInstructions.size();
    }
}

