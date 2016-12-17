package com.asalfo.wiulgi.http;



import com.asalfo.wiulgi.data.model.Item;
import com.asalfo.wiulgi.data.model.Rating;
import com.asalfo.wiulgi.auth.User;
import com.asalfo.wiulgi.data.model.WiugliCollection;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    String SELECTION = "selection";
    String PAGE = "page";

    @POST("api/tokensd")
    Call<ResponseBody> token();

    @POST("api/tokens")
    Call<User> login();

    @GET("/api/users/{username}")
    Call<User> getUser(@Path("username") String username);

    @POST("/api/users")
    Call<User> createUser(@Body User user,@Query("apikey") String api_key);

    @PUT("/api/users/{username}")
    Call<User> updateUser(@Path("username") String username, @Body User user);

    @FormUrlEncoded
    @PUT("/api/users/{username}/wishlist")
    Call<User> addToWishlist (@Path("username") String username, @Field("item_id") String id,@Field("action") String action);

    @FormUrlEncoded
    @PUT("/api/users/{username}/like")
    Call<User> like (@Path("username") String username, @Field("item_id") String id,@Field("action") String action);

    @GET("/api/users/{username}/wishlist")
    Call<WiugliCollection<Item>> getWishlist (@Path("username") String username);

    @GET("/api/users/{username}/recommended")
    Call<WiugliCollection<Item>> recommended (@Path("username") String username);

    @Multipart
    @PUT("/api/users/{username}/avatar")
    Call<User> uploadAvatar(@Path("username") String username, @Part("avatar\"; filename=\"image.png\" ") RequestBody photo);


    @GET("api/items")
    Call<WiugliCollection<Item>> itemList(@Query("selection") String sort, @Query("page") int page, @Query("apikey") String api_key);

    @POST("api/ratings")
    Call<Rating> rateItem(@Body Rating rating);


}
