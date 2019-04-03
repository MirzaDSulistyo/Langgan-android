package id.langgan.android.data.api

import androidx.lifecycle.LiveData
import id.langgan.android.model.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    /* ====== AUTHENTICATION ===== */

    @FormUrlEncoded
    @POST("users/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Auth>

    /* ====== PROFILE ===== */

    @GET("users/profile")
    fun profile(
        @Header("token") token: String
    ): Call<Profile>

    /* ===== PRODUCT ===== */

    @GET("product")
    fun getProduct(
        @Header("token") token: String
    ): LiveData<ApiResponse<ProductList>>

    @GET("product/list/{id}")
    fun getProductById(
        @Header("token") token: String,
        @Path("id") id: String
    ): LiveData<ApiResponse<ProductList>>

    @POST("product/new")
    fun postProduct(
        @Header("token") token: String,
        @Body body: RequestBody
    ): Call<Product>

    @FormUrlEncoded
    @PUT("product/{id}")
    fun putProduct(
        @Header("token") token: String,
        @Path("id") id: String,
        @FieldMap fields: Map<String, String>
    ): Call<Product>

    @DELETE("product/{id}")
    fun deleteProduct(
        @Header("token") token: String,
        @Path("id") id: String
    ): Call<Product>

    /* ===== FAVORITE ===== */

    @GET("favorite")
    fun getFavorites(
        @Header("token") token: String
    ): LiveData<ApiResponse<FavoriteList>>

    /* ===== SUBSCRIPTION ===== */

    @GET("subs")
    fun getSubscriptions(
        @Header("token") token: String
    ): LiveData<ApiResponse<PlanList>>

    /* ====== BOX ===== */

    @GET("box/list/{id}")
    fun getBox(
        @Header("token") token: String,
        @Path("id") id: String
    ): LiveData<ApiResponse<BoxList>>

    @POST("box/new")
    fun postBox(
        @Header("token") token: String,
        @Body body: RequestBody
    ): Call<Box>

    @FormUrlEncoded
    @PUT("box/{id}")
    fun putBox(
        @Header("token") token: String,
        @Path("id") id: String,
        @FieldMap fields: Map<String, String>
    ): Call<Box>

    @DELETE("box/{id}")
    fun deleteBox(
        @Header("token") token: String,
        @Path("id") id: String
    ): Call<Box>
}