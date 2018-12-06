package edu.neu.recipehub.fragments.communication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.neu.recipehub.R;
import edu.neu.recipehub.fragments.adapters.ChatRoomAdapter;
import edu.neu.recipehub.objects.ChatMessage;
import edu.neu.recipehub.utils.UIUtils;

public class ChatRoomFragment extends Fragment {

    private static final String ROOM_KEY = "roomkey";
    private static final String CURRENT_USER = "currentuser";
    private static final String OTHER_USER = "otheruser";

    private String mRoomKey;

    private String mCurrentUser;

    private String mOtherUser;

    private OnFragmentInteractionListener mListener;

    private List<ChatMessage> mMessageList;

    private RecyclerView mMessageRecyclerView;

    private DatabaseReference mChatRoomDatabaseReference;

    private ChatRoomAdapter mChatRoomAdapter;

    private EditText mMessageEditText;

    private ImageView mSendMessageImageView;

    public ChatRoomFragment() {
        // Required empty public constructor
    }

    public static ChatRoomFragment newInstance(String roomKey, String currentUser, String otherUser) {
        ChatRoomFragment fragment = new ChatRoomFragment();
        Bundle args = new Bundle();
        args.putString(ROOM_KEY,roomKey);
        args.putString(CURRENT_USER,currentUser);
        args.putString(OTHER_USER,otherUser);
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
        return inflater.inflate(R.layout.fragment_chat_room, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mRoomKey = getArguments().getString(ROOM_KEY);
        mCurrentUser = getArguments().getString(CURRENT_USER);
        mOtherUser = getArguments().getString(OTHER_USER);
        mMessageEditText = getView().findViewById(R.id.messageEditText);
        mSendMessageImageView = getView().findViewById(R.id.sendMessageImageView);
        mSendMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mMessageEditText.getText().toString();
                ChatMessage chatMessage = new ChatMessage(input,mCurrentUser);
                if (mMessageList==null){
                    UIUtils.showToast(getActivity()," Send Failed, Please Check Connection");
                }
                mMessageList.add(chatMessage);
                mChatRoomDatabaseReference.child(mRoomKey).setValue(mMessageList);
                mChatRoomAdapter.notifyDataSetChanged();
            }
        });
        initializeRecycler();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void initializeRecycler(){
        mMessageRecyclerView = getView().findViewById(R.id.chatRoomRecyclerView);
        mChatRoomDatabaseReference = FirebaseDatabase.getInstance().getReference("RecipeHub").child("chatroom");
        mChatRoomDatabaseReference.child(mRoomKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<ChatMessage>> t = new GenericTypeIndicator<List<ChatMessage>>() {};
                List<ChatMessage> messagesList = dataSnapshot.getValue(t);
                if (messagesList == null){
                    messagesList = new ArrayList<>();
                    mChatRoomDatabaseReference.child(mRoomKey).setValue(mMessageList);
                }
                if(mMessageList==null){
                    mMessageList = new ArrayList<>();
                }


                mMessageList.clear();

                mMessageList.addAll(messagesList);

                initializeAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void initializeAdapter(){
        if (mChatRoomAdapter==null){
            mChatRoomAdapter = new ChatRoomAdapter(mMessageList,mCurrentUser,mOtherUser);
            mMessageRecyclerView.setAdapter(mChatRoomAdapter);
            mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.VERTICAL,false));
        } else {
            mChatRoomAdapter.notifyDataSetChanged();
        }

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
