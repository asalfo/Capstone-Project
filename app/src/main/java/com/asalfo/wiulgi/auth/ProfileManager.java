package com.asalfo.wiulgi.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.asalfo.wiulgi.util.Settings;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProfileManager {

    private List<ProfileListener> mListeners;
    private SharedPreferences mSharedPreferences;
    @Nullable
    private User mUser;

    private ProfileManager() {
        if (this.mListeners == null) {
            synchronized (ProfileManager.class) {
                if (this.mListeners == null) {
                    this.mListeners = new CopyOnWriteArrayList();
                }
            }
        }
    }

    @NonNull
    public static ProfileManager getInstance() {
        return ProfileManagerHolder.instance;
    }

    public void init(@NonNull Context context) {
        this.mSharedPreferences = context.getSharedPreferences("com.asalfo.wiulgi.user", Context.MODE_PRIVATE);
        synchronized (ProfileManager.class) {
            if (isLoggedIn()) {
                if (this.mSharedPreferences.contains("user")) {
                    init();
                } else {
                    loadCurrentUser(context);
                }
            }
        }
    }

    public void registerListener(@NonNull ProfileListener listener) {
        synchronized (ProfileManager.class) {
            if (!this.mListeners.contains(listener)) {
                this.mListeners.add(listener);
            }
            if (isLoggedIn()) {
                listener.onProfileLogIn();
            }
        }
    }

    public void unregisterListener(ProfileListener listener) {
        synchronized (ProfileManager.class) {
            this.mListeners.remove(listener);
        }
    }

    public boolean isLoggedIn() {
        return Settings.getInstance().hasApiToken();
    }

    private void loadCurrentUser(Context context) {

    }

    private void init() {
        synchronized (ProfileManager.class) {
            if (this.mUser == null) {
                String userJson = this.mSharedPreferences.getString("user", null);
                if (userJson != null) {
                    this.mUser = User.fromString(userJson);
                    dispatchOnProfileLogIn();
                }
            }
        }
    }

    @Nullable
    public User getUser() {
        return this.mUser;
    }

    public void setUser(User user) {
        synchronized (ProfileManager.class) {
            this.mUser = user;
            save();
        }
    }

    public void logIn(@NonNull User user) {
        Settings.getInstance().setUserApiToken(user.getAuthToken());

            synchronized (ProfileManager.class) {
                ProfileManager.this.mUser = user;
                ProfileManager.this.save();
            }
            ProfileManager.this.dispatchOnProfileLogIn();

    }

    public void logOut() {
        synchronized (ProfileManager.class) {
            Settings settings = Settings.getInstance();
            settings.setUserApiToken(null);
            settings.setUserAuthToken(null);
            settings.setUserId(null);
            this.mUser = null;
            save();
        }
        dispatchOnProfileLogOut();
    }

    private void save() {
        if (this.mUser != null) {
            this.mSharedPreferences.edit().putString("user", this.mUser.toString()).apply();
            return;
        }
        this.mSharedPreferences.edit().remove("user").apply();
    }

    private void dispatchOnProfileLogIn() {
        synchronized (ProfileManager.class) {
            for (ProfileListener listener : this.mListeners) {
                listener.onProfileLogIn();
            }
        }
    }

    private void dispatchOnProfileLogOut() {
        synchronized (ProfileManager.class) {
            for (ProfileListener listener : this.mListeners) {
                listener.onProfileLogOut();
            }
        }
    }


    public interface ProfileListener {
        void onProfileLogIn();

        void onProfileLogOut();
    }

    private static class ProfileManagerHolder {
        private static final ProfileManager instance = new ProfileManager();
    }
}
