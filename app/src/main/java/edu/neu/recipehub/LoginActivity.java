package edu.neu.recipehub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


import edu.neu.recipehub.users.UserEntry;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    /**
     * Start activity with customized user name.
     *
     * @param view
     */
    public void startButtonOnClick(View view) {
        EditText editText = findViewById(R.id.loginNameEditText);
        String userName = editText.getText().toString();
        if (userName.length() == 0) userName = UserEntry.ANONYMOUS_USER;
        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra(UserEntry.USER_NAME, userName);

        startActivity(intent);
    }

    /**
     * Start activity by default user name.
     *
     * @param view
     */
    public void startAsAnonymousButtonOnClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra(UserEntry.USER_NAME, UserEntry.ANONYMOUS_USER);

        startActivity(intent);
    }
}
