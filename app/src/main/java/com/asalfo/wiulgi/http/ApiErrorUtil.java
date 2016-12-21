package com.asalfo.wiulgi.http;

import android.support.annotation.NonNull;

import com.asalfo.wiulgi.data.model.ApiError;
import com.asalfo.wiulgi.http.ApiServiceGenerator;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by asalfo on 23/06/16.
 */
public class ApiErrorUtil {


        public static ApiError parseError(@NonNull Response<?> response) {
            Converter<ResponseBody, ApiError> converter =
                    ApiServiceGenerator.retrofit()
                            .responseBodyConverter(ApiError.class, new Annotation[0]);

            ApiError error;

            try {
                error = converter.convert(response.errorBody());
            } catch (IOException e) {
                return new ApiError();
            }

            return error;
        }

}
