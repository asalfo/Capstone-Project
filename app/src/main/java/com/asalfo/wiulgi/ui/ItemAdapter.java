package com.asalfo.wiulgi.ui;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import com.asalfo.wiulgi.data.provider.ItemsContract.ItemsColumns;


public class ItemAdapter extends CursorRecyclerViewAdapter<ItemAdapter.ViewHolder> {

    private final String LOG_TAG = ItemAdapter.class.getSimpleName();
    private static Context mContext;
    private static Typeface robotoLight;
    private int mImageSize;
    private LatLng mLatestLocation;

    public ItemAdapter(Context context){
        super(context, null);
        mContext = context;
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
    protected void onBindViewHolder(final ItemAdapter.ViewHolder viewHolder, Cursor cursor) {
        final ImageView imageView = viewHolder.mImageView;
        viewHolder.mTitleTextView.setText(cursor.getString(cursor.getColumnIndex(ItemsColumns.TITLE)));
        viewHolder.mBrandTextView.setText(cursor.getString(cursor.getColumnIndex(ItemsColumns.BRAND)));
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        viewHolder.mPriceTextView.setText(currency.format(Double.valueOf(cursor.getString(cursor.getColumnIndex(ItemsColumns.PRICE)))));

        Picasso.with(mContext)
                .load(cursor.getString(cursor.getColumnIndex(ItemsColumns.THUMBNAIL)))
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
                                        Palette.Swatch textSwatch = palette.getVibrantSwatch();

                                        if (textSwatch == null) {
                                            return;
                                        }

                                        viewHolder.mCardView.setBackgroundColor(palette.getVibrantColor(0xFF333333));
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

    public static class ViewHolder extends RecyclerView.ViewHolder
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
        ImageView mImageView;
        @BindView(R.id.count_average)
        ImageView mVoteImageView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }



        @Override
        public void onClick(View v) {

        }
    }

}



