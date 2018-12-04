package edu.neu.recipehub.utils;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 *  A class help the application play some special movements in UI.
 */
public class UIUtils {

    static Toast mToast;

    public static void showToast(Context context, String toast){
        if(mToast!=null){
            mToast.cancel();
        }
        mToast = Toast.makeText(context,toast,Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
