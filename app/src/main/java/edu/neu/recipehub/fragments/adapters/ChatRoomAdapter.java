package edu.neu.recipehub.fragments.adapters;

import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import edu.neu.recipehub.R;
import edu.neu.recipehub.objects.ChatMessage;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {
    private List<ChatMessage> mMessages;
    private String mCurrentUser;
    private String mOtherUser;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ConstraintLayout mConstrainLayout;
        public TextView mMessageTextView;
        public CardView mCardView;

        public ViewHolder(View view){
            super(view);
            mConstrainLayout = view.findViewById(R.id.chatMessageItemConstrainLayout);
            mMessageTextView = view.findViewById(R.id.chatRoomMessageTextView);
            mCardView = view.findViewById(R.id.messageItemCardView);
        }
    }

    public ChatRoomAdapter(List<ChatMessage> messagesList, String currentUser, String otherUser){
        mMessages = messagesList;
        mCurrentUser = currentUser;
        mOtherUser = otherUser;
    }

    @Override
    public ChatRoomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_item,
                parent,false);
        ChatRoomAdapter.ViewHolder viewHolder = new ChatRoomAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomAdapter.ViewHolder viewHolder, int position) {
        ChatMessage message = mMessages.get(position);
        viewHolder.mMessageTextView.setText(message.getmMessage());


        // TODO:: HERES BUG
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) viewHolder.mCardView.getLayoutParams();
        if (message.getmUserName().equals(mCurrentUser)){
            params.setMargins(100,8,0,8);
        } else {
            params.setMargins(10,8,0,8);
        }
        viewHolder.mCardView.setLayoutParams(params);
        viewHolder.mCardView.requestLayout();
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }
}
