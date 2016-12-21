package com.asalfo.wiulgi;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;

import com.asalfo.wiulgi.data.model.Item;
import com.asalfo.wiulgi.ui.ItemAdapter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public abstract class BaseFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {


    public final static String ACTIVITY_TITLE = "activity_title";
    @Nullable
    private String mTitle;
    /**
     * The {@link Tracker} used to record screen views.
     */
    private Tracker mTracker;


    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTitle = getArguments().getString(ACTIVITY_TITLE);
        }

        // Obtain the shared Tracker instance.
        WApplication application = (WApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }


    public Tracker getTracker() {
        return mTracker;
    }

    public void sendScreenNameToGAnalytics(String title) {
        mTracker.setScreenName(title);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void sendScreenNameToGAnalytics() {
        mTracker.setScreenName(getTitle());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri, Class<?> cls, ItemAdapter.ViewHolder vh);

        void onFragmentInteraction(Item item);
    }
}
