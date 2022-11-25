package com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;
import com.udacity.firebase.shoppinglistplusplus.model.User;
import com.udacity.firebase.shoppinglistplusplus.utils.Constants;
import com.udacity.firebase.shoppinglistplusplus.utils.Utils;

import java.util.HashMap;

/**
 * Lets the user remove active shopping list
 */
public class RemoveListDialogFragment extends DialogFragment {
    String mListId;
    String mListOwner;
    HashMap mSharedWith;

    final static String LOG_TAG = RemoveListDialogFragment.class.getSimpleName();

    /**
     * Public static constructor that creates fragment and passes a bundle with data into it when adapter is created
     */
    public static RemoveListDialogFragment newInstance(ShoppingList shoppingList, String listId,
                                                       HashMap<String, User> sharedWithUsers) {
        RemoveListDialogFragment removeListDialogFragment = new RemoveListDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_LIST_ID, listId);
        bundle.putString(Constants.KEY_LIST_OWNER, shoppingList.getOwner());
        bundle.putSerializable(Constants.KEY_SHARED_WITH_USERS, sharedWithUsers);
        removeListDialogFragment.setArguments(bundle);
        return removeListDialogFragment;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListId = getArguments().getString(Constants.KEY_LIST_ID);
        mListOwner = getArguments().getString(Constants.KEY_LIST_OWNER);
        mSharedWith = (HashMap) getArguments().getSerializable(Constants.KEY_SHARED_WITH_USERS);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog)
                .setTitle(getActivity().getResources().getString(R.string.action_remove_list))
                .setMessage(getString(R.string.dialog_message_are_you_sure_remove_list))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        removeList();
                        /* Dismiss the dialog */
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        /* Dismiss the dialog */
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);

        return builder.create();
    }

    private void removeList() {
        Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL);

        /**
         * Create map and fill it in with deep path multi write operations list
         */
        HashMap<String, Object> removeListData = new HashMap<String, Object>();

        /* Remove the ShoppingLists from both user lists and active lists */
        Utils.updateMapForAllWithValue(mSharedWith, mListId, mListOwner, removeListData, "", null);

        /* Remove the associated list items */
        removeListData.put("/" + Constants.FIREBASE_LOCATION_SHOPPING_LIST_ITEMS + "/" + mListId,
                null);

        removeListData.put("/" + Constants.FIREBASE_LOCATION_OWNER_MAPPINGS + "/" + mListId,
                null);
        /* Do a deep-path update */
        firebaseRef.updateChildren(removeListData, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {

                if (firebaseError != null) {
                    Log.e(LOG_TAG, getString(R.string.log_error_updating_data) + firebaseError.getMessage());
                }
            }
        });
    }

}
