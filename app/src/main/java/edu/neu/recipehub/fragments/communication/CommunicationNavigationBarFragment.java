package edu.neu.recipehub.fragments.communication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import edu.neu.recipehub.MainActivity;
import edu.neu.recipehub.R;

public class CommunicationNavigationBarFragment extends Fragment {

    public static final int HIGHLIGHT_NOTIFICATION = 1;
    public static final int HIGHLIGHT_MESSAGE =2;

    private OnFragmentInteractionListener mListener;

    private ImageView mNotificationImageView;

    private ImageView mMessageImageView;

    public CommunicationNavigationBarFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CommunicationNavigationBarFragment newInstance() {
        CommunicationNavigationBarFragment fragment = new CommunicationNavigationBarFragment();
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
        return inflater.inflate(R.layout.fragment_communication_navigation_bar, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mNotificationImageView = getView().findViewById(R.id.notificationsImageView);

        mNotificationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunicationFragment communicationFragment =
                        (CommunicationFragment) ((MainActivity)getActivity()).getmFragment();
                communicationFragment.changeLowerFragment(NotificationsFragment.newInstance());
                changeButton(HIGHLIGHT_NOTIFICATION);
            }
        });

        mMessageImageView = getView().findViewById(R.id.messagesImageView);

        mMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunicationFragment communicationFragment =
                        (CommunicationFragment) ((MainActivity)getActivity()).getmFragment();
                communicationFragment.changeLowerFragment(MessagesFragment.newInstance());
                changeButton(HIGHLIGHT_MESSAGE);
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
        mListener = null;
    }

    public void changeButton(int action){
        switch (action){
            case HIGHLIGHT_NOTIFICATION:
                mNotificationImageView.setImageResource(R.drawable.notifications_red);
                mMessageImageView.setImageResource(R.drawable.messages_black);
                break;
            case HIGHLIGHT_MESSAGE:
                mNotificationImageView.setImageResource(R.drawable.notifications_black);
                mMessageImageView.setImageResource(R.drawable.messages_red);
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
