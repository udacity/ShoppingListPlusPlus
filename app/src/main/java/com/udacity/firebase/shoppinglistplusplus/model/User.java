package com.udacity.firebase.shoppinglistplusplus.model;

import java.util.HashMap;

/**
 * Defines the data structure for User objects.
 */
public class User {
    // TODO Add a boolean to keep track of whether the user has logged in
    private String name;
    private String email;
    private HashMap<String, Object> timestampJoined;

    /**
     * Required public constructor
     */
    public User() {
    }

    /**
     * Use this constructor to create new User.
     * Takes user name, email and timestampJoined as params
     *
     * @param name
     * @param email
     * @param timestampJoined
     */
    public User(String name, String email, HashMap<String, Object> timestampJoined) {
        this.name = name;
        this.email = email;
        this.timestampJoined = timestampJoined;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }

}
