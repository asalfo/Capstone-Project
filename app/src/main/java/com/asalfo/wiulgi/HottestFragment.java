package com.asalfo.wiulgi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;


import com.asalfo.wiulgi.data.provider.ItemLoader;
import com.asalfo.wiulgi.data.provider.WiulgiContract;
import com.asalfo.wiulgi.service.UtilityService;
import com.asalfo.wiulgi.sync.WiulgiSyncAdapter;
import com.asalfo.wiulgi.ui.ItemAdapter;
import com.asalfo.wiulgi.ui.RecyclerViewItemClickListener;
import com.asalfo.wiulgi.ui.WiugliRecyclerView;
import com.asalfo.wiulgi.util.Utils;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HottestFragment extends BaseFragment {



    @BindView(R.id.recycler_view)
    WiugliRecyclerView mRecyclerView;
    @BindView(R.id.recyclerview_item_empty)
    TextView mEmptyView;
    private ItemAdapter mAdapter;
    private OnFragmentInteractionListener mListener;


    private boolean mItemClicked;
    private LatLng mLatestLocation;
    private static final int ITEM_LOADER = 0;


    public HottestFragment() {

    }


    public static HottestFragment newInstance() {
        HottestFragment fragment = new HottestFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_hottest, container, false);
        ButterKnife.bind(this, view);
        if (mRecyclerView != null) {

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {

                    if(position <= 3) return 1;
                    if (position % 4 == 0) {
                        return 2;
                    } else {
                        return 1;
                    }
                }
            });
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.setEmptyView(view.findViewById(android.R.id.empty));
            RecyclerView.ItemDecoration itemDecoration =
                    new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
            mRecyclerView.addItemDecoration(itemDecoration);
            mRecyclerView.setHasFixedSize(true);


            getLoaderManager().initLoader(ITEM_LOADER, null, this);

            mAdapter = new ItemAdapter(getActivity(), new ItemAdapter.ItemAdapterOnClickHandler() {
                @Override
                public void onClick(Long itemId, ItemAdapter.ViewHolder vh) {
                    onItemSelected(itemId,vh);
                }
            },mEmptyView);
   /*         mRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(),
                    new RecyclerViewItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            long itemId = mAdapter.getItemId(position);
                            onItemSelected(itemId);
                        }
                    }));*/
            mRecyclerView.setAdapter(mAdapter);
        }

       return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(ITEM_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        mItemClicked = false;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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




    /*
            Updates the empty list view with contextually relevant information that the user can
            use to determine why they aren't seeing weather.
         */
    private void updateEmptyView() {
        if ( mAdapter.getItemCount() == 0 ) {

            if ( null != mEmptyView ) {
                // if cursor is empty, why? do we have an invalid location
                int message = R.string.empty_list;
                @WiulgiSyncAdapter.LocationStatus int location = Utils.getLocationStatus(getActivity());
                switch (location) {
                    case WiulgiSyncAdapter.STATUS_SERVER_DOWN:
                        message = R.string.empty_item_list_server_down;
                        break;
                    case WiulgiSyncAdapter.STATUS_SERVER_INVALID:
                        message = R.string.empty_item_list_server_error;
                        break;
                    case WiulgiSyncAdapter.STATUS_INVALID:
                        message = R.string.empty_item_list_invalid_location;
                        break;
                    default:
                        if (!Utils.isNetworkAvailable(getActivity())) {
                            message = R.string.empty_item_list_no_network;
                        }
                }
                mEmptyView.setText(message);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return ItemLoader.newAllItemsInstance(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public void onItemSelected(long itemId,ItemAdapter.ViewHolder vh) {


        if (!mItemClicked) {
            mItemClicked = true;
            Uri contentUri = WiulgiContract.Items.buildItemUri(itemId);
            mListener.onFragmentInteraction(contentUri,ItemDetailActivity.class,vh);
        }
    }



}
