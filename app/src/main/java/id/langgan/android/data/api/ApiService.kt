package id.langgan.android.data.api

import androidx.lifecycle.LiveData
import id.langgan.android.model.Auth
import id.langgan.android.model.FavoriteList
import id.langgan.android.model.PlanList
import id.langgan.android.model.ProductList
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

    /* ===== PRODUCT ===== */

    @GET("product")
    fun getProduct(
        @Header("token") token: String
    ): LiveData<ApiResponse<ProductList>>

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
}