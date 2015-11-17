package com.udacity.firebase.shoppinglistplusplus.ui.sharing;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;
import com.udacity.firebase.shoppinglistplusplus.model.User;
import com.udacity.firebase.shoppinglistplusplus.utils.Constants;
import com.udacity.firebase.shoppinglistplusplus.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Populates the list_view_friends_share inside ShareListActivity
 */
public class FriendAdapter extends FirebaseListAdapter<User> {
    private ShoppingList mShoppingList;
    private static final String LOG_TAG = FriendAdapter.class.getSimpleName();
    private String mListId;
    private Firebase mFirebaseRef;
    private HashMap<String, User> mSharedUsersList;
    private HashMap <Firebase, ValueEventListener> mLocationListenerMap;


    /**
     * Public constructor that initializes private instance variables when adapter is created
     */
    public FriendAdapter(Activity activity, Class<User> modelClass, int modelLayout,
                         Query ref, String listId) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
        this.mListId = listId;
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        mLocationListenerMap = new HashMap<Firebase, ValueEventListener>();
    }

    /**
     * Protected method that populates the view attached to the adapter (list_view_friends_autocomplete)
     * with items inflated from single_user_item.xml
     * populateView also handles data changes and updates the listView accordingly
     */
    @Override
    protected void populateView(View view, final User friend) {
        ((TextView) view.findViewById(R.id.user_name)).setText(friend.getName());
        final ImageButton buttonToggleShare = (ImageButton) view.findViewById(R.id.button_toggle_share);

        final Firebase sharedFriendInShoppingListRef = new Firebase(Constants.FIREBASE_URL_LISTS_SHARED_WITH)
                .child(mListId).child(friend.getEmail());

        /**
         * Gets the value of the friend from the ShoppingList's sharedWith list of users
         * and then allows the friend to be toggled as shared with or not.
         *
         * The friend in the snapshot (sharedFriendInShoppingList) will either be a User object
         * (if they are in the the sharedWith list) or null (if they are not in the sharedWith
         * list)
         */

        ValueEventListener listener = sharedFriendInShoppingListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final User sharedFriendInShoppingList = snapshot.getValue(User.class);

                /**
                 * If list is already being shared with this friend, set the buttonToggleShare
                 * to remove selected friend from sharedWith onClick and change the
                 * buttonToggleShare image to green
                 */
                if (sharedFriendInShoppingList != null) {
                    buttonToggleShare.setImageResource(R.drawable.ic_shared_check);
                    buttonToggleShare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            /**
                             * Create map and fill it in with deep path multi write operations list.
                             * Use false to mark that you are removing this friend
                             */
                            HashMap<String, Object> updatedUserData = updateFriendInSharedWith(false, friend);

                            /* Do a deep-path update */
                            mFirebaseRef.updateChildren(updatedUserData, new Firebase.CompletionListener() {
                                @Override
                                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                    Utils.updateTimestampReversed(firebaseError, LOG_TAG, mListId,
                                            mSharedUsersList, mShoppingList.getOwner());
                                }
                            });
                        }
                    });
                } else {

                    /**
                     * Set the buttonToggleShare onClickListener to add selected friend to sharedWith
                     * and change the buttonToggleShare image to grey otherwise
                     */
                    buttonToggleShare.setImageResource(R.drawable.icon_add_friend);
                    buttonToggleShare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            /**
                             * Create map and fill it in with deep path multi write operations list
                             */
                            HashMap<String, Object> updatedUserData = updateFriendInSharedWith(true, friend);

                            /* Do a deep-path update */
                            mFirebaseRef.updateChildren(updatedUserData, new Firebase.CompletionListener() {
                                @Override
                                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                    Utils.updateTimestampReversed(firebaseError, LOG_TAG, mListId,
                                            mSharedUsersList, mShoppingList.getOwner());
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(LOG_TAG,
                        mActivity.getString(R.string.log_error_the_read_failed) +
                                firebaseError.getMessage());
            }
        });
        /* Add the listener to the HashMap so that it can be removed on cleanup */
        mLocationListenerMap.put(sharedFriendInShoppingListRef, listener);

    }

    /**
     * Public method that is used to pass ShoppingList object when it is loaded in ValueEventListener
     */
    public void setShoppingList(ShoppingList shoppingList) {
        this.mShoppingList = shoppingList;
        this.notifyDataSetChanged();
    }

    /**
     * Public method that is used to pass SharedUsers when they are loaded in ValueEventListener
     */
    public void setSharedWithUsers(HashMap<String, User> sharedUsersList) {
        this.mSharedUsersList = sharedUsersList;
        this.notifyDataSetChanged();
    }

    /**
     * This method does the tricky job of adding or removing a friend from the sharedWith list.
     * @param addFriend This is true if the friend is being added, false is the friend is being removed.
     * @param friendToAddOrRemove This is the friend to either add or remove
     * @return
     */
    private HashMap<String, Object> updateFriendInSharedWith(Boolean addFriend, User friendToAddOrRemove) {
        HashMap<String, Object> updatedUserData = new HashMap<String, Object>();

        /* The newSharedWith lists contains all users who need their last time changed updated */
        HashMap<String, User> newSharedWith = new HashMap<String, User>(mSharedUsersList);

        if (addFriend) {
            /**
             * Changes the timestamp changed to now; Because of ancestry issues, we cannot
             * have one updateChildren call that both creates data and then updates that same data
             * because updateChildren has no way of knowing what was the intended update
             */
            mShoppingList.setTimestampLastChangedToNow();
            /* Make it a HashMap of the shopping list and user */
            final HashMap<String, Object> shoppingListForFirebase = (HashMap<String, Object>)
                    new ObjectMapper().convertValue(mShoppingList, Map.class);

            final HashMap<String, Object> friendForFirebase = (HashMap<String, Object>)
                    new ObjectMapper().convertValue(friendToAddOrRemove, Map.class);

            /* Add the friend to the shared list */
            updatedUserData.put("/" + Constants.FIREBASE_LOCATION_LISTS_SHARED_WITH + "/" + mListId +
                    "/" + friendToAddOrRemove.getEmail(), friendForFirebase);

            /* Add that shopping list hashmap to the new user's active lists */
            updatedUserData.put("/" + Constants.FIREBASE_LOCATION_USER_LISTS + "/" + friendToAddOrRemove.getEmail()
                    + "/" + mListId, shoppingListForFirebase);

        } else {
            /* Remove the friend from the shared list */
            updatedUserData.put("/" + Constants.FIREBASE_LOCATION_LISTS_SHARED_WITH + "/" + mListId +
                    "/" + friendToAddOrRemove.getEmail(), null);

            /* Remove the list from the shared friend */
            updatedUserData.put("/" + Constants.FIREBASE_LOCATION_USER_LISTS + "/" + friendToAddOrRemove.getEmail()
                    + "/" + mListId, null);

            newSharedWith.remove(friendToAddOrRemove.getEmail());

        }

        Utils.updateMapWithTimestampLastChanged(newSharedWith,
                mListId, mShoppingList.getOwner(), updatedUserData);

        return updatedUserData;
    }

    @Override
    public void cleanup() {
        super.cleanup();
        /* Clean up the event listeners */
        for (HashMap.Entry<Firebase, ValueEventListener> listenerToClean : mLocationListenerMap.entrySet())
        {
            listenerToClean.getKey().removeEventListener(listenerToClean.getValue());
        }

    }
}
