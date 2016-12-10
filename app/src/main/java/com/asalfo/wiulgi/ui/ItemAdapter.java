package com.asalfo.wiulgi.ui;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asalfo.wiulgi.R;
import com.asalfo.wiulgi.util.Constants;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.asalfo.wiulgi.data.provider.WiulgiContract.ItemsColumns;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final String LOG_TAG = ItemAdapter.class.getSimpleName();
    private static Context mContext;
    private static Typeface robotoLight;
    private int mImageSize;
    final private ItemAdapterOnClickHandler mClickHandler;

    final private View mEmptyView;

    private Cursor mCursor;
    private LatLng mLatestLocation;



    public static interface ItemAdapterOnClickHandler {
        void onClick(Long id, ViewHolder vh);
    }

    public ItemAdapter (Context context, ItemAdapterOnClickHandler dh, View emptyView) {
        mContext = context;
        mClickHandler = dh;
        mEmptyView = emptyView;
        mImageSize = mContext.getResources().getDimensionPixelSize(R.dimen.image_size)
                * Constants.IMAGE_ANIM_MULTIPLIER;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ItemAdapter.ViewHolder viewHolder, int position) {
        mCursor.moveToPosition(position);
        final ImageView imageView = viewHolder.mThumbnail;
        viewHolder.mTitleTextView.setText(mCursor.getString(mCursor.getColumnIndex(ItemsColumns.TITLE)));
        viewHolder.mBrandTextView.setText(mCursor.getString(mCursor.getColumnIndex(ItemsColumns.BRAND)));
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        viewHolder.mPriceTextView.setText(currency.format(Double.valueOf(mCursor.getString(mCursor.getColumnIndex(ItemsColumns.PRICE)))));

        // this enables better animations. even if we lose state due to a device rotation,
        // the animator can use this to re-find the original view
        ViewCompat.setTransitionName(viewHolder.mThumbnail, "thumbnailView" + position);

        Picasso.with(mContext)
                .load(mCursor.getString(mCursor.getColumnIndex(ItemsColumns.THUMBNAIL)))
                .error(R.drawable.empty_photo)
                .placeholder(R.drawable.empty_photo)
                .resize(mImageSize, mImageSize)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        assert imageView != null;
                        imageView.setImageBitmap(bitmap);
                        Palette.from(bitmap)
                                .generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        Palette.Swatch textSwatch = palette.getDominantSwatch();

                                        if (textSwatch == null) {
                                            return;
                                        }

                                        viewHolder.mCardView.setBackgroundColor(textSwatch.getRgb());
                                        viewHolder.mTitleTextView.setTextColor(textSwatch.getTitleTextColor());
                                        viewHolder.mBrandTextView.setTextColor(textSwatch.getTitleTextColor());
                                    }
                                });
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });


    }



    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public Cursor getCursor() {
        return mCursor;
    }

    public void selectView(RecyclerView.ViewHolder viewHolder) {
        if ( viewHolder instanceof ViewHolder ) {
            ViewHolder vfh = (ViewHolder)viewHolder;
            vfh.onClick(vfh.itemView);
        }
    }





    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        @BindView(R.id.card_view)
        CardView mCardView;
        @BindView(R.id.title)
        TextView mTitleTextView;
        @BindView(R.id.price)
        TextView mPriceTextView;
        @BindView(R.id.brand)
        TextView mBrandTextView;
        @BindView(R.id.thumbnail)
        public ImageView mThumbnail;
        @BindView(R.id.count_average)
        ImageView mVoteImageView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);

        }



        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            mClickHandler.onClick(mCursor.getLong(mCursor.getColumnIndex(ItemsColumns._ID)), this);

        }
    }

}



