package com.udacity.firebase.shoppinglistplusplus.ui.sharing;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;
import com.udacity.firebase.shoppinglistplusplus.model.User;

import java.util.HashMap;

/**
 * Populates the list_view_friends_share inside ShareListActivity
 */
public class FriendAdapter extends FirebaseListAdapter<User> {
    private static final String LOG_TAG = FriendAdapter.class.getSimpleName();


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
        ((TextView) view.findViewById(R.id.user_name)).setText(friend.getName());
        // TODO This is where most of the heavy lifting will happen. In this method you should set
        // a listener on the user in the shared list of active lists.
        // When the listener is triggered with either the user being added or removed at the location
        // you should:
        //
        // 1.   Update the R.id.button_toggle_share button to the appropriate image resource, either
        //      R.drawable.ic_shared_check or R.drawable.icon_add_friend.
        // 2.   Call the updateFriendInSharedWith with addFriend false if you are removing the
        //      friend from the shared list or true if you are adding a friend. friendToAddOrRemove
        //      should be the friend.
    }

    /**
     * Public method that is used to pass ShoppingList object when it is loaded in ValueEventListener
     */
    public void setShoppingList(ShoppingList shoppingList) {
        // TODO Set the associated shopping list here; it will be called by ShareListActivity
        // whenever the shopping list changes.
    }

    /**
     * Public method that is used to pass SharedUsers when they are loaded in ValueEventListener
     */
    public void setSharedWithUsers(HashMap<String, User> sharedUsersList) {
        // TODO Set the associated shared with users; it will be called by ShareListActivity
        // whenever the shared with users changes.
    }

    /**
     * This method does the tricky job of adding or removing a friend from the sharedWith list.
     * @param addFriend This is true if the friend is being added, false is the friend is being removed.
     * @param friendToAddOrRemove This is the friend to either add or remove
     * @return
     */
    private HashMap<String, Object> updateFriendInSharedWith(Boolean addFriend, User friendToAddOrRemove) {
        // TODO This method should add or remove the friendToAddOrRemove to the sharedWith list of the
        // ShoppingList, depending on the value of addFriend.
        return null;
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }
}
