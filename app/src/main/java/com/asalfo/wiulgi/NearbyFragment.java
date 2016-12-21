package com.asalfo.wiulgi;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asalfo.wiulgi.auth.ProfileManager;
import com.asalfo.wiulgi.data.provider.ItemLoader;
import com.asalfo.wiulgi.data.provider.WiulgiContract;
import com.asalfo.wiulgi.sync.WiulgiSyncAdapter;
import com.asalfo.wiulgi.ui.ItemAdapter;
import com.asalfo.wiulgi.ui.WiugliRecyclerView;
import com.asalfo.wiulgi.util.Utils;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NearbyFragment extends BaseFragment {



    @Nullable
    @BindView(R.id.recycler_view)
    WiugliRecyclerView mRecyclerView;
    @Nullable
    @BindView(R.id.recyclerview_item_empty)
    TextView mEmptyView;
    @Nullable
    private ItemAdapter mAdapter;
    @Nullable
    private OnFragmentInteractionListener mListener;


    private boolean mItemClicked;
    private LatLng mLatestLocation;
    private static final int ITEM_LOADER = 0;


    public NearbyFragment() {

    }


    public static NearbyFragment newInstance(String title) {
        if(!ProfileManager.getInstance().isLoggedIn())
            return null;
        NearbyFragment fragment = new NearbyFragment();
        Bundle args = new Bundle();
        args.putString(BaseFragment.ACTIVITY_TITLE,title);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_hottest, container, false);
        ButterKnife.bind(this, view);
        if (mRecyclerView != null) {
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
        sendScreenNameToGAnalytics();
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

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        ItemLoader loader = ItemLoader.newAllItemsInstance(getActivity());
     loader.setSelection(WiulgiContract.Items.WISHED+" = ?");
        loader.setSelectionArgs(new String[]{"1"});

        return loader;
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
