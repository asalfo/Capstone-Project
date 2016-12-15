package com.asalfo.wiulgi.event;


import com.asalfo.wiulgi.auth.User;

/**
 * Created by asalfo on 26/11/2016.
 */

public class UserEvent {

    private final User user;

    public UserEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
