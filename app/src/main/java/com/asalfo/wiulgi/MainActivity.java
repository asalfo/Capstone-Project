package com.asalfo.wiulgi;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.asalfo.wiulgi.sync.WiulgiSyncAdapter;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HottestFragment.OnFragmentInteractionListener {



    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle = mDrawerTitle = getTitle();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
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
                    .add(R.id.content_main, hottestFragment)
                    .commit();
        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_setting) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
