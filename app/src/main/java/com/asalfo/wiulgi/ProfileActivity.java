package com.asalfo.wiulgi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.asalfo.wiulgi.auth.User;
import com.asalfo.wiulgi.http.WiulgiApi;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements
        WiulgiApi.OnApiResponseListener,
        ProfileActivityFragment.OnFragmentInteractionListener {

    @Nullable
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    private WiulgiApi mApi;
    private Tracker mTracker;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mApi = new WiulgiApi(this, this);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, new ProfileActivityFragment())
                    .commit();

        }
        // Obtain the shared Tracker instance.
        WApplication application = (WApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(getString(R.string.profile_activity_title));
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    @Override
    public void onApiRequestFailure(int statusCode, String message) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, R.string.error_updating_profile, Snackbar.LENGTH_LONG);

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
                .make(coordinatorLayout, R.string.profile_updated, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    @Override
    public void onProfileChanged(@NonNull User user) {
        mApi.updateUser(user);
    }


}
