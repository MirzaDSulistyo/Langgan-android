package id.langgan.android.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FavoriteList {

    @SerializedName("data")
    @Expose
    var data: List<Favorite>? = null

    @SerializedName("status")
    @Expose
    val status: Int = 0

}