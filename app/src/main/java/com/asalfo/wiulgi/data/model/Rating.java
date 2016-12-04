package com.asalfo.wiulgi.data.model;


import com.google.gson.annotations.SerializedName;

public class Rating  extends Model {


    @SerializedName("user")
    private String mUserId;
    @SerializedName("item")
    private String mItemId;
    @SerializedName("itemi")
    private Item mItem;
    @SerializedName("preference")
    private float mPreference;


    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getItemId() {
        return mItemId;
    }

    public void setItemId(String itemId) {
        this.mItemId = itemId;
    }

    public float getPrefrerence() {
        return mPreference;
    }

    public void setPreference(float preference) {
        this.mPreference = preference;
    }

    public Item getItem() {
        return mItem;
    }

    public void setItem(Item item) {
        this.mItem = item;
    }
}
