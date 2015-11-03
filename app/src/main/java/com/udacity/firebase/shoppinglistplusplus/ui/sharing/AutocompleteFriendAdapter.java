package com.udacity.firebase.shoppinglistplusplus.ui.sharing;

import android.app.Activity;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;
import com.udacity.firebase.shoppinglistplusplus.model.User;

/**
 * Populates the list_view_friends_autocomplete inside AddFriendActivity
 */
public class AutocompleteFriendAdapter extends FirebaseListAdapter<User> {

    /**
     * Public constructor that initializes private instance variables when adapter is created
     */
    public AutocompleteFriendAdapter(Activity activity, Class<User> modelClass, int modelLayout,
                                     Query ref) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
    }

    /**
     * Protected method that populates the view attached to the adapter (list_view_friends_autocomplete)
     * with items inflated from single_autocomplete_item.xml
     * populateView also handles data changes and updates the listView accordingly
     */
    @Override
    protected void populateView(View view, final User user) {
    }

    /** Checks if the friend you try to add is the current user **/
    private boolean isNotCurrentUser(User user) {
        return true;
    }

    /** Checks if the friend you try to add is already added, given a dataSnapshot of a user **/
    private boolean isNotAlreadyAdded(DataSnapshot dataSnapshot, User user) {
        return true;
    }

}
