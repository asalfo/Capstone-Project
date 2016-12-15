package com.asalfo.wiulgi;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import android.view.View;

import com.asalfo.wiulgi.auth.User;
import com.asalfo.wiulgi.http.WiulgiApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity  implements
        WiulgiApi.OnApiResponseListener ,
        ProfileActivityFragment.OnFragmentInteractionListener {

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    private WiulgiApi mApi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mApi = new WiulgiApi(this,this);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, new ProfileActivityFragment())
                    .commit();

        }
    }



    @Override
    public void onApiRequestFailure(int statusCode, String message) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Sorry your can't be updated.", Snackbar.LENGTH_LONG);

        snackbar.show();

    }

    @Override
    public void onApiRequestFinish() {

    }

    @Override
    public void onApiRequestStart() {

    }

    @Override
    public void onApiRequestSuccess(int i, Response response) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Profile updeted successfully.", Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    @Override
    public void onProfileChanged(User user) {
        mApi.updateUser(user);
    }


}
