package com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails;

import android.app.Dialog;
import android.os.Bundle;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;
import com.udacity.firebase.shoppinglistplusplus.model.User;
import com.udacity.firebase.shoppinglistplusplus.utils.Constants;
import com.udacity.firebase.shoppinglistplusplus.utils.Utils;

import java.util.HashMap;

/**
 * Lets user edit the list name for all copies of the current list
 */
public class EditListNameDialogFragment extends EditListDialogFragment {
    private static final String LOG_TAG = ActiveListDetailsActivity.class.getSimpleName();
    String mListName;

    /**
     * Public static constructor that creates fragment and passes a bundle with data into it when adapter is created
     */
    public static EditListNameDialogFragment newInstance(ShoppingList shoppingList, String listId,
                                                         String encodedEmail,
                                                         HashMap<String, User> sharedWithUsers) {
        EditListNameDialogFragment editListNameDialogFragment = new EditListNameDialogFragment();
        Bundle bundle = EditListDialogFragment.newInstanceHelper(shoppingList,
                R.layout.dialog_edit_list, listId, encodedEmail, sharedWithUsers);
        bundle.putString(Constants.KEY_LIST_NAME, shoppingList.getListName());
        editListNameDialogFragment.setArguments(bundle);
        return editListNameDialogFragment;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListName = getArguments().getString(Constants.KEY_LIST_NAME);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        /** {@link EditListDialogFragment#createDialogHelper(int)} is a
         * superclass method that creates the dialog
         **/
        Dialog dialog = super.createDialogHelper(R.string.positive_button_edit_item);
        /**
         * {@link EditListDialogFragment#helpSetDefaultValueEditText(String)} is a superclass
         * method that sets the default text of the TextView
         */
        helpSetDefaultValueEditText(mListName);
        return dialog;
    }

    /**
     * Changes the list name in all copies of the current list
     */
    protected void doListEdit() {
        final String inputListName = mEditTextForList.getText().toString();
        /**
         * Check that the user inputted list name is not empty, has changed the original name
         * and that the dialog was properly initialized with the current name and id of the list.
         */
        if (!inputListName.equals("") && mListName != null &&
                mListId != null && !inputListName.equals(mListName)) {

            Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL);

            /**
             * Create map and fill it in with deep path multi write operations list
             */
            HashMap<String, Object> updatedListData = new HashMap<String, Object>();

            /* Add the value to update at the specified property for all lists */
            Utils.updateMapForAllWithValue(mSharedWith, mListId, mOwner, updatedListData,
                    Constants.FIREBASE_PROPERTY_LIST_NAME, inputListName);

            /* Update affected lists timestamps */
            Utils.updateMapWithTimestampLastChanged(mSharedWith, mListId, mOwner, updatedListData);

            /* Do a deep-path update */
            firebaseRef.updateChildren(updatedListData, new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    /* Now that we have the timestamp, update the reversed timestamp */
                    Utils.updateTimestampReversed(firebaseError, LOG_TAG, mListId,
                            mSharedWith, mOwner);
                }
            });

        }
    }
}

