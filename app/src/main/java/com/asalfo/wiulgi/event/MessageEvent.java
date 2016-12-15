package com.asalfo.wiulgi.event;


import com.asalfo.wiulgi.util.Constants;

/**
 * Created by asalfo on 25/11/2016.
 */

public class MessageEvent {
    private final String message;
    private final int type;

    public MessageEvent(String message) {
        this.message = message;
        this.type = Constants.ONLY_MESSAGE;
    }

    public MessageEvent(String message, int type) {
        this.message = message;
        this.type = type;
    }

    public int getType() {
        return type;
    }


    public String getMessage() {
        return message;
    }
}
