package com.udacity.firebase.shoppinglistplusplus.model;

/**
 * Defines the data structure for both Active and Archived ShoppingList objects.
 */
public class ShoppingList {
    private String listName;
    private String owner;

    /**
     * Required public constructor
     */
    public ShoppingList() {
    }

    /**
     * Use this constructor to create new ShoppingLists.
     *
     * @param name
     * @param owner
     *
     */
    public ShoppingList(String name, String owner) {
        this.listName = name;
        this.owner = owner;
    }

    public String getListName() {
        return listName;
    }

    public String getOwner() {
        return owner;
    }

}
