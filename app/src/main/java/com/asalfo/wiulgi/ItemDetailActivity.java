package com.asalfo.wiulgi;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.asalfo.wiulgi.data.model.Item;
import com.asalfo.wiulgi.ui.ItemHeaderView;
import com.asalfo.wiulgi.util.Constants;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemDetailActivity extends AppCompatActivity
        implements ItemDetailActivityFragment.OnFragmentInteractionListener {

    public static final String LOG_TAG = ItemDetailActivity.class.getSimpleName();

    @BindView(R.id.container)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;
    @BindView(R.id.item_header_view)
    ItemHeaderView mItemHeaderView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.appBarLayout)
    AppBarLayout mAppBarLayout;

    private String mTitle;
    private boolean isHideToolbarView = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        mItemHeaderView.addOnThumbnailChangedListener(new ItemHeaderView.OnThumbnailChangedListener(){

            @Override
            public void onThumbnailChanged(final ItemHeaderView itemHeaderView, String thumbnailUrl) {

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
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

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


        Uri contentUri = getIntent().getData();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, ItemDetailActivityFragment.createInstance(contentUri,getIntent().getBooleanExtra("mongo_id",false)))
                    .commit();

        }

    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentLoad(Item item) {

        mItemHeaderView.setItem(item);

    }



}
