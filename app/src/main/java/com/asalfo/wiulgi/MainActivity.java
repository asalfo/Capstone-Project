package com.asalfo.wiulgi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.asalfo.wiulgi.auth.ProfileManager;
import com.asalfo.wiulgi.service.UtilityService;
import com.asalfo.wiulgi.sync.WiulgiSyncAdapter;
import com.asalfo.wiulgi.ui.ItemAdapter;
import com.asalfo.wiulgi.util.Utils;
import com.google.android.gms.wearable.internal.StorageInfoResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements

        NavigationView.OnNavigationItemSelectedListener,
        HottestFragment.OnFragmentInteractionListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    public static final int REQUEST_SIGNIN = 1;
    public static final int REQUEST_PROFILE = 1;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;




    private CharSequence mDrawerTitle;
    private CharSequence mTitle;


    private static final int PERMISSION_REQ = 0;



    private final ProfileManager.ProfileListener mProfileListener = new ProfileManager.ProfileListener() {
        public void onProfileLogIn() {

            mNavigationView.getMenu().getItem(4).getSubMenu().getItem(1).setVisible(false);

            TextView mUserEmail = (TextView) mNavigationView.getHeaderView(0)
                    .findViewById(R.id.user_email_address);

            TextView mUsername = (TextView) mNavigationView.getHeaderView(0)
                    .findViewById(R.id.profile_username);

            ImageView avatarView = (ImageView) mNavigationView.getHeaderView(0)
                    .findViewById(R.id.avatarView);

            LinearLayout profile_info = (LinearLayout) mNavigationView.getHeaderView(0)
                    .findViewById(R.id.profile_info);

            mUserEmail.setText(ProfileManager.getInstance().getUser().getEmail());
            String username =ProfileManager.getInstance().getUser().getUsername();
            mUsername.setText(username);

            TextDrawable avatar = Utils.createTextDrawable(username);

            avatarView.setImageDrawable(avatar);

            profile_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent profile = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(profile);
                }
            });
            profile_info.setVisibility(View.VISIBLE);
        }

        public void onProfileLogOut() {

            mNavigationView.getMenu().getItem(4).getSubMenu().getItem(1).setVisible(true);
            LinearLayout profile_info = (LinearLayout) mNavigationView.getHeaderView(0)
                    .findViewById(R.id.profile_info);
            profile_info.setVisibility(View.INVISIBLE);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mTitle = mDrawerTitle = getTitle();



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);



        HottestFragment hottestFragment = HottestFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, hottestFragment)
                .commit();

        WiulgiSyncAdapter.initializeSyncAdapter(this);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        if(!sharedPref.getBoolean(getString(R.string.fist_time),false)){
            WiulgiSyncAdapter.syncImmediately(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.fist_time), true);
            editor.apply();
        }

        setupLocationService(savedInstanceState);
         if(ProfileManager.getInstance().isLoggedIn()){
             mNavigationView.getHeaderView(0)
                     .findViewById(R.id.profile_info).setVisibility(View.VISIBLE);
         }else{
             mNavigationView.getHeaderView(0)
                     .findViewById(R.id.profile_info).setVisibility(View.INVISIBLE);
         }

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ProfileManager.getInstance().registerListener(this.mProfileListener);

    }

    protected void onDestroy() {
        ProfileManager.getInstance().unregisterListener(this.mProfileListener);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UtilityService.requestLocation(this);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_for_you) {
            setTitle(R.string.for_you_title);
            // Handle the camera action
        } else if (id == R.id.nav_wishlist) {
            setTitle(R.string.my_wishlist_title);

        } else if (id == R.id.nav_nearby) {
            setTitle(R.string.nearby_title);
        } else if (id == R.id.nav_hottest) {
            setTitle(R.string.hottest_title);
            HottestFragment hottestFragment = HottestFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, hottestFragment)
                    .commit();
        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_sign_in) {
            Intent login = new Intent(this, SignInActivity.class);
            startActivityForResult(login, REQUEST_SIGNIN);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri, Class<?> cls,ItemAdapter.ViewHolder vh) {


        Intent intent = new Intent(this, cls)
                .setData(uri);


        ActivityOptionsCompat activityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        new Pair<View, String>(vh.mThumbnail, getString(R.string.detail_thumnail_transition_name)));
        ActivityCompat.startActivity(this, intent, activityOptions.toBundle());

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    /**
     * Setup the location service
     * @param savedInstanceState
     */

    private void setupLocationService(Bundle savedInstanceState) {
        // Check fine location permission has been granted
        if (!Utils.checkFineLocationPermission(this)) {
            // See if user has denied permission in the past
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show a simple snackbar explaining the request instead
                showPermissionSnackbar();
            } else {
                // Otherwise request permission from user
                if (savedInstanceState == null) {
                    requestFineLocationPermission();
                }
            }
        } else {
            // Otherwise permission is granted (which is always the case on pre-M devices)
            fineLocationPermissionGranted();
        }
    }



    /**
     * Run when fine location permission has been granted
     */
    private void fineLocationPermissionGranted() {
        UtilityService.requestLocation(this);
    }

    /**
     * Permissions request result callback
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQ:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fineLocationPermissionGranted();
                }
        }
    }
    /**
     * Request the fine location permission from the user
     */
    private void requestFineLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQ);
    }
    /**
     * Show a permission explanation snackbar
     */
    private void showPermissionSnackbar() {
        Snackbar.make(
                findViewById(R.id.container), R.string.permission_explanation, Snackbar.LENGTH_LONG)
                .setAction(R.string.permission_explanation_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestFineLocationPermission();
                    }
                })
                .show();
    }

}
