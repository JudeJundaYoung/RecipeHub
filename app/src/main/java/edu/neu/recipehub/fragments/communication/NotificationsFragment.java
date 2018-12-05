package edu.neu.recipehub.fragments.communication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.neu.recipehub.MainActivity;
import edu.neu.recipehub.R;
import edu.neu.recipehub.fragments.adapters.NotificationAdapter;
import edu.neu.recipehub.utils.OnSwipeTouchListener;
import edu.neu.recipehub.utils.UIUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {
    private List<String> mNotificationsList;

    private CommunicationFragment.OnFragmentInteractionListener mListener;

    private DatabaseReference mNotificationsDatabaseRef;

    private RecyclerView mNotificationsRecyclerView;

    private NotificationAdapter mNotificationAdapter;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance() {
        NotificationsFragment fragment = new NotificationsFragment();
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
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mNotificationsRecyclerView = getView().findViewById(R.id.notificationsRecyclerView);
        mNotificationsList = new ArrayList<>();
        mNotificationAdapter = new NotificationAdapter(mNotificationsList);
        mNotificationsRecyclerView.setAdapter(mNotificationAdapter);
        mNotificationsRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        getNotificationList();
        setSwipeListener();
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


    private void getNotificationList(){
        if (mNotificationsDatabaseRef==null){
            mNotificationsDatabaseRef = FirebaseDatabase.getInstance().getReference("RecipeHub")
                    .child("notifications");
        }
        mNotificationsDatabaseRef.child(((MainActivity)getActivity()).getmCurrentUser().mUserName).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mNotificationsList.clear();
                        java.util.List<String> resultList = (List<String>) dataSnapshot.getValue();
                        if (resultList==null){
                            resultList = new ArrayList<>();
                            mNotificationsDatabaseRef.child(((MainActivity)getActivity())
                                    .getmCurrentUser().mUserName).setValue(resultList);
                        }
                        for(String string: resultList){
                            mNotificationsList.add(string);
                        }
                        Collections.reverse(mNotificationsList);
                        mNotificationAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setSwipeListener(){
        mNotificationsRecyclerView.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
            @Override
            public void onSwipeLeft() {
                super.onSwipeRight();
                ((CommunicationFragment)((MainActivity) getActivity()).getmFragment())
                        .changeButton(CommunicationNavigationBarFragment.HIGHLIGHT_MESSAGE);
                ((CommunicationFragment)((MainActivity) getActivity()).getmFragment())
                        .changeLowerFragment(MessagesFragment.newInstance());
            }
        });
    }
}
