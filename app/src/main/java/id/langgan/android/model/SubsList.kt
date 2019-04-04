package id.langgan.android.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SubsList {

    @SerializedName("data")
    @Expose
    var data: List<Subs>? = null

    @SerializedName("status")
    @Expose
    val status: Int = 0

}