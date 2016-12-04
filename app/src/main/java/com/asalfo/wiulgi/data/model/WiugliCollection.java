package com.asalfo.wiulgi.data.model;


import android.util.ArrayMap;

import java.util.ArrayList;


public class WiugliCollection <E> {

        private ArrayList<E> items;
        private int total;
        private int count;
        private ArrayMap<String,String> _links;

        public WiugliCollection(ArrayList<E> items, int total, int count, ArrayMap<String,String> _links) {
            this.items = items;
            this.total = total;
            this.count = count;
            this._links = _links;

        }


    public ArrayList<E> getItems() {
        return items;
    }

    public void setItems(ArrayList<E> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayMap<String, String> get_links() {
        return _links;
    }

    public void set_links(ArrayMap<String, String> _links) {
        this._links = _links;
    }
}

