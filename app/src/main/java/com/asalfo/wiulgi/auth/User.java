package com.asalfo.wiulgi.auth;

import android.os.Parcel;
import android.os.Parcelable;

import com.asalfo.wiulgi.data.model.Model;
import com.google.gson.annotations.SerializedName;


public class User extends Model implements Parcelable {


    @SerializedName("id")
    private String mId;
    @SerializedName("username")
    private String mUsername;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("plain_password")
    private String mPlainPassword;
    @SerializedName("first_name")
    private String mFirstName;
    @SerializedName("last_name")
    private String mLastName;
    @SerializedName("birth_date")
    private String mBirthDate;
    @SerializedName("gender")
    private String mGender;
    @SerializedName("country")
    private String mCountry;
    @SerializedName("avatar")
    private String mAvatar;
    @SerializedName("token")
    private String mAuthToken;
    @SerializedName("facebook_token")
    private String mFacebookToken;


    public User() {
    }

    public User(String username, String email, String plainPassword) {
        this.mUsername = username;
        this.mEmail = email;
        this.mPlainPassword = plainPassword;
    }



    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {

        this.mId = in.readString();
        this.mUsername = in.readString();
        this.mEmail = in.readString();
        this.mFirstName = in.readString();
        this.mLastName = in.readString();
        this.mBirthDate = in.readString();
        this.mGender = in.readString();
        this.mAvatar = in.readString();
        this.mCountry = in.readString();
        this.mAuthToken = in.readString();
        this.mFacebookToken = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        long time;

        dest.writeString(mId);
        dest.writeString(mUsername);
        dest.writeString(mEmail);
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mBirthDate);
        dest.writeString(mGender);
        dest.writeString(mAvatar);
        dest.writeString(mCountry);
        dest.writeString(mAuthToken);
        dest.writeString(mFacebookToken);

    }



    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getmCountry() {
        return mCountry;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        this.mAvatar = avatar;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstname) {
        this.mFirstName = firstname;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getBirthDate() {
        return mBirthDate;
    }

    public void setBirthDate(String birthDate) {
        this.mBirthDate = birthDate;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        this.mGender = gender;
    }

    public String getPlainPassword() {
        return mPlainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.mPlainPassword = plainPassword;
    }

    public String getAuthToken() {
        return mAuthToken;
    }

    public void setAuthToken(String token) {
        this.mAuthToken = token;
    }


    public static User fromString(String userString) {
        return (User) Model.fromString(userString, User.class);
    }

    public String getFacebookToken() {
        return mFacebookToken;
    }

    public void setFacebookToken(String mFacebookToken) {
        this.mFacebookToken = mFacebookToken;
    }
}