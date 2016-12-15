/*
 * Copyright 2015 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.asalfo.wiulgi.util;

public class Constants {

    private Constants() {};

    // Set to false to have the geofence trigger use the enhanced notifications instead
    public static final boolean USE_MICRO_APP = true;

    public static final int GOOGLE_API_CLIENT_TIMEOUT_S = 10; // 10 seconds
    public static final String GOOGLE_API_CLIENT_ERROR_MSG =
            "Failed to connect to GoogleApiClient (error code = %d)";

    // Used to size the images in the mobile app so they can animate cleanly from list to detail
    public static final int IMAGE_ANIM_MULTIPLIER =2;

    // Resize images sent to Wear to 400x400px
    public static final int WEAR_IMAGE_SIZE = 400;

    // Except images that can be set as a background with parallax, set width 640x instead
    public static final int WEAR_IMAGE_SIZE_PARALLAX_WIDTH = 640;

    // The minimum bottom inset percent to use on a round screen device
    public static final float WEAR_ROUND_MIN_INSET_PERCENT = 0.08f;

    // Max # of attractions to show at once
    public static final int MAX_ATTRACTIONS = 4;

    // Notification IDs
    public static final int MOBILE_NOTIFICATION_ID = 100;
    public static final int WEAR_NOTIFICATION_ID = 200;

    // Intent and bundle extras
    public static final String EXTRA_ATTRACTIONS = "extra_attractions";
    public static final String EXTRA_ATTRACTIONS_URI = "extra_attractions_uri";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_DESCRIPTION = "extra_description";
    public static final String EXTRA_LOCATION_LAT = "extra_location_lat";
    public static final String EXTRA_LOCATION_LNG = "extra_location_lng";
    public static final String EXTRA_DISTANCE = "extra_distance";
    public static final String EXTRA_CITY = "extra_city";
    public static final String EXTRA_IMAGE = "extra_image";
    public static final String EXTRA_IMAGE_SECONDARY = "extra_image_secondary";
    public static final String EXTRA_TIMESTAMP = "extra_timestamp";

    // Wear Data API paths
    public static final String ATTRACTION_PATH = "/attraction";
    public static final String START_PATH = "/start";
    public static final String START_ATTRACTION_PATH = START_PATH + "/attraction";
    public static final String START_NAVIGATION_PATH = START_PATH + "/navigation";
    public static final String CLEAR_NOTIFICATIONS_PATH = "/clear";

    // Maps values
    public static final String MAPS_INTENT_URI = "geo:0,0?q=";
    public static final String MAPS_NAVIGATION_INTENT_URI = "google.navigation:mode=w&q=";

    public static final String TAG = "tag";
    public static final String INIT = "init";
    public static final String ADD = "add";
    public static final String PERIODIC_TAG = "periodic";
    public static final String ACTION_DATA_UPDATED = "com.asalfo.wiulgi.ACTION_DATA_UPDATED";
    public static final String TOKEN = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXUyJ9.eyJ1c2VybmFtZSI6InRlc3Nkc3MiLCJpYXQiOiIxNDY4MDAxMjQyIn0.R1nk1ds7vIgslw0ncO3ZB7erHp26aOMyYUuVRSXowDzrU3zq1NWGrLeA6LysWhy1y2G9_IRqmDA3kHOU9UMAMtnt6-OzXC2dBaxeU_mDNzTCzcFT2sxGqh6vFdgYWW292scV6Yl4zl7MUwhAsEc-Iza_PhMVMZflMUbe5QbMoJcIs2QMB2vhZTheIEatssJ8VPQaARVJfpOnwDlGt6-7C19Mg6fBuhRtiFDSSckJjcNOSEefjIPajNmCLE3zO7at6ULG6fQ4Kq3ACxcV3hIscsWHhbtjRbLdlIaXBanoPPmG81VBkH6uIUvcf8xjrxLisU9CYOgDwOnawWb_k8dPxk9AbUzzHq2DVgeITYrD-6qEQQvni45ROvbPJuhGgZvyI4CxQ1ZNPXkwOvOa1Ywx2QnV7XAKoMgJiYOkDUtlSE1iPBoR9KTz6jDw-e9N97A-cbySzM0lOyzpr2Hy107_JwmPoETq31KJaDVhox7EWdfEJk5plyhCVD_ahfExUzGcbgI_Hn9GOcJLhiYfiFEsNwcOTPf7YSRLXYhvrI8l4YD04jCHRkXVNZBHKiszLBhdmGFDjOIaBztEPaMaaZaO_r7De9jLaEAfWiFZ9CyW7R4_Xo4q8AhACKqv9IHO5lZq1yPjmK8U8v5cPNsMBNNtzladLzKWME4qsFQUzvn2kh8";

// Events type

    public static final int ONLY_MESSAGE = 0;
    public static final int LOGIN_SUCCESS = 1;
    public static final int LOGOUT_SUCCESS = 2;

}
