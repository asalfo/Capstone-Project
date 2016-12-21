package com.asalfo.wiulgi.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Model {
    private static final String[] DATE_FORMATS = new String[]{"yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd'T'HH:mm:ssZ", "yyyy-MM-dd"};

    public static <T> Object fromString(String objectString, @NonNull Class<T> classType) {
        if (TextUtils.isEmpty(objectString)) {
            return null;
        }
        return new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer()).create().fromJson(objectString, classType);
    }

    public String toString() {
        String json = new Gson().toJson(this);
        Log.d("Model",json);
        return new Gson().toJson(this);
    }

    private static class DateSerializer implements JsonDeserializer<Date> {
        private DateSerializer() {
        }

        @Nullable
        public Date deserialize(@NonNull JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            String[] access = Model.DATE_FORMATS;
            int i = 0;
            while (i < access.length) {
                try {
                    return new SimpleDateFormat(access[i]).parse(jsonElement.getAsString());
                } catch (ParseException e) {
                    i++;
                }
            }
            return null;
        }
    }
}
