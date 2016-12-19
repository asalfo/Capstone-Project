package com.asalfo.wiulgi;



import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;

import com.asalfo.wiulgi.data.model.Item;
import com.asalfo.wiulgi.ui.ItemAdapter;


public abstract class BaseFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>{




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
