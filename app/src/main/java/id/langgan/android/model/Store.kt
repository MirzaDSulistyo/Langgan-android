package id.langgan.android.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Store {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: String? = null

    var name: String? = null
    var description: String? = null
    var email: String? = null
    var phone: String? = null
    var address: String? = null
    var city: String? = null
    var state: String? = null
    var country: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
}