package com.asalfo.wiulgi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.asalfo.wiulgi.R;


import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class WelcomeFragment  extends Fragment {


    @BindView(R.id.sign_up)
    Button mSignUp;
    @BindView(R.id.sign_in_link)
    TextView mSignIn;

    public WelcomeFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.welcome_fragment, container, false);
        ButterKnife.bind(this, view);


        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), SignUpActivity.class),MainActivity.REQUEST_SIGNUP);
            }
        });


        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), SignInActivity.class),MainActivity.REQUEST_SIGNIN);
            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            Intent intent = getActivity().getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            getActivity().finish();
            startActivity(intent);
        }
    }
}
