package com.asalfo.wiulgi;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.asalfo.wiulgi.data.provider.ItemLoader;
import com.asalfo.wiulgi.ui.ItemAdapter;
import com.asalfo.wiulgi.ui.RecyclerViewItemClickListener;
import com.asalfo.wiulgi.ui.WiugliRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HottestFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor> {



    @BindView(R.id.recycler_view)
    WiugliRecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private OnFragmentInteractionListener mListener;

    private boolean mItemClicked;

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
            mRecyclerView.setEmptyView(view.findViewById(android.R.id.empty));
            RecyclerView.ItemDecoration itemDecoration =
                    new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
            mRecyclerView.addItemDecoration(itemDecoration);
            mRecyclerView.setHasFixedSize(true);


            getLoaderManager().initLoader(ITEM_LOADER, null, this);

            mAdapter = new ItemAdapter(getActivity());
            mRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(),
                    new RecyclerViewItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            long itemId = mAdapter.getItemId(position);
                            onItemSelected(itemId);
                        }
                    }));
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

    public void onItemSelected(long itemId) {


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
    }
}
