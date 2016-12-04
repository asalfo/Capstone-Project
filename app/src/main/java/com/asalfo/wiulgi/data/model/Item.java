package com.asalfo.wiulgi.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;


public class Item  extends Model implements Parcelable {

    @SerializedName("id")
    private String mId;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("slug")
    private String mSlug;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("color")
    private String mColor;
    @SerializedName("brand")
    private String mBrand;
    @SerializedName("model")
    private String mModel;
    @SerializedName("size")
    private String mSize;
    @SerializedName("location")
    private LatLng mLocation;
    @SerializedName("price")
    private float mPrice;
    @SerializedName("thumbnail")
    private String mThumbnail;
    @SerializedName("vote_count")
    private int mVoteCount;

    @SerializedName("vote_average")
    private float mVoteAverage;



    public Item(){}

    public Item(String id, String title, String slug, String description,
                String color, String brand, String model, String size,
                LatLng location, float price, String thumbnail) {
        this.mId = id;
        this.mTitle = title;
        this.mSlug = slug;
        this.mDescription = description;
        this.mColor = color;
        this.mBrand = brand;
        this.mModel = model;
        this.mSize = size;
        this.mLocation = location;
        this.mPrice = price;
        this.mThumbnail = thumbnail;
    }




    public static final Parcelable.Creator<Item> CREATOR
            = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    private Item(Parcel in) {

        this.mId = in.readString();
        this.mTitle = in.readString();
        this.mSlug = in.readString();
        this.mDescription = in.readString();
        this.mColor = in.readString();
        this.mBrand = in.readString();
        this.mModel = in.readString();
        this.mSize = in.readString();
        this.mPrice = in.readFloat();
        this.mThumbnail = in.readString();
        this.mVoteAverage = in.readFloat();
        this.mVoteCount = in.readInt();


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        long time;

        dest.writeString(mId);
        dest.writeString(mTitle);
        dest.writeString(mSlug);
        dest.writeString(mDescription);
        dest.writeString(mColor);
        dest.writeString(mBrand);
        dest.writeString(mModel);
        dest.writeString(mSize);
        dest.writeFloat(mPrice);
        dest.writeString(mThumbnail);
        dest.writeFloat(mVoteAverage);
        dest.writeInt(mVoteCount);


    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getSlug() {
        return mSlug;
    }

    public void setSlug(String slug) {
        this.mSlug = slug;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        this.mColor = color;
    }

    public String getBrand() {
        return mBrand;
    }

    public void setBrand(String brand) {
        this.mBrand = brand;
    }

    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        this.mModel = model;
    }

    public String getSize() {
        return mSize;
    }

    public void setSize(String size) {
        this.mSize = size;
    }

    public LatLng getLocation() {
        return mLocation;
    }

    public void setLocation(LatLng location) {
        this.mLocation = location;
    }

    public float getPrice() {
        return mPrice;
    }

    public void setPrice(float price) {
        this.mPrice = price;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.mThumbnail = thumbnail;
    }


    public float getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.mVoteAverage = voteAverage;
    }

    public int getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(int voteCount) {
        this.mVoteCount = voteCount;
    }
}