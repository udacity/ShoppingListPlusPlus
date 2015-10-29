package com.udacity.firebase.shoppinglistplusplus.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Scope;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.User;
import com.udacity.firebase.shoppinglistplusplus.ui.BaseActivity;
import com.udacity.firebase.shoppinglistplusplus.ui.MainActivity;
import com.udacity.firebase.shoppinglistplusplus.utils.Constants;
import com.udacity.firebase.shoppinglistplusplus.utils.Utils;

import java.io.IOException;
import java.util.HashMap;

/**
 * Represents Sign in screen and functionality of the app
 */
public class LoginActivity extends BaseActivity {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;
    /* References to the Firebase */
    private Firebase mFirebaseRef;
    private EditText mEditTextEmailInput, mEditTextPasswordInput;

    /**
     * Variables related to Google Login
     */
    /* A flag indicating that a PendingIntent is in progress and prevents us from starting further intents. */
    private boolean mGoogleIntentInProgress;
    /* Request code used to invoke sign in user interactions for Google+ */
    public static final int RC_GOOGLE_LOGIN = 1;
    /* A Google account object that is populated if the user signs in with Google */
    GoogleSignInAccount mGoogleAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**
         * Create Firebase references
         */
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        /**
         * Link layout elements from XML and setup progress dialog
         */
        initializeScreen();

