package com.udacity.firebase.shoppinglistplusplus.ui.sharing;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.User;
import com.udacity.firebase.shoppinglistplusplus.ui.BaseActivity;
import com.udacity.firebase.shoppinglistplusplus.utils.Constants;

/**
 * Represents the Add Friend screen and functionality
 */
public class AddFriendActivity extends BaseActivity {
    private EditText mEditTextAddFriendEmail;
    private AutocompleteFriendAdapter mFriendsAutocompleteAdapter;
    private ListView mListViewAutocomplete;
    private Firebase mUsersRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        /**
         * Create Firebase references
         */
        mUsersRef = new Firebase(Constants.FIREBASE_URL_USERS);

        /**
         * Link layout elements from XML and setup the toolbar
         */
        initializeScreen();

        /**
         * Set interactive bits, such as click events/adapters
         */

        mFriendsAutocompleteAdapter = new AutocompleteFriendAdapter(AddFriendActivity.this, User.class,
                R.layout.single_autocomplete_item, mUsersRef.orderByChild(Constants.FIREBASE_PROPERTY_EMAIL),
                mEncodedEmail);

        mListViewAutocomplete.setAdapter(mFriendsAutocompleteAdapter);

        /**mEditTextAddFriendEmail.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        // TODO This is where you can add the search code. Tips:
        // 1.   The easiest way to do this involves just making a new adapter with a new query.
        // 2.   Since you'll be making lots of adapters, make sure to clean up the old ones.
        // 3.   If the user has types less than 2 characters, then nothing should be displayed.

        }
        });**/

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFriendsAutocompleteAdapter.cleanup();
    }

    /**
     * Link layout elements from XML and setup the toolbar
     */
    public void initializeScreen() {
        mListViewAutocomplete = (ListView) findViewById(R.id.list_view_friends_autocomplete);
        mEditTextAddFriendEmail = (EditText) findViewById(R.id.edit_text_add_friend_email);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        /* Add back button to the action bar */
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
