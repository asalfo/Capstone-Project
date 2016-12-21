package com.asalfo.wiulgi;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.asalfo.wiulgi.data.model.Item;
import com.asalfo.wiulgi.event.EventCode;
import com.asalfo.wiulgi.event.ItemEvent;
import com.asalfo.wiulgi.http.WiulgiApi;
import com.asalfo.wiulgi.ui.ItemAdapter;
import com.asalfo.wiulgi.ui.ItemHeaderView;
import com.asalfo.wiulgi.util.Constants;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class ItemDetailActivity extends AppCompatActivity
        implements ItemDetailActivityFragment.OnFragmentInteractionListener,
        WiulgiApi.OnApiResponseListener {

    public static final String LOG_TAG = ItemDetailActivity.class.getSimpleName();

    @Nullable
    @BindView(R.id.container)
    CoordinatorLayout mCoordinatorLayout;
    @Nullable
    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;
    @Nullable
    @BindView(R.id.item_header_view)
    ItemHeaderView mItemHeaderView;
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Nullable
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Nullable
    @BindView(R.id.appBarLayout)
    AppBarLayout mAppBarLayout;
    WiulgiApi mApi;
    private String mTitle;
    private boolean isHideToolbarView = false;
    private ItemDetailActivityFragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        ButterKnife.bind(this);
        mApi = new WiulgiApi(this, this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mItemHeaderView.addOnThumbnailChangedListener(new ItemHeaderView.OnThumbnailChangedListener() {

            @Override
            public void onThumbnailChanged(@NonNull final ItemHeaderView itemHeaderView, String thumbnailUrl) {

                int imageSize = getResources().getDimensionPixelSize(R.dimen.image_size)
                        * Constants.IMAGE_ANIM_MULTIPLIER;

                Picasso.with(getBaseContext())
                        .load(thumbnailUrl)
                        .error(R.drawable.empty_photo)
                        .placeholder(R.drawable.empty_photo)
                        .resize(imageSize, imageSize)
                        .into(itemHeaderView.getItemThumbnail());
            }
        });

        mCollapsingToolbarLayout.setTitle(" ");
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {


            @Override
            public void onOffsetChanged(@NonNull AppBarLayout appBarLayout, int verticalOffset) {

                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;


                if (percentage == 1f && isHideToolbarView) {
                    mCollapsingToolbarLayout.setTitle(mItemHeaderView.getItemName());
                    mItemHeaderView.setVisibility(View.INVISIBLE);
                    isHideToolbarView = !isHideToolbarView;

                } else if (percentage < 1f && !isHideToolbarView) {
                    mCollapsingToolbarLayout.setTitle(" ");
                    mItemHeaderView.setVisibility(View.VISIBLE);
                    isHideToolbarView = !isHideToolbarView;
                }

            }
        });


        if (savedInstanceState == null) {
            Uri contentUri = getIntent().getData();
            mFragment = ItemDetailActivityFragment.createInstance(contentUri, getIntent().getBooleanExtra("mongo_id", false));
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, mFragment)
                    .commit();

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(@NonNull ItemEvent event) {

        Snackbar snackbar = Snackbar
                .make(mCoordinatorLayout, event.getMessage(), Snackbar.LENGTH_LONG);
        snackbar.show();
        switch (event.getType()) {

            case EventCode.EVENT_ADD_WISHLIST:
                mApi.addToWhislist(event.getItem(), "add");
                break;
            case EventCode.EVENT_REMOVE_WISHLIST:
                mApi.addToWhislist(event.getItem(), "remove");
                break;

            case EventCode.EVENT_ADD_FAVORITE:
                mApi.like(event.getItem(), "add");
                break;
            case EventCode.EVENT_REMOVE_FAVORITE:
                mApi.like(event.getItem(), "remove");
                break;
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri, Class<?> cls, ItemAdapter.ViewHolder vh) {

    }

    @Override
    public void onFragmentInteraction(@NonNull final Item item) {
        mItemHeaderView.setItem(item);

    }

    @Override
    public void onApiRequestFailure(int statusCode, String message) {

        Log.e(LOG_TAG, message);
    }

    @Override
    public void onApiRequestFinish() {

    }

    @Override
    public void onApiRequestStart() {

    }

    @Override
    public void onApiRequestSuccess(int i, @NonNull Response response) {
        Log.v(LOG_TAG, response.body().toString());
    }
}