        /**
         * Call signInPassword() when user taps "Done" keyboard action
         */
        mEditTextPasswordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    signInPassword();
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO Check to see whether SharedPreferences has an email set for someone trying
        // to sign up. If it does, then:
        //  1. Display that email
        //  2. Clear the email from shared preferences
    }

    @Override
    public void onPause() {
        super.onPause();
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
     * Sign in with Password provider when user clicks sign in button
     */
    public void onSignInPressed(View view) {
        signInPassword();
    }

    /**
     * Open CreateAccountActivity when user taps on "Sign up" TextView
     */
    public void onSignUpPressed(View view) {
        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        startActivity(intent);
    }

    /**
     * Link layout elements from XML and setup the progress dialog
     */
    public void initializeScreen() {
        mEditTextEmailInput = (EditText) findViewById(R.id.edit_text_email);
        mEditTextPasswordInput = (EditText) findViewById(R.id.edit_text_password);
        LinearLayout linearLayoutLoginActivity = (LinearLayout) findViewById(R.id.linear_layout_login_activity);
        initializeBackground(linearLayoutLoginActivity);
        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getString(R.string.progress_dialog_authenticating_with_firebase));
        mAuthProgressDialog.setCancelable(false);
        /* Setup Google Sign In */
        setupGoogleSignIn();
    }

    /**
     * Sign in with Password provider (used when user taps "Done" action on keyboard)
     */
    public void signInPassword() {
        String email = mEditTextEmailInput.getText().toString();
        String password = mEditTextPasswordInput.getText().toString();

        /**
         * If email and password are not empty show progress dialog and try to authenticate
         */
        if (email.equals("")) {
            mEditTextEmailInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }

        if (password.equals("")) {
            mEditTextPasswordInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }
        mAuthProgressDialog.show();
        mFirebaseRef.authWithPassword(email, password, new MyAuthResultHandler(Constants.PASSWORD_PROVIDER));
    }

    /**
     * Handle user authentication that was initiated with mFirebaseRef.authWithPassword
     * or mFirebaseRef.authWithOAuthToken
     */
    private class MyAuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public MyAuthResultHandler(String provider) {
            this.provider = provider;
        }

        /**
         * On successful authentication call setAuthenticatedUser if it was not already
         * called in
         */
        @Override
        public void onAuthenticated(AuthData authData) {
            mAuthProgressDialog.dismiss();
            Log.i(LOG_TAG, provider + " " + getString(R.string.log_message_auth_successful));

            if (authData != null) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor spe = sp.edit();
                /**
                 * If user has logged in with Google provider
                 */
                if (authData.getProvider().equals(Constants.PASSWORD_PROVIDER)) {
                    setAuthenticatedUserPasswordProvider(authData);
                } else
                /**
                 * If user has logged in with Password provider
                 */
                    if (authData.getProvider().equals(Constants.GOOGLE_PROVIDER)) {
                        setAuthenticatedUserGoogle(authData);
                    } else {
                        Log.e(LOG_TAG, getString(R.string.log_error_invalid_provider) + authData.getProvider());
                    }

                /* Save provider name and encodedEmail for later use and start MainActivity */
                spe.putString(Constants.KEY_PROVIDER, authData.getProvider()).apply();
                spe.putString(Constants.KEY_ENCODED_EMAIL, mEncodedEmail).apply();

                /* Go to main activity */
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            mAuthProgressDialog.dismiss();

            /**
             * Use utility method to check the network connection state
             * Show "No network connection" if there is no connection
             * Show Firebase specific error message otherwise
             */
            switch (firebaseError.getCode()) {
                case FirebaseError.INVALID_EMAIL:
                case FirebaseError.USER_DOES_NOT_EXIST:
                    mEditTextEmailInput.setError(getString(R.string.error_message_email_issue));
                    break;
                case FirebaseError.INVALID_PASSWORD:
                    mEditTextPasswordInput.setError(firebaseError.getMessage());
                    break;
                case FirebaseError.NETWORK_ERROR:
                    showErrorToast(getString(R.string.error_message_failed_sign_in_no_network));
                    break;
                default:
                    showErrorToast(firebaseError.toString());
            }
        }
    }
    
    /**
     * Helper method that makes sure a user is created if the user
     * logs in with Firebase's email/password provider.
     *
     * @param authData AuthData object returned from onAuthenticated
     */
    private void setAuthenticatedUserPasswordProvider(AuthData authData) {
        final String unprocessedEmail = authData.getProviderData().get(Constants.FIREBASE_PROPERTY_EMAIL).toString().toLowerCase();
        /**
         * Encode user email replacing "." with ","
         * to be able to use it as a Firebase db key
         */
        mEncodedEmail = Utils.encodeEmail(unprocessedEmail);

        // TODO Using a boolean in the User class, check if the current person who's logging in
        // has never logged in before. If they have not:
        // Change the password to be the password they just typed in (which will be the reset
        // password they got from their email). You do this to ensure the reset password will
        // last more than 24 hours.
        //
        // If you're wondering what happens to users who do not log in 24 hours after they create
        // an account, they are in fact locked out. This is an edge case that you can consider
        // a way to solve.
    }

    /**
     * Helper method that makes sure a user is created if the user
     * logs in with Firebase's Google login provider.
     *
     * @param authData AuthData object returned from onAuthenticated
     */
    private void setAuthenticatedUserGoogle(AuthData authData) {
/**
 * If google api client is connected, get the lowerCase user email
 * and save in sharedPreferences
 */
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor spe = sp.edit();
        String unprocessedEmail;
        if (mGoogleApiClient.isConnected()) {
            unprocessedEmail = mGoogleAccount.getEmail().toLowerCase();
            spe.putString(Constants.KEY_GOOGLE_EMAIL, unprocessedEmail).apply();
        } else {

            /**
             * Otherwise get email from sharedPreferences, use null as default value
             * (this mean that user resumes his session)
             */
            unprocessedEmail = sp.getString(Constants.KEY_GOOGLE_EMAIL, null);
        }
        /**
         * Encode user email replacing "." with "," to be able to use it
         * as a Firebase db key
         */
        mEncodedEmail = Utils.encodeEmail(unprocessedEmail);

            /* Get username from authData */
        final String userName = (String) authData.getProviderData().get(Constants.PROVIDER_DATA_DISPLAY_NAME);

            /* If no user exists, make a user */
        final Firebase userLocation = new Firebase(Constants.FIREBASE_URL_USERS).child(mEncodedEmail);
        userLocation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    /* If nothing is there ...*/
                if (dataSnapshot.getValue() == null) {
                    HashMap<String, Object> timestampJoined = new HashMap<>();
                    timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

                    User newUser = new User(userName, mEncodedEmail, timestampJoined);
                    userLocation.setValue(newUser);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(LOG_TAG, getString(R.string.log_error_occurred) + firebaseError.getMessage());
            }
        });
        
    }

    /**
     * Show error toast to users
     */
    private void showErrorToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }


    /**
     * Signs you into ShoppingList++ using the Google Login Provider
     *
     * @param token A Google OAuth access token returned from Google
     */
    private void loginWithGoogle(String token) {
        mFirebaseRef.authWithOAuthToken(Constants.GOOGLE_PROVIDER, token, new MyAuthResultHandler(Constants.GOOGLE_PROVIDER));
    }

    /**
     * GOOGLE SIGN IN CODE
     *
     * This code is mostly boiler plate from
     * https://developers.google.com/identity/sign-in/android/start-integrating
     * and
     * https://github.com/googlesamples/google-services/blob/master/android/signin/app/src/main/java/com/google/samples/quickstart/signin/SignInActivity.java
     *
     * The big picture steps are:
     * 1. User clicks the sign in with Google button
     * 2. An intent is started for sign in.
     *      - If the connection fails it is caught in the onConnectionFailed callback
     *      - If it finishes, onActivityResult is called with the correct request code.
     * 3. If the sign in was successful, set the mGoogleAccount to the current account and
     * then call get GoogleOAuthTokenAndLogin
     * 4. getGoogleOAuthTokenAndLogin launches an AsyncTask to get an OAuth2 token from Google.
     * 5. Once this token is retrieved it is available to you in the onPostExecute method of
     * the AsyncTask. **This is the token required by Firebase**
     */


    /* Sets up the Google Sign In Button : https://developers.google.com/android/reference/com/google/android/gms/common/SignInButton */
    private void setupGoogleSignIn() {
        SignInButton signInButton = (SignInButton)findViewById(R.id.login_with_google);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignInGooglePressed(v);
            }
        });
    }

    /**
     * Sign in with Google plus when user clicks "Sign in with Google" textView (button)
     */
    public void onSignInGooglePressed(View view) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_LOGIN);
        mAuthProgressDialog.show();

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        /**
         * An unresolvable error has occurred and Google APIs (including Sign-In) will not
         * be available.
         */
        mAuthProgressDialog.dismiss();
        showErrorToast(result.toString());
    }


    /**
     * This callback is triggered when any startActivityForResult finishes. The requestCode maps to
     * the value passed into startActivityForResult.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...); */
        if (requestCode == RC_GOOGLE_LOGIN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(LOG_TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            /* Signed in successfully, get the OAuth token */
            mGoogleAccount = result.getSignInAccount();
            getGoogleOAuthTokenAndLogin();


        } else {
            if (result.getStatus().getStatusCode() == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) {
                showErrorToast("The sign in was cancelled. Make sure you're connected to the internet and try again.");
            } else {
                showErrorToast("Error handling the sign in: " + result.getStatus().getStatusMessage());
            }
            mAuthProgressDialog.dismiss();
        }
    }

    /**
     * Gets the GoogleAuthToken and logs in.
     */
    private void getGoogleOAuthTokenAndLogin() {
        /* Get OAuth token in Background */
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            String mErrorMessage = null;

            @Override
            protected String doInBackground(Void... params) {
                String token = null;

                try {
                    String scope = String.format(getString(R.string.oauth2_format), new Scope(Scopes.PROFILE)) + " email";

                    token = GoogleAuthUtil.getToken(LoginActivity.this, mGoogleAccount.getEmail(), scope);
                } catch (IOException transientEx) {
                    /* Network or server error */
                    Log.e(LOG_TAG, getString(R.string.google_error_auth_with_google) + transientEx);
                    mErrorMessage = getString(R.string.google_error_network_error) + transientEx.getMessage();
                } catch (UserRecoverableAuthException e) {
                    Log.w(LOG_TAG, getString(R.string.google_error_recoverable_oauth_error) + e.toString());

                    /* We probably need to ask for permissions, so start the intent if there is none pending */
                    if (!mGoogleIntentInProgress) {
                        mGoogleIntentInProgress = true;
                        Intent recover = e.getIntent();
                        startActivityForResult(recover, RC_GOOGLE_LOGIN);
                    }
                } catch (GoogleAuthException authEx) {
                    /* The call is not ever expected to succeed assuming you have already verified that
                     * Google Play services is installed. */
                    Log.e(LOG_TAG, " " + authEx.getMessage(), authEx);
                    mErrorMessage = getString(R.string.google_error_auth_with_google) + authEx.getMessage();
                }
                return token;
            }

            @Override
            protected void onPostExecute(String token) {
                mAuthProgressDialog.dismiss();
                if (token != null) {
                    /* Successfully got OAuth token, now login with Google */
                    loginWithGoogle(token);
                } else if (mErrorMessage != null) {
                    showErrorToast(mErrorMessage);
                }
            }
        };

        task.execute();
    }
}