package com.asalfo.wiulgi.util;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.asalfo.wiulgi.WApplication;
import com.asalfo.wiulgi.data.model.Model;

public class Settings {

    private final Context mContext;
    @NonNull
    private final SettingsObject mSettings;


    private static class SettingsHolder {
        private static final Settings instance = new Settings();
    }



    private class SettingsObject extends Model {
        private String requestMessage;
        private String userApiToken;
        private String userAuthToken;
        private String userEmail;
        private Long userId;
        private Boolean firstSync;

        private SettingsObject() {
        }
    }


    @NonNull
    private static Settings instance = new Settings();

    @NonNull
    public static Settings getInstance() {
        return instance;
    }

    private Settings() {

        this.mContext = WApplication.getContext();
        String settingsString = Preferences.getString(this.mContext, "com.asalfo.wiugli.settings");
        if (TextUtils.isEmpty(settingsString)) {
            this.mSettings = new SettingsObject();
        } else {
            this.mSettings = (SettingsObject) Model.fromString(settingsString, SettingsObject.class);
        }
    }



    private void save() {
        Preferences.putString(this.mContext, "com.asalfo.wiugli.settings", this.mSettings.toString());
    }

    public String getUserEmail() {
        return this.mSettings.userEmail;
    }

    public Boolean isFirstSync(){ return this.mSettings.firstSync; }

    public boolean hasUserEmail() {
        return !TextUtils.isEmpty(this.mSettings.userEmail);
    }

    public String getUserApiToken() {
        return this.mSettings.userApiToken;
    }

    public boolean hasApiToken() {
        return this.mSettings.userApiToken != null;
    }

    public boolean hasFirstSync() {
        return this.mSettings.firstSync != null;
    }

    public String getUserAuthToken() {
        return this.mSettings.userAuthToken;
    }

    public Long getUserId() {
        return this.mSettings.userId;
    }

    public boolean hasUserId() {
        return this.mSettings.userId != null;
    }

    public String getRequestMessage() {
        return this.mSettings.requestMessage;
    }

    public void setUserAuthToken(String userAuthToken) {
        this.mSettings.userAuthToken = userAuthToken;
        save();
    }

    public boolean hasAuthToken() {
        return this.mSettings.userAuthToken != null;
    }

    public void setUserId(Long id) {
        this.mSettings.userId = id;
        save();
    }

    public void setRequestMessage(String message) {
        this.mSettings.requestMessage = message;
        save();
    }

    public void setUserEmail(String email){
        this.mSettings.userEmail = email;
        save();
    }

    public void setUserApiToken(String userApiToken) {
        this.mSettings.userApiToken = userApiToken;
        save();
    }

    public void setFirstSync(Boolean sync){
        this.mSettings.firstSync = sync;
        save();
    }

}
