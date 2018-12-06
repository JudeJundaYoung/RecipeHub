package edu.neu.recipehub.fragments.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import edu.neu.recipehub.R;
import edu.neu.recipehub.objects.ChatMessage;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {
    private List<ChatMessage> mMessages;
    private String mCurrentUser;
    private String mOtherUser;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout mLinearLayout;
        public TextView mMessageTextView;
        public CardView mCardView;

        public ViewHolder(View view){
            super(view);
            mLinearLayout = view.findViewById(R.id.chatMessageItemLinearLayout);
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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (message.getmUserName().equals(mCurrentUser)){
            params.gravity = Gravity.LEFT;
        } else {
            params.rightMargin = 0;
        }
        viewHolder.mLinearLayout.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }
}
