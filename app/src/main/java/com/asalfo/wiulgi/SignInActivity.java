package com.asalfo.wiulgi;


import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.asalfo.wiulgi.auth.ProfileManager;
import com.asalfo.wiulgi.auth.User;
import com.asalfo.wiulgi.data.provider.WiulgiContract;
import com.asalfo.wiulgi.event.MessageEvent;
import com.asalfo.wiulgi.event.UserEvent;
import com.asalfo.wiulgi.http.WiulgiApi;
import com.asalfo.wiulgi.service.UtilityService;
import com.asalfo.wiulgi.util.Constants;
import com.asalfo.wiulgi.util.Settings;
import com.asalfo.wiulgi.util.Utils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;


public class SignInActivity extends AppCompatActivity implements WiulgiApi.OnApiResponseListener {

    WiulgiApi mApi;

    @BindString(R.string.no_connection)
    String mNoConnextion;

    @Nullable
    @BindView(R.id.email_address)
    EditText mEmailAddressEditText;
    @Nullable
    @BindView(R.id.password)
    EditText mPasswordEditText;

    private Context mContext;
    private boolean isConnected;
    private String mEmailAddress;
    private String mPassword;
    private MaterialDialog mProgess;
    private Tracker mTracker;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApi = new WiulgiApi(this, this);
        mContext = this;

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initData();
        initUI();

        // Obtain the shared Tracker instance.
        WApplication application = (WApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(getString(R.string.sign_in_activity_title));
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public Intent getParentActivityIntent() {
        return new Intent(this, MainActivity.class);
    }


    private void initData() {
        if (getIntent().hasExtra("email")) {
            this.mEmailAddress = getIntent().getStringExtra("email");
            this.mPassword = getIntent().getStringExtra("password");
        }
    }

    private void initUI() {
        if (!TextUtils.isEmpty(this.mEmailAddress)) {
            this.mEmailAddressEditText.setText(this.mEmailAddress);
        } else if (Settings.getInstance().hasUserEmail()) {
            this.mEmailAddressEditText.setText(Settings.getInstance().getUserEmail());
        }
        if (!TextUtils.isEmpty(this.mPassword)) {
            this.mPasswordEditText.setText(this.mPassword);
        }

        mProgess = new MaterialDialog.Builder(this)
                .content("Please wait .....")
                .progress(true, 0)
                .build();
    }


    @OnClick(R.id.sign_in)
    public void onClickLogIn() {
        signIn();
    }

    @OnClick(R.id.btn_password)
    public void onClickForgetPassword() {

/*        new Builder(this).title(getString(R.string.session_password_reset_forget)).input(getString(R.string.session_password_reset_email_address), this.mEmailAddressEditText.getText().toString(), false, new InputCallback() {
            public void onInput(MaterialDialog dialog, CharSequence input) {
            }
        }).inputType(32).positiveText(getString(R.string.send)).negativeText(getString(R.string.cancel)).onPositive(new SingleButtonCallback() {
            public void onClick(MaterialDialog dialog, DialogAction which) {
                SignInActivity.this.mApi.postUserResetPassword(dialog.getInputEditText().getText().toString());
            }
        }).show();*/

    }

    @OnClick(R.id.link_sign_up)
    public void onClickSignUp() {
        startActivityForResult(new Intent(this, SignUpActivity.class), 14);
    }


    private void signIn() {

        Boolean error = false;
        String email = this.mEmailAddressEditText.getText().toString();

        if (TextUtils.isEmpty(email) || !email.matches(".+?@.+?\\..+?")) {
            error = true;
        }

        String password = this.mPasswordEditText.getText().toString();
        if (password.length() < 6 || password.length() > 128) {
            error = true;
        }

        if (error) {
            EventBus.getDefault().post(new MessageEvent(getString(R.string.wrong_credentials)));
            return;
        }
        User user = new User();
        user.setEmail(email);
        user.setPlainPassword(password);
        this.mApi.signIn(user);
        if (mProgess != null)
            mProgess.show();

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
    public void onMessageEvent(@NonNull MessageEvent event) {

        if (event.getType() != Constants.ONLY_MESSAGE)
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
            public void onClick(@NonNull DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.setMessage(event.getMessage());
        dialog.show();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onUserEvent(UserEvent event) {

    }


    @Override
    public void onApiRequestFailure(int statusCode, String message) {
        if (mProgess.isShowing()) {
            mProgess.dismiss();
        }
        new MaterialDialog.Builder(this)
                .content("Your email or password was entered incorrectly.")
                .positiveText(R.string.retry)
                .negativeText(R.string.cancel)
                .show();
    }

    @Override
    public void onApiRequestFinish() {

    }

    @Override
    public void onApiRequestStart() {

    }

    @Override
    public void onApiRequestSuccess(int i, @NonNull Response response) {
        if (mProgess.isShowing()) {
            mProgess.dismiss();
        }

        final User user = (User) response.body();
        ProfileManager.getInstance().logIn(user);

        new Thread(new Runnable() {
            public void run() {
                updateItems(user);
            }
        }).start();

        UtilityService.requestRecommendation(this);
        Utils.configureGCMTask(this);

        setResult(RESULT_OK);
        finish();

    }


    private void updateItems(@NonNull User user) {

        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();

        Utils.getItemsContentVals(batchOperations, user.getLikedItems(), WiulgiContract.Items.FAVORITED, "1");
        Utils.getItemsContentVals(batchOperations, user.getWishlistItems(), WiulgiContract.Items.WISHED, "1");
        Utils.getItemsContentVals(batchOperations, user.getRecommendedItems(), WiulgiContract.Items.RECOMMENDED, "1");
        try {
            getContentResolver().applyBatch(WiulgiContract.CONTENT_AUTHORITY, batchOperations);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }
}