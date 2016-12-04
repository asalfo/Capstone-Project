package com.asalfo.wiulgi;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asalfo.wiulgi.R;


import butterknife.ButterKnife;

/**
 * Created by asalfo on 30/11/2016.
 */

public class WelcomeFragment  extends Fragment {

    public WelcomeFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.welcome_fragment, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

}
