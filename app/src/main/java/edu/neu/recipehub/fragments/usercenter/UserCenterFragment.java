package edu.neu.recipehub.fragments.usercenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.neu.recipehub.R;
import edu.neu.recipehub.objects.User;

public class UserCenterFragment extends Fragment {


    public static final String USER = "user";

    private User mCurrentUser;

    private TextView mUserNameTextView;

    private ImageView mLogOutImageView;

    private OnFragmentInteractionListener mListener;

    public UserCenterFragment() {
        // Required empty public constructor
    }

    public static UserCenterFragment newInstance(User user) {
        UserCenterFragment fragment = new UserCenterFragment();
        Bundle args = new Bundle();

        args.putSerializable(USER, user);

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
        return inflater.inflate(R.layout.fragment_user_center, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        mCurrentUser = (User) getArguments().getSerializable(USER);

        initializeView();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void logOut();
    }

    private void initializeView() {
        mUserNameTextView = getView().findViewById(R.id.userCenterUserNameTextView);

        mUserNameTextView.setText(mCurrentUser.mUserName);

        mLogOutImageView = getView().findViewById(R.id.logOutImageView);

        mLogOutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogOutDialog();
            }
        });
    }

    private void showLogOutDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("Log Out");
        dialogBuilder.setMessage("Are you sure to log out?");

        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.logOut();
            }
        });

        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialogBuilder.show();
    }
}
