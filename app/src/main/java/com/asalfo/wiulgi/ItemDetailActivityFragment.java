package com.asalfo.wiulgi;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asalfo.wiulgi.data.model.Item;
import com.asalfo.wiulgi.data.provider.ItemLoader;
import com.asalfo.wiulgi.data.provider.WiulgiContract;
import com.asalfo.wiulgi.data.provider.WiulgiContract.ItemsColumns;
import com.asalfo.wiulgi.event.PaletteEvent;
import com.asalfo.wiulgi.ui.WiulgiExpandableTextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class ItemDetailActivityFragment extends Fragment  implements
        RatingBar.OnRatingBarChangeListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = ItemDetailActivityFragment.class.getSimpleName();
    public static final String EXTRA_ITEM = "itemUri";

    @BindView(R.id.content_item_detail)
    RelativeLayout mContentItemDetail;
    @BindView(R.id.itemDescription)
    WiulgiExpandableTextView mDescriptionTextView;
    @BindView(R.id.itemBrand)
    TextView mBrand;
    @BindView(R.id.itemModel)
    TextView mModel;
    @BindView(R.id.itemColor)
    TextView mColor;
    @BindView(R.id.itemSize)
    TextView mSize;
    @BindView(R.id.ratingBar)
    RatingBar mRatingBar;





    private GoogleMap mGoogleMap;

    private String mItemTitle;
    private Uri mUri;
    private ItemDetailActivityFragment.OnFragmentInteractionListener mListener;
    private final static int DETAIL_LOADER = 0;
    private final static int DETAIL_LOADER_MONGOID = 1;
    private  boolean    mLoadByMongoId;

    private String mItemMongoId;
    private long mItemId;
    private  double mLatitude,mLongitud;



    public static ItemDetailActivityFragment createInstance(Uri contentUri,boolean hasMongoId) {
        ItemDetailActivityFragment detailFragment = new ItemDetailActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ITEM, contentUri);
        bundle.putBoolean("mongo_id",hasMongoId);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    public ItemDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_detail, container, false);
        ButterKnife.bind(this,view);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(EXTRA_ITEM);
            mLoadByMongoId = arguments.getBoolean("mongo_id",false);
            Log.d(LOG_TAG, "onCreateView " + mUri);
        }else{
            getActivity().finish();
            return null;
        }

        mRatingBar.setIsIndicator(false);
        mRatingBar.setOnRatingBarChangeListener(this);


        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader( mLoadByMongoId ?
                DETAIL_LOADER_MONGOID: DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);


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
    public void applyPalette(PaletteEvent event) {
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader " + mUri);
        if (null != mUri) {
            Long itemId = WiulgiContract.Items.getItemId(mUri);

            return ItemLoader.newInstanceForItemId(getActivity(), itemId);
        }

        return  null;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "onLoadFinished " + cursor.getCount());
        if (cursor != null && cursor.moveToFirst()) {



            final Item item = new Item(cursor);


            mListener.onFragmentLoad(item);

            mItemTitle = cursor.getString(cursor.getColumnIndex(ItemsColumns.TITLE));

            mItemId = cursor.getLong(cursor.getColumnIndex(ItemsColumns._ID));

            mDescriptionTextView.setText(item.getDescription());
            mBrand.setText(item.getBrand());
            mModel.setText(item.getModel());
            mSize.setText(item.getSize());
            mColor.setText(item.getColor());
            mRatingBar.setRating(item.getVoteAverage());


            ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    Log.d(LOG_TAG,"MapReady");
                    mGoogleMap = googleMap;

                    MarkerOptions place = new MarkerOptions().position(item.getLocation())
                            .title("Store");
                    mGoogleMap.addMarker(place);

                    CameraPosition target = new CameraPosition.Builder()
                            .target(item.getLocation())
                            .zoom(14)
                            .build();
                    mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));

                }

            });
        }

    }




    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mUri = null;
    }


    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void onFragmentLoad(Item item);
    }
}
