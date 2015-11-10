package com.udacity.firebase.shoppinglistplusplus.model;

/**
 * Defines the data structure for ShoppingListItem objects.
 */
public class ShoppingListItem {
    private String itemName;
    private String owner;

    /**
     * Required public constructor
     */
    public ShoppingListItem() {
    }

    /**
     * Use this constructor to create new ShoppingListItem.
     *
     * @param itemName
     */
    public ShoppingListItem(String itemName) {
        this.itemName = itemName;
        /**
         * This is a default value until we can differentiate users.
         * Which will be soon, I promise.
         */
        // TODO Time to make good on my promise.
        // This value should change now that you have the owner.
        this.owner = "Anonymous Owner";
    }

    public String getItemName() { return itemName; }

    public String getOwner() {
        return owner;
    }

}
