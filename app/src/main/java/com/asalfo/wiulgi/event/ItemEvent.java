package com.asalfo.wiulgi.event;

import com.asalfo.wiulgi.data.model.Item;
import com.asalfo.wiulgi.util.Constants;


public class ItemEvent {

    private final Item item;
    private final String message;
    private final int type;

    public ItemEvent(Item item, String message) {
        this.item = item;
        this.message = message;
        this.type = Constants.ONLY_MESSAGE;
    }

    public ItemEvent(Item item,String message, int type) {
        this.item = item;
        this.message = message;
        this.type = type;
    }

    public Item getItem(){ return item; }
    public int getType() {
        return type;
    }


    public String getMessage() {
        return message;
    }
}
