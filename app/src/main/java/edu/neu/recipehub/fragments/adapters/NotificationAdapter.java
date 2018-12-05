package edu.neu.recipehub.fragments.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import edu.neu.recipehub.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<String> mNotificationList;

    public class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView mNotificationTextView;

        public ViewHolder(View view) {
            super(view);
            mNotificationTextView = view.findViewById(R.id.notificationTextView);
        }
    }

    public NotificationAdapter(List<String> notificationList){
        mNotificationList = notificationList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,
                parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder holder, int position) {
        String  message = mNotificationList.get(position);
        holder.mNotificationTextView.setText(message);

    }


    @Override
    public int getItemCount() {
        return mNotificationList.size();
    }
}
