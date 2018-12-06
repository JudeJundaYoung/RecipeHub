package edu.neu.recipehub.fragments.communication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.neu.recipehub.MainActivity;
import edu.neu.recipehub.R;
import edu.neu.recipehub.utils.OnSwipeTouchListener;
import edu.neu.recipehub.utils.UIUtils;


public class MessagesFragment extends Fragment {

    private RecyclerView mMessagesRecyclerView;

    private FloatingActionButton mAddChatFAB;

    private MainActivity mMainActivity;

    private DatabaseReference mUserDateBaseReference;

    private DatabaseReference mChattingRoomReference;
    
    public static MessagesFragment newInstance() {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
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
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMainActivity = (MainActivity) getActivity();
        mMessagesRecyclerView = getView().findViewById(R.id.messagesRecyclerView);
        mUserDateBaseReference = FirebaseDatabase.getInstance().getReference("RecipeHub").child("users");
        mChattingRoomReference = FirebaseDatabase.getInstance().getReference("RecipeHub").child("chatroom");
        setSwipeListener();
        mAddChatFAB = getActivity().findViewById(R.id.addChatFloatingActionButton);
        mAddChatFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddChatDialog();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setSwipeListener(){
        mMessagesRecyclerView.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                ((CommunicationFragment)((MainActivity) getActivity()).getmFragment())
                        .changeButton(CommunicationNavigationBarFragment.HIGHLIGHT_NOTIFICATION);
                ((CommunicationFragment)((MainActivity) getActivity()).getmFragment())
                        .changeLowerFragment(NotificationsFragment.newInstance());
            }
        });
    }
    
    private void showAddChatDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter the one you want to chat with");

        final EditText input = new EditText(getActivity());
        input.setHint("The user name");
        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                startChat(input.getText().toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        builder.show();
    }

    private void startChat(final String userName){
        if (userName.equals(mMainActivity.getmCurrentUser().mUserName)){
            UIUtils.showToast(mMainActivity,"Don't talk to yourself!");
            return;
        }
        mUserDateBaseReference.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String)dataSnapshot.getValue();
                if (name == null){
                    UIUtils.showToast(mMainActivity,"There is no such user");
                } else {
                    createOrRetrieveChatRoom(userName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void createOrRetrieveChatRoom(String userName){

        String key = null;
        if (userName.compareTo(mMainActivity.getmCurrentUser().mUserName)<0){
            key = userName + "+" + mMainActivity.getmCurrentUser().mUserName;
        } else {
            key = mMainActivity.getmCurrentUser().mUserName + "+" + userName;
        }

        mMainActivity.changeFragmentInHomeFragment(ChatRoomFragment.newInstance(key,
                mMainActivity.getmCurrentUser().mUserName, userName));
    }
}
