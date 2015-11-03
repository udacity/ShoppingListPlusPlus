package com.udacity.firebase.shoppinglistplusplus.utils;

import android.content.Context;

import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;

import java.text.SimpleDateFormat;

/**
 * Utility class
 */
public class Utils {
    /**
     * Format the timestamp with SimpleDateFormat
     */
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private Context mContext = null;


    /**
     * Public constructor that takes mContext for later use
     */
    public Utils(Context con) {
        mContext = con;
    }

    /**
     * Return true if currentUserEmail equals to shoppingList.owner()
     * Return false otherwise
     */
    public static boolean checkIfOwner(ShoppingList shoppingList, String currentUserEmail) {
        return (shoppingList.getOwner() != null &&
                shoppingList.getOwner().equals(currentUserEmail));
    }

    /**
     * Encode user email to use it as a Firebase key (Firebase does not allow "." in the key name)
     * Encoded email is also used as "userEmail", list and item "owner" value
     */
    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    // TODO You will end up needing a lot of the similar code to update all copies of the list.
    // Because of that, I suggest writing two methods here.

    // TODO The first is updateMapForAllWithValue.
    // It will take a Hash Map of String to Objects (the kind you pass to updateChildren), a
    // property in the shopping list you want to change, the value you want to change it to and
    // a few other values (it's up to you to decide what you need to pass in).
    // With these values, it should add entries to the HashMap for changing all ShoppingList copies
    // to have the updated property/value passed in. For now this will only be one list because
    // we haven't implemented sharing yet. Eventually we will implement sharing and you will
    // need to add the code to update the lists stored under each shared user.

    // TODO the second method is updateMapWithTimestampLastChanged.
    // This method will similarly take in a HashMap of String and Objects. It will then set all copies
    // of the desired shopping list to have their TimestampLastChanged updated with the
    // ServerValue.TIMESTAMP value. As with above, right now this is only one list.



}
