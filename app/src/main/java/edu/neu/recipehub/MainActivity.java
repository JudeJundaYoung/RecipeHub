package edu.neu.recipehub;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import edu.neu.recipehub.fragments.communication.CommunicationFragment;
import edu.neu.recipehub.fragments.favorite.FavoriteFragment;
import edu.neu.recipehub.fragments.forks.ForksFragment;
import edu.neu.recipehub.fragments.home.CollectionFragment;
import edu.neu.recipehub.fragments.home.HomeFragment;
import edu.neu.recipehub.fragments.home.SearchFragment;
import edu.neu.recipehub.fragments.usercenter.UserCenterFragment;
import edu.neu.recipehub.objects.User;
import edu.neu.recipehub.users.UserEntry;
import edu.neu.recipehub.utils.UIUtils;

public class MainActivity extends AppCompatActivity
        implements SensorListener,
        HomeFragment.OnFragmentInteractionListener,
        UserCenterFragment.OnFragmentInteractionListener,
        SearchFragment.OnFragmentInteractionListener {


    //public static final String USER_NAME = "userName";
    private static final float SHAKE_THRESHOLD = 1;

    public static String USER_NAME;

    private DatabaseReference mUserDatabaseRef;

    private DatabaseReference mFavoritesDatabaseRef;

    private DatabaseReference mNotificationsDatabaseRef;

    private User mCurrentUser;

    private Map<String, String> mFavorites;

    private List<String> mHelperNotificationList;

    private List<String> mCurrentUserNotificationList;

    private Stack<Fragment> mFragmentStack;

    private Fragment mFragment;

    private FragmentManager mFragmentManager;

    private BottomNavigationView mBottomNavigationView;

    private Context mContext;

    private SensorManager mSensorManager;
    private float mXLocation;
    private float mYLocation;
    private float mZLocation;
    private long mLastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentStack = new Stack<>();
        getUser(getIntent().getStringExtra(UserEntry.USER_NAME));
        getNotifications();
        initializeBottomNavigationView();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(this, SensorManager.SENSOR_ACCELEROMETER);
        mContext = this;
    }

    private void initializeBottomNavigationView() {
        mFragmentManager = getSupportFragmentManager();

        mFragment = HomeFragment.newInstance();
        mFragmentStack.add(mFragment);

        // Make the activity display default mFragment.
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentFrameLayout, mFragment).commit();


        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                mFragmentStack.clear();
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.homeMenuItem:
                        fragment = HomeFragment.newInstance();
                        if (mFragment instanceof HomeFragment) {
                            return true;
                        }
                        break;
                    case R.id.favouriteMenuItem:
                        fragment = FavoriteFragment.newInstance();
                        if (mFragment instanceof FavoriteFragment) {
                            return true;
                        }
                        break;
                    case R.id.forksMenuItem:
                        fragment = ForksFragment.newInstance();
                        if (mFragment instanceof ForksFragment) {
                            return true;
                        }
                        break;
                    case R.id.communicationMenuItem:
                        fragment = CommunicationFragment.newInstance();
                        if (mFragment instanceof CommunicationFragment) {
                            return true;
                        }
                        break;
                    case R.id.usercenterMenuItem:
                        fragment = UserCenterFragment.newInstance(mCurrentUser);
                        if (mFragment instanceof UserCenterFragment) {
                            return true;
                        }
                        break;
                }
                mFragment = fragment;
                changeCurrentFragment(fragment);
                return true;
            }
        });
    }

    private void changeCurrentFragment(Fragment fragment) {
        mFragmentStack.push(fragment);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.fragmentFrameLayout, fragment).commit();
        UIUtils.hideKeyboard(this);
    }

    private void getUser(final String userName) {
        mUserDatabaseRef = FirebaseDatabase.getInstance().getReference("RecipeHub")
                .child("users").child(userName);
        mUserDatabaseRef.setValue(userName);


        mFavoritesDatabaseRef = FirebaseDatabase.getInstance().getReference("RecipeHub")
                .child("favorites").child(userName);
//        final Set<Recipe> myRecipe = new ArrayList<>();
        mFavoritesDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFavorites = (Map<String, String>) dataSnapshot.getValue();
                if (mFavorites == null) {
                    mFavorites = new HashMap<>();
                    mFavoritesDatabaseRef.setValue(mFavorites);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mCurrentUser = new User(userName);
        USER_NAME = userName;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mFavorites == null) {

                }
                mFavorites.put("123", "!23");
            }
        }).start();
    }

    private void getNotifications(){
        if (mNotificationsDatabaseRef==null){
            mNotificationsDatabaseRef = FirebaseDatabase.getInstance().getReference("RecipeHub")
                    .child("notifications");
        }

        mNotificationsDatabaseRef.child(mCurrentUser.mUserName).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mCurrentUserNotificationList = (List<String>) dataSnapshot.getValue();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to get a recommendation?");
        builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final ArrayList<String> keys = new ArrayList<>();
                keys.add("-LT-KO0_TEpgkYkkOE57");
                keys.add("-LT-LkeyQG0EMG_2fRxo");
                keys.add("-LT0WFt7PRaaQ7DmudKX");
                changeCurrentFragment(CollectionFragment.newInstance(keys));
            }
        });
        builder.setNegativeButton("No!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UIUtils.showToast(mContext, "You'll regret that");
            }
        });
        builder.show();
    }

    @Override
    public void changeFragmentInHomeFragment(Fragment fragment) {
        changeCurrentFragment(fragment);
    }

    @Override
    public void onGoBackButtonClick() {
        Fragment previousFragment = mFragmentStack.pop();
        previousFragment = mFragmentStack.pop();

        mFragmentStack.push(previousFragment);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.fragmentFrameLayout, previousFragment).commit();
        UIUtils.hideKeyboard(this);
    }

    @Override
    public void logOut() {
        finish();
    }

    @Override
    public void onAccuracyChanged(int sensor, int accuracy) {
    }

    /**
     * Shake the phone to do something
     */
    @Override
    public void onSensorChanged(int sensor, float[] values) {
        if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long diffTime = currentTime - mLastUpdate;

            if (diffTime > 1000) {
                mLastUpdate = currentTime;
                float x = values[SensorManager.DATA_X];
                float y = values[SensorManager.DATA_Y];
                float z = values[SensorManager.DATA_Z];

                float speed = (Math.abs(x + y + z - mXLocation - mYLocation - mZLocation) / diffTime) * 10000;
                if (speed > SHAKE_THRESHOLD) {
                    UIUtils.showToast(this, "!23");
                    showDialog();
                }

                mXLocation = x;
                mYLocation = y;
                mZLocation = z;
            }
        }
    }






    public void removeFromFavorites(String string) {
        if (mFavorites.containsKey(string)) {
            mFavorites.remove(string);
            mFavoritesDatabaseRef.setValue(mFavorites);
        }
    }

    public void addToFavorites(String string) {
        if (!mFavorites.containsKey(string)) {
            mFavorites.put(string, "123");
            mFavoritesDatabaseRef.setValue(mFavorites);
        }
    }

    public void sentNotification(final String notification, final String user){
        if (mNotificationsDatabaseRef==null){
            mNotificationsDatabaseRef = FirebaseDatabase.getInstance().getReference("RecipeHub")
                    .child("notifications");
        }

        mHelperNotificationList = null;

        mNotificationsDatabaseRef.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mHelperNotificationList = (List<String>)dataSnapshot.getValue();
                if (mHelperNotificationList == null){
                    mHelperNotificationList = new ArrayList<>();
                }
                mHelperNotificationList.add(notification);
                mNotificationsDatabaseRef.child(user).setValue(mHelperNotificationList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public User getmCurrentUser() {
        return mCurrentUser;
    }

    public Map<String, String> getmFavorites() {
        return mFavorites;
    }

    public Fragment getmFragment() {
        return mFragment;
    }

    public DatabaseReference getmUserDatabaseRef() {
        return mUserDatabaseRef;
    }
}
