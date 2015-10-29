package com.udacity.firebase.shoppinglistplusplus.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.ui.BaseActivity;

/**
 * Represents Sign up screen and functionality of the app
 */
public class CreateAccountActivity extends BaseActivity {
    private static final String LOG_TAG = CreateAccountActivity.class.getSimpleName();
    private ProgressDialog mAuthProgressDialog;
    private EditText mEditTextUsernameCreate, mEditTextEmailCreate, mEditTextPasswordCreate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        /**
         * Link layout elements from XML and setup the progress dialog
         */
        initializeScreen();
    }

    /**
     * Override onCreateOptionsMenu to inflate nothing
     *
     * @param menu The menu with which nothing will happen
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    /**
     * Link layout elements from XML and setup the progress dialog
     */
    public void initializeScreen() {
        mEditTextUsernameCreate = (EditText) findViewById(R.id.edit_text_username_create);
        mEditTextEmailCreate = (EditText) findViewById(R.id.edit_text_email_create);
        mEditTextPasswordCreate = (EditText) findViewById(R.id.edit_text_password_create);
        LinearLayout linearLayoutCreateAccountActivity = (LinearLayout) findViewById(R.id.linear_layout_create_account_activity);
        initializeBackground(linearLayoutCreateAccountActivity);

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getResources().getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getResources().getString(R.string.progress_dialog_creating_user_with_firebase));
        mAuthProgressDialog.setCancelable(false);
    }

    /**
     * Open LoginActivity when user taps on "Sign in" textView
     */
    public void onSignInPressed(View view) {
        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Create new account using Firebase email/password provider
     */
    public void onCreateAccountPressed(View view) {
        // TODO Start by doing a client side error check of the three values that the
        // user entered. If any of the three values fail a check, you should stop
        // this method.

        // TODO Next, show the progress dialog by calling .show()

        // TODO Make the new user in Firebase here. If the user was not successfully made
        // either show a toast using show ErrorToast (if it was a more general problem)
        // or highlight the TextView that was the problem.
        // Regardless of success or failure, once Firebase responds you should close the
        // progress dialog using .dismiss().
    }

    /**
     * Creates a new user in Firebase from the Java POJO
     */
    private void createUserInFirebaseHelper(final String encodedEmail) {
    }

    private boolean isEmailValid(String email) {
        // TODO you should return whether or not the email is valid.
        // This should check using android.util.Patterns.EMAIL_ADDRESS
        // and show the user an error using TextView's .setError if not.
        return true;
    }

    private boolean isUserNameValid(String userName) {
        // TODO You should return whether the entered username is valid.
        // This should check whether something was entered
        // and show the user an error using TextView's .setError if not.
        return true;
    }

    private boolean isPasswordValid(String password) {
        // TODO You should return whether the entered password is valid.
        // This should check whether the password is at least 6 characters long
        // and show the user an error using TextView's .setError if not.
        return true;
    }

    /**
     * Show error toast to users
     */
    private void showErrorToast(String message) {
        Toast.makeText(CreateAccountActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
