package com.asalfo.wiulgi.http;


import android.content.Context;
import android.util.Log;

import com.asalfo.wiulgi.BuildConfig;
import com.asalfo.wiulgi.auth.ProfileManager;
import com.asalfo.wiulgi.auth.Settings;
import com.asalfo.wiulgi.auth.User;
import com.asalfo.wiulgi.data.model.ApiError;
import com.asalfo.wiulgi.data.model.Item;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WiulgiApi {

    private static final Integer[] SERVER_ERROR_CODES = new Integer[]{
            Integer.valueOf(500),
            Integer.valueOf(501),
            Integer.valueOf(502),
            Integer.valueOf(503),
            Integer.valueOf(504),
            Integer.valueOf(505)
    };

    private final Context mContext;

    private final OnApiResponseListener mListener;


    public interface OnApiResponseListener {
        void onApiRequestFailure(int statusCode, String message);

        void onApiRequestFinish();

        void onApiRequestStart();

        void onApiRequestSuccess(int i, Response response);
    }


    public WiulgiApi(Context mContext, OnApiResponseListener mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
    }



    public void signIn(User user){
        ApiInterface apiEndPoint =
                ApiServiceGenerator.createService(ApiInterface.class, user.getEmail(), user.getPlainPassword());
        Call<User> call = apiEndPoint.login();

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    mListener.onApiRequestSuccess(response.code(),response);
                }else{
                    ApiError error = ApiErrorUtil.parseError(response);
                    mListener.onApiRequestFailure(error.getStatusCode(),error.getMessage());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mListener.onApiRequestFailure(500, t.getMessage());
            }
        });
    }


    public void CreateUser(User user){

        ApiInterface apiEndPoint =
                ApiServiceGenerator.createService(ApiInterface.class);

        Call<User> call = apiEndPoint.createUser(user, BuildConfig.WIULGI_API_KEY);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    mListener.onApiRequestSuccess(response.code(),response);
                }else{
                    ApiError error = ApiErrorUtil.parseError(response);
                    mListener.onApiRequestFailure(error.getStatusCode(),error.getMessage());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                mListener.onApiRequestFailure(500,t.getMessage());
            }
        });

    }


    public void updateUser(User user){

        String token = "Bearer "+ Settings.getInstance().getUserApiToken();

        ApiInterface apiEndPoint =
                ApiServiceGenerator.createService(ApiInterface.class,token);

        Call<User> call = apiEndPoint.updateUser(user.getUsername(),user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    ProfileManager.getInstance().setUser(response.body());
                    mListener.onApiRequestSuccess(response.code(),response);
                }else{
                    ApiError error = ApiErrorUtil.parseError(response);
                    mListener.onApiRequestFailure(error.getStatusCode(),error.getMessage());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                mListener.onApiRequestFailure(500,t.getMessage());
            }
        });

    }


    private void addToWhislist(Item item) {

        String token = "Bearer "+ Settings.getInstance().getUserApiToken();

        ApiInterface apiEndPoint =
                ApiServiceGenerator.createService(ApiInterface.class,token);

        User user = ProfileManager.getInstance().getUser();
        if (null != user) {
            Call<User> call = apiEndPoint.addToWishlist(user.getUsername(),item.getId());
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User user = response.body();
                        mFavorite.setImageResource(R.drawable.ic_favorite);
                        Log.d(LOG_TAG, user.getUsername());
                    } else {
                        ApiError error = ApiErrorUtil.parseError(response);
                        Log.d(LOG_TAG, error.message());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d(LOG_TAG, t.getMessage());
                }
            });

        }
    }
}
