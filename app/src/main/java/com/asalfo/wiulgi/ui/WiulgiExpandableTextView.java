package com.asalfo.wiulgi.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

/**
 * Created by asalfo on 06/12/2016.
 */

public class WiulgiExpandableTextView extends ExpandableTextView {

    public WiulgiExpandableTextView(Context context) {
        super(context);
    }

    public WiulgiExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WiulgiExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public TextView getTextView(){
        return mTv;
    }

    public void setTextColor(int color){
        mTv.setTextColor(color);
    }
}
