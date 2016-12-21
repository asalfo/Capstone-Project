package com.asalfo.wiulgi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asalfo.wiulgi.auth.ProfileManager;
import com.asalfo.wiulgi.auth.User;
import com.asalfo.wiulgi.data.model.Item;
import com.asalfo.wiulgi.data.model.Rating;
import com.asalfo.wiulgi.data.provider.ItemLoader;
import com.asalfo.wiulgi.data.provider.WiulgiContract;
import com.asalfo.wiulgi.data.provider.WiulgiContract.ItemsColumns;
import com.asalfo.wiulgi.event.PaletteEvent;
import com.asalfo.wiulgi.http.WiulgiApi;
import com.asalfo.wiulgi.service.DatabaseUpdateTask;
import com.asalfo.wiulgi.ui.WiulgiExpandableTextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class ItemDetailActivityFragment extends BaseFragment implements
        RatingBar.OnRatingBarChangeListener,
        WiulgiApi.OnApiResponseListener {

    public static final String LOG_TAG = ItemDetailActivityFragment.class.getSimpleName();
    public static final String EXTRA_ITEM = "itemUri";
    private final static int DETAIL_LOADER = 0;
    private final static int DETAIL_LOADER_MONGOID = 1;
    public Item mItem;
    @Nullable
    @BindView(R.id.content_item_detail)
    RelativeLayout mContentItemDetail;
    @Nullable
    @BindView(R.id.itemDescription)
    WiulgiExpandableTextView mDescriptionTextView;
    @Nullable
    @BindView(R.id.itemBrand)
    TextView mBrand;
    @Nullable
    @BindView(R.id.itemModel)
    TextView mModel;
    @Nullable
    @BindView(R.id.itemColor)
    TextView mColor;
    @Nullable
    @BindView(R.id.itemSize)
    TextView mSize;
    @Nullable
    @BindView(R.id.ratingBar)
    RatingBar mRatingBar;
    WiulgiApi mApi;
    private GoogleMap mGoogleMap;
    private String mItemTitle;
    @Nullable
    private Uri mUri;
    @Nullable
    private ItemDetailActivityFragment.OnFragmentInteractionListener mListener;
    private boolean mLoadByMongoId;

    private String mItemMongoId;
    private long mItemId;
    private double mLatitude, mLongitud;


    public ItemDetailActivityFragment() {
    }

    @NonNull
    public static ItemDetailActivityFragment createInstance(Uri contentUri, boolean hasMongoId) {
        ItemDetailActivityFragment detailFragment = new ItemDetailActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ITEM, contentUri);
        bundle.putBoolean("mongo_id", hasMongoId);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_detail, container, false);
        ButterKnife.bind(this, view);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(EXTRA_ITEM);
            mLoadByMongoId = arguments.getBoolean("mongo_id", false);
            Log.d(LOG_TAG, "onCreateView " + mUri);
        } else {
            getActivity().finish();
            return null;
        }

        if (ProfileManager.getInstance().isLoggedIn()) {
            mRatingBar.setIsIndicator(false);
        }

        mRatingBar.setOnRatingBarChangeListener(this);


        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(mLoadByMongoId ?
                DETAIL_LOADER_MONGOID : DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

        mApi = new WiulgiApi(getActivity(), this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemDetailActivityFragment.OnFragmentInteractionListener) {
            mListener = (ItemDetailActivityFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void applyPalette(@NonNull PaletteEvent event) {
        Palette palette = event.getPalette();


        int titleColor;
        if (palette.getLightVibrantSwatch() != null) {

            titleColor = palette.getLightVibrantSwatch().getTitleTextColor();
        } else {
            titleColor = palette.getMutedSwatch().getTitleTextColor();
        }

        //mContentItemDetail.setBackgroundColor(textSwatch.getRgb());
        mDescriptionTextView.setTextColor(titleColor);
        mBrand.setTextColor(titleColor);
        mModel.setTextColor(titleColor);
        mSize.setTextColor(titleColor);
        mColor.setTextColor(titleColor);


    }

    @Nullable
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader " + mUri);
        if (null != mUri) {
            Long itemId = WiulgiContract.Items.getItemId(mUri);

            return ItemLoader.newInstanceForItemId(getActivity(), itemId);
        }

        return null;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, @NonNull Cursor cursor) {
        Log.d(LOG_TAG, "onLoadFinished " + cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {

            mItem = new Item(cursor);

            mItemTitle = cursor.getString(cursor.getColumnIndex(ItemsColumns.TITLE));

            mItemId = cursor.getLong(cursor.getColumnIndex(ItemsColumns._ID));

            mDescriptionTextView.setText(mItem.getDescription());
            mBrand.setText(mItem.getBrand());
            mModel.setText(mItem.getModel());
            mSize.setText(mItem.getSize());
            mColor.setText(mItem.getColor());
            mRatingBar.setRating(mItem.getVoteAverage());

            if (mItem.getLocation() != null) {
                ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        Log.d(LOG_TAG, "MapReady");
                        mGoogleMap = googleMap;

                        MarkerOptions place = new MarkerOptions().position(mItem.getLocation())
                                .title("Store");
                        mGoogleMap.addMarker(place);

                        CameraPosition target = new CameraPosition.Builder()
                                .target(mItem.getLocation())
                                .zoom(14)
                                .build();
                        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));

                    }

                });
            }

            mListener.onFragmentInteraction(mItem);
            sendScreenNameToGAnalytics(mItemTitle);

        }

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mUri = null;
    }


    @Override
    public void onRatingChanged(@NonNull RatingBar ratingBar, float rating, boolean fromUser) {

        if (fromUser) {
            User user = ProfileManager.getInstance().getUser();
            if (null != user) {
                Rating rat = new Rating();
                rat.setItemId(mItem.getMongoId());
                rat.setUserId(user.getId());
                rat.setPreference(ratingBar.getRating());
                mApi.rate(rat);
            }
        }

    }


    @Override
    public void onApiRequestFailure(int statusCode, String message) {
        Log.d(LOG_TAG, message);
        mRatingBar.setRating(mItem.getVoteAverage());
        mRatingBar.refreshDrawableState();
    }

    @Override
    public void onApiRequestFinish() {

    }

    @Override
    public void onApiRequestStart() {

    }

    @Override
    public void onApiRequestSuccess(int i, @NonNull Response response) {
        Log.d(LOG_TAG, response.body().toString());
        try {
            JSONObject jsonObj = new JSONObject(response.body().toString());
            final JSONObject item = jsonObj.getJSONObject("itemi");
            ContentValues cv = new ContentValues();
            cv.put(WiulgiContract.Items.VOTE_AVERAGE, item.getString("vote_average"));
            cv.put(WiulgiContract.Items.VOTE_COUNT, item.getString("vote_count"));

            new DatabaseUpdateTask(getActivity(), cv, new DatabaseUpdateTask.AsyncCallback() {
                @Override
                public void onCallback() {

                    try {
                        mRatingBar.setRating(Float.valueOf(item.getString("vote_average")));
                        mRatingBar.refreshDrawableState();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).execute(mItem.getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
