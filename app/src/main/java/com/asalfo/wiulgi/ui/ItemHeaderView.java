package com.asalfo.wiulgi.ui;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asalfo.wiulgi.R;
import com.asalfo.wiulgi.auth.ProfileManager;
import com.asalfo.wiulgi.data.model.Item;
import com.asalfo.wiulgi.data.provider.WiulgiContract;
import com.asalfo.wiulgi.event.EventCode;
import com.asalfo.wiulgi.event.ItemEvent;
import com.asalfo.wiulgi.event.MessageEvent;
import com.asalfo.wiulgi.service.DatabaseUpdateTask;
import com.asalfo.wiulgi.util.Utils;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemHeaderView extends RelativeLayout {


    @BindView(R.id.item_info_wrapper)
    LinearLayout mItemInfoWrapper;
    @BindView(R.id.item_actions)
    LinearLayout mItemActions;
    @BindView(R.id.item_thumbnail)
    ImageView mItemThumnail;
    @BindView(R.id.item_name)
    TextView mItemName;
    @BindView(R.id.item_price)
    TextView mItemPrice;
    @BindView(R.id.item_distance)
    TextView mItemDistance;
    @BindView(R.id.item_action_share)
    ImageView mItemActionShare;
    @BindView(R.id.item_action_wishlist)
    ImageView mItemActionWishlist;
    @BindView(R.id.item_action_like)
    ImageView mItemActionLike;

    Context mContext;
    private Item mItem;

    /**
     * Interface definition for a callback to be invoked when  the thumbnail  changes.
     */
    public interface OnThumbnailChangedListener {
        /**
         * Called when the ItemHeaderView 's thumbnail  url  has been changed.
         *
         * @param itemHeaderView the ItemHeaderView which url has changed
         * @param thumnailUrl    the url of the thumbnail
         */
        void onThumbnailChanged(ItemHeaderView itemHeaderView, String thumnailUrl);
    }

    private List<OnThumbnailChangedListener> mListeners;


    public ItemHeaderView(Context context) {
        super(context);
        mContext = context;
    }

    public ItemHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public ItemHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ItemHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        mItemActionWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContentValues cv = new ContentValues();
                cv.put(WiulgiContract.Items.WISHED, mItem.getWhised() ? 0 : 1);

                new DatabaseUpdateTask(mContext, cv, new DatabaseUpdateTask.AsyncCallback() {
                    @Override
                    public void onCallback() {
                        if (mItem.getWhised()) {
                            mItemActionWishlist.setImageResource(R.drawable.ic_remove_circle_outline);
                            EventBus.getDefault().post(new ItemEvent(mItem,
                                    String.format(mContext.getString(R.string.removed_from_wishlist),
                                            mItem.getTitle()),EventCode.EVENT_REMOVE_WISHLIST));

                        } else {
                            mItemActionWishlist.setImageResource(R.drawable.ic_add);

                            EventBus.getDefault().post(new ItemEvent(mItem, String.format(
                                    mContext.getString(R.string.added_to_wishlist),
                                    mItem.getTitle()),EventCode.EVENT_ADD_WISHLIST));
                        }
                    }
                }).execute(mItem.getId());
            }
        });


        mItemActionLike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ContentValues cv = new ContentValues();
                cv.put(WiulgiContract.Items.FAVORITED, mItem.getFavorited() ? 0 : 1);

                new DatabaseUpdateTask(mContext, cv, new DatabaseUpdateTask.AsyncCallback() {
                    @Override
                    public void onCallback() {
                        if (mItem.getFavorited()) {

                            EventBus.getDefault().post(new ItemEvent( mItem,
                                    String.format(mContext.getString(R.string.added_to_favorite),
                                            mItem.getTitle()), EventCode.EVENT_ADD_FAVORITE));

                            mItemActionLike.setImageResource(R.drawable.ic_favorite_border);

                        } else {

                            mItemActionLike.setImageResource(R.drawable.ic_favorite);

                            EventBus.getDefault().post(new ItemEvent(mItem,
                                    String.format(mContext.getString(R.string.removed_from_favorite),
                                            mItem.getTitle()),EventCode.EVENT_REMOVE_FAVORITE));
                        }
                    }
                }).execute(mItem.getId());
            }
        });
    }


    public void setItem(Item item) {

        mItem = item;

        setThumbnail(item.getThumbnail());
        mItemName.setText(item.getTitle());

        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        mItemPrice.setText(currency.format(item.getPrice()));

        setDistance(item.getLocation());

        setupAction(item);
    }


    public ImageView getItemThumbnail() {
        return mItemThumnail;
    }

    public ImageView getItemActionShare() {
        return mItemActionShare;
    }

    public ImageView getItemActionLike() {
        return mItemActionLike;
    }

    public ImageView getItemActionWishlist() {
        return mItemActionWishlist;
    }

    public CharSequence getItemName() {
        return mItemName.getText();
    }


    public void setupAction(final Item item) {

        final String shareTitle = item.getTitle();
        final String shareText = "Wiulgi#" + item.getTitle() + item.getDescription();
        mItemActionShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                sendIntent.setType("text/plain");
                mContext.startActivity(Intent.createChooser(sendIntent, shareTitle));
            }
        });

        if (ProfileManager.getInstance().isLoggedIn()) {

            if (mItem.getWhised()) {
                mItemActionWishlist.setImageResource(R.drawable.ic_remove_circle_outline);
            } else {
                mItemActionWishlist.setImageResource(R.drawable.ic_add);
            }

            if (mItem.getFavorited()) {
                mItemActionLike.setImageResource(R.drawable.ic_favorite);
            } else {
                mItemActionLike.setImageResource(R.drawable.ic_favorite_border);
            }

            mItemActionWishlist.setVisibility(VISIBLE);
            mItemActionLike.setVisibility(VISIBLE);

        } else {
            mItemActionWishlist.setVisibility(INVISIBLE);
            mItemActionLike.setVisibility(INVISIBLE);
        }
    }

    public void setDistance(LatLng location) {

        LatLng myLocation = Utils.getLocation(mContext);


        String distance = Utils.formatDistanceBetween(location, myLocation);

        if (TextUtils.isEmpty(distance)) {
            mItemDistance.setVisibility(View.GONE);
        }

        mItemDistance.setText(distance);
    }

    public void setThumbnail(String thumnailUrl) {
        // Dispatch the updates to any listeners
        dispatchThumbnailUrlUpdates(thumnailUrl);

    }

    public void setTextSize(float size) {
        mItemName.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }


    public void applyPalette(Palette palette) {
        Palette.Swatch textSwatch = palette.getMutedSwatch();

        if (textSwatch == null) {

            int color = ContextCompat.getColor(mContext, R.color.black);
            mItemName.setTextColor(color);
            mItemPrice.setTextColor(color);
            mItemDistance.setTextColor(color);
            mItemDistance.setTextColor(color);
            return;

        }
        mItemName.setTextColor(textSwatch.getTitleTextColor());
        mItemPrice.setTextColor(textSwatch.getTitleTextColor());
        mItemDistance.setTextColor(textSwatch.getTitleTextColor());
        mItemDistance.setTextColor(textSwatch.getTitleTextColor());

    }


    /**
     * Add a listener that will be called when the offset of this {@link AppBarLayout} changes.
     *
     * @param listener The listener that will be called when the thumbnail changes.]
     */
    public void addOnThumbnailChangedListener(OnThumbnailChangedListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        if (listener != null && !mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }


    /**
     * Remove the previously added {@link AppBarLayout.OnOffsetChangedListener}.
     *
     * @param listener the listener to remove.
     */
    public void removeOnThumbnailChangedListener(OnThumbnailChangedListener listener) {
        if (mListeners != null && listener != null) {
            mListeners.remove(listener);
        }
    }


    void dispatchThumbnailUrlUpdates(String url) {
        // Iterate backwards through the list so that most recently added listeners
        // get the first chance to decide
        if (mListeners != null) {
            for (int i = 0, z = mListeners.size(); i < z; i++) {
                final ItemHeaderView.OnThumbnailChangedListener listener = mListeners.get(i);
                if (listener != null) {
                    listener.onThumbnailChanged(this, url);
                }
            }
        }
    }

}
