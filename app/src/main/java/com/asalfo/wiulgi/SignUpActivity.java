package com.asalfo.wiulgi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.asalfo.wiulgi.auth.ProfileManager;
import com.asalfo.wiulgi.auth.User;
import com.asalfo.wiulgi.http.WiulgiApi;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

/**
 * Created by asalfo on 26/06/16.
 */
public class SignUpActivity extends AppCompatActivity implements OnKeyListener,WiulgiApi.OnApiResponseListener {

        private String mEmailAddress;
        private String mPassword;

        @Nullable
        @BindView(R.id.username)
        EditText mUsernameText;
        @Nullable
        @BindView(R.id.email_address)
        EditText mEmailText;
        @Nullable
        @BindView(R.id.password)
        EditText mPasswordText;
        @Nullable
        @BindView(R.id.verify_password)
        EditText mRepeatPasswordText;
        @Nullable
        @BindView(R.id.btn_sign_up)
        Button mSignupButton;
        @Nullable
        @BindView(R.id.link_sign_in)
        TextView mLoginLink;
        @Nullable
        @BindView(R.id.agreement)
        AppCompatCheckBox mAgreement;

        WiulgiApi mApi;

    private Tracker mTracker;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_signup);
            ButterKnife.bind(this);
            initUI();
            mApi = new WiulgiApi(this,this);

            // Obtain the shared Tracker instance.
            WApplication application = (WApplication) getApplication();
            mTracker = application.getDefaultTracker();
            mTracker.setScreenName(getString(R.string.sing_up_activity_title));
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }


        private void initUI() {
            if (!TextUtils.isEmpty(this.mEmailAddress)) {
                this.mEmailText.setText(this.mEmailAddress);
            }
            this.mPasswordText.setOnKeyListener(this);
            this.mRepeatPasswordText.setOnKeyListener(this);
        }




        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
        }


        @OnClick(R.id.btn_sign_up)
        public void onClickSignUp() {
            signUp();
        }

        @OnClick(R.id.link_sign_in)
        public void onClickLogIn() {
            startActivityForResult(new Intent(this, SignInActivity.class), 14);
        }

        private void signUp() {
            boolean cancel = false;
            View errorView = null;


            String username= this.mUsernameText.getText().toString();

            if (TextUtils.isEmpty(username)) {
                cancel = true;
                errorView = this.mUsernameText;
                this.mUsernameText.setError(getString(R.string.error_field_required));
            }


            this.mEmailAddress = this.mEmailText.getText().toString();
            if (TextUtils.isEmpty(this.mEmailAddress)) {
                cancel = true;
                errorView = this.mEmailText;
                this.mEmailText.setError(getString(R.string.error_field_required));
            }
            if (!this.mEmailAddress.matches(".+?@.+?\\..+?")) {
                cancel = true;
                errorView = this.mEmailText;
                this.mEmailText.setError(getString(R.string.error_invalid_email));
            }


            this.mPassword = this.mPasswordText.getText().toString();
            if (this.mPassword.length() < 6 || this.mPassword.length() > 128) {
                cancel = true;
                errorView = this.mPasswordText;
                this.mPasswordText.setError(getString(R.string.error_invalid_password));
            }

            String repeatedPassword = mRepeatPasswordText.getText().toString();

            if (!this.mPassword.equals(repeatedPassword)) {
                cancel = true;
                errorView = this.mRepeatPasswordText;
                this.mRepeatPasswordText.setError(getString(R.string.error_password_not_match));
            }

            if(! mAgreement.isChecked()){
                mAgreement.setError("You must agree the terms and conditions");
                cancel = true;
                errorView = this.mAgreement;
            }

            if (cancel) {
                errorView.requestFocus();
                return;
            }
            User user = new User();
            user.setUsername(username);
            user.setPlainPassword(this.mPassword);
            user.setEmail(this.mEmailAddress);
            this.mApi.createUser(user);
        }

        @Override
        public boolean onKey(View v, int keyCode, @NonNull KeyEvent event) {
            if (event.getAction() != 0 || keyCode != 66) {
                return false;
            }
            ((InputMethodManager) SignUpActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.mPasswordText.getWindowToken(), 0);
            signUp();
            return true;
        }



    public void onApiRequestSuccess(int statusCode, @NonNull Response response) {

        User user = (User) response.body();
        if (user != null) {
            ProfileManager.getInstance().logIn(user);
            setResult(RESULT_OK);
            finish();
        }

    }

    @Override
    public void onApiRequestFailure(int statusCode, String message) {

    }

    @Override
    public void onApiRequestFinish() {

    }

    @Override
    public void onApiRequestStart() {

    }
}
