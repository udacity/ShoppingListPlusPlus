package com.udacity.firebase.shoppinglistplusplus.ui.sharing;

import android.app.Activity;
import android.view.View;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;
import com.udacity.firebase.shoppinglistplusplus.model.User;

import java.util.HashMap;

/**
 * Populates the list_view_friends_share inside ShareListActivity
 */
public class FriendAdapter extends FirebaseListAdapter<User> {
    private static final String LOG_TAG = FriendAdapter.class.getSimpleName();
    private HashMap <Firebase, ValueEventListener> mLocationListenerMap;


    /**
     * Public constructor that initializes private instance variables when adapter is created
     */
    public FriendAdapter(Activity activity, Class<User> modelClass, int modelLayout,
                         Query ref) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
    }

    /**
     * Protected method that populates the view attached to the adapter (list_view_friends_autocomplete)
     * with items inflated from single_user_item.xml
     * populateView also handles data changes and updates the listView accordingly
     */
    @Override
    protected void populateView(View view, final User friend) {

    }

    /**
     * Public method that is used to pass ShoppingList object when it is loaded in ValueEventListener
     */
    public void setShoppingList(ShoppingList shoppingList) {

    }

    /**
     * Public method that is used to pass SharedUsers when they are loaded in ValueEventListener
     */
    public void setSharedWithUsers(HashMap<String, User> sharedUsersList) {

    }


    /**
     * This method does the tricky job of adding or removing a friend from the sharedWith list.
     * @param addFriend This is true if the friend is being added, false is the friend is being removed.
     * @param friendToAddOrRemove This is the friend to either add or remove
     * @return
     */
    private HashMap<String, Object> updateFriendInSharedWith(Boolean addFriend, User friendToAddOrRemove) {
        return null;
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }
}
