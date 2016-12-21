package com.asalfo.wiulgi.http;


import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.asalfo.wiulgi.BuildConfig;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ApiServiceGenerator {


    public static String apiBaseUrl = BuildConfig.WIULGI_API_END_POINT;


    @NonNull
    private static Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    private static Retrofit retrofit;


    @NonNull
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(apiBaseUrl);


    @NonNull
    private static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

    @NonNull
    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(2000, TimeUnit.MILLISECONDS);


    public static void changeApiBaseUrl(String newApiBaseUrl) {
        apiBaseUrl = newApiBaseUrl;

        builder =
                new Retrofit.Builder()
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(new OkHttpClient())
                        .baseUrl(apiBaseUrl);
    }



    public static <S> S createService(@NonNull Class<S> serviceClass) {
        return createService(serviceClass,"");
    }


    public static <S> S createService(
            @NonNull Class<S> serviceClass, String clientId, String clientSecret) {
        if (!TextUtils.isEmpty(clientId)
                && !TextUtils.isEmpty(clientSecret)) {
            String authToken = Credentials.basic(clientId, clientSecret);
            return createService(serviceClass, authToken);
        }

        return create(serviceClass);
    }


    public static <S> S createService(
            @NonNull Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            httpClient.addInterceptor(
                    new AuthenticationInterceptor(authToken));
        }

        return create(serviceClass);
    }


    private static <S> S create(@NonNull Class<S> serviceClass) {
        if (! httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
        }

        builder.client(httpClient.build());
        retrofit = builder.build();

        return retrofit.create(serviceClass);
    }

    public static Retrofit retrofit() {
        return retrofit;
    }



}
