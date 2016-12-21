package com.asalfo.wiulgi.data.model;

import android.support.annotation.NonNull;

/**
 * Created by asalfo on 23/06/16.
 */
public class ApiError {


    private int statusCode;
    private String detail;
    private String message = "";
    private String type;
    private String title;


    public ApiError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @NonNull
    @Override
    public String toString() {
        return "ApiError{" +
                "statusCode=" + statusCode +
                ", detail='" + detail + '\'' +
                ", message='" + message + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}


