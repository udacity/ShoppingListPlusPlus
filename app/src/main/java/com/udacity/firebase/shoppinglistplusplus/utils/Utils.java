package com.udacity.firebase.shoppinglistplusplus.utils;

import android.content.Context;

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

    // TODO You can put the encodeEmail method here. This code is important because
    // Firebase does not allow for keys to have "." in them. As a work around we
    // can convert "." in emails to ",".
}
