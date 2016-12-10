package com.asalfo.wiulgi.event;

import android.support.v7.graphics.Palette;

/**
 * Created by asalfo on 06/12/2016.
 */

public class PaletteEvent {
    private final Palette palette;

    public PaletteEvent(Palette palette) {
        this.palette = palette;
    }
    public Palette getPalette(){
        return palette;
    }
}
