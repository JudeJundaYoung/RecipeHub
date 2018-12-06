package edu.neu.recipehub.fragments.communication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.neu.recipehub.MainActivity;
import edu.neu.recipehub.R;
import edu.neu.recipehub.fragments.adapters.NotificationAdapter;
import edu.neu.recipehub.utils.OnSwipeTouchListener;
import edu.neu.recipehub.utils.UIUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommunicationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommunicationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunicationFragment extends Fragment {

    FragmentManager mFragmentManager;

    FrameLayout mUpperNavigationBarFrameLayout;

    Fragment mLowerFragment;

    FragmentManager mChildFragmentManager;

    public CommunicationFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CommunicationFragment newInstance() {
        CommunicationFragment fragment = new CommunicationFragment();
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
        return inflater.inflate(R.layout.fragment_communication, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mFragmentManager = getFragmentManager();
        mUpperNavigationBarFrameLayout = getView().findViewById(R.id.communicationNavigationBarFrameLayout);
        mChildFragmentManager = getChildFragmentManager();
        changeUpperFragment(CommunicationNavigationBarFragment.newInstance());
        changeLowerFragment(NotificationsFragment.newInstance());
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
    }

    public void changeUpperFragment(Fragment fragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.communicationNavigationBarFrameLayout, fragment).commit();
    }

    public void changeLowerFragment(Fragment fragment) {
        if (mLowerFragment instanceof NotificationsFragment && fragment instanceof NotificationsFragment){
            return;
        }
        if (mLowerFragment instanceof MessagesFragment && fragment instanceof MessagesFragment){
            return;
        }
        mLowerFragment = fragment;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mLowerFragment instanceof  NotificationsFragment) {
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        } else {
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        }
        transaction.replace(R.id.communicationLowerFrameLayout, fragment).commit();
    }

    public void changeButton(int action){
        for (Fragment fragment:mFragmentManager.getFragments()){
            if (fragment instanceof CommunicationNavigationBarFragment){
                ((CommunicationNavigationBarFragment) fragment).changeButton(action);
            }
        }
    }

}
