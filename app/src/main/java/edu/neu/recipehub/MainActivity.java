package edu.neu.recipehub;

import android.content.Context;
import android.hardware.Sensor;
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

import com.algolia.search.saas.Client;

import edu.neu.recipehub.fragments.CommunicationFragment;
import edu.neu.recipehub.fragments.FavoriteFragment;
import edu.neu.recipehub.fragments.ForksFragment;
import edu.neu.recipehub.fragments.HomeFragment;
import edu.neu.recipehub.fragments.SearchFragment;
import edu.neu.recipehub.fragments.UserCenterFragment;
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

    public static  String USER_NAME;



    private User mCurrentUser;

    private Context mContext;
    private Fragment mFragment;
    private Fragment mPreviousFragment;
    private FragmentManager mFragmentManager;
    private BottomNavigationView mBottomNavigationView;

    private SensorManager mSensorManager;
    private float mXLocation;
    private float mYLocation;
    private float mZLocation;
    private long mLastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getUser(getIntent().getStringExtra(UserEntry.USER_NAME));
        initializeBottomNavigationView();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(this,SensorManager.SENSOR_ACCELEROMETER);
        mPreviousFragment = null;
    }

    private void initializeBottomNavigationView(){
        mFragmentManager = getSupportFragmentManager();

        mFragment = HomeFragment.newInstance();

        // Make the activity display default mFragment.
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentFrameLayout, mFragment).commit();



        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()){
                    case R.id.homeMenuItem:
                        fragment = HomeFragment.newInstance();
                        if (mFragment instanceof HomeFragment){
                            return true;
                        }
                        break;
                    case R.id.favouriteMenuItem:
                        fragment = FavoriteFragment.newInstance();
                        if (mFragment instanceof FavoriteFragment){
                            return true;
                        }
                        break;
                    case R.id.forksMenuItem:
                        fragment = ForksFragment.newInstance();
                        if (mFragment instanceof ForksFragment){
                            return true;
                        }
                        break;
                    case R.id.communicationMenuItem:
                        fragment = CommunicationFragment.newInstance();
                        if (mFragment instanceof CommunicationFragment){
                            return true;
                        }
                        break;
                    case R.id.usercenterMenuItem:
                        fragment = UserCenterFragment.newInstance(mCurrentUser);
                        if (mFragment instanceof UserCenterFragment){
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

    private void changeCurrentFragment(Fragment fragment){
        mPreviousFragment = mFragment;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.fragmentFrameLayout, fragment).commit();
        UIUtils.hideKeyboard(this);
    }

    private void getUser(String userName){
        mCurrentUser = new User(userName);
        USER_NAME = userName;
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("It is a dialog");
        builder.show();
    }


    @Override
    public void changeFragmentInHomeFragment(Fragment fragment) {
        changeCurrentFragment(fragment);
    }

    @Override
    public void onGoBackButtonClick() {
        changeCurrentFragment(mPreviousFragment);
    }

    @Override
    public void logOut() {
        finish();
    }

    @Override
    public void onAccuracyChanged(int sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(int sensor, float[] values) {
        if (sensor == SensorManager.SENSOR_ACCELEROMETER){
            long currentTime = System.currentTimeMillis();
            long diffTime = currentTime - mLastUpdate;

            if (diffTime>1000){
                mLastUpdate = currentTime;
                float x = values[SensorManager.DATA_X];
                float y = values[SensorManager.DATA_Y];
                float z = values[SensorManager.DATA_Z];

                float speed = (Math.abs(x+y+z - mXLocation - mYLocation - mZLocation) / diffTime) * 10000;
                if (speed > SHAKE_THRESHOLD) {
                    UIUtils.showToast(this,"!23");
                    showDialog();
                }

                mXLocation = x;
                mYLocation = y;
                mZLocation = z;
            }
        }
    }



    public User getmCurrentUser() {
        return mCurrentUser;
    }
}
