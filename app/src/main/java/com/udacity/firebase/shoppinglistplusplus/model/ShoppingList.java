package com.udacity.firebase.shoppinglistplusplus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.firebase.database.ServerValue;

/**
 * Created by vortana_say on 29/05/16.
 */
public class ShoppingList {
    private String listName;
    private String owner;
    private Long creationDate;

    public ShoppingList() {

    }

    public ShoppingList(String listName, String owner) {
        this.listName = listName;
        this.owner = owner;
    }

    public String getListName() {
        return listName;
    }

    public String getOwner() {
        return owner;
    }


    public java.util.Map<String, String> getCreationDate() {
        return ServerValue.TIMESTAMP;
    }

    @JsonIgnore
    public Long getCreationDateLong() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return listName + " by " + owner;
    }
}
