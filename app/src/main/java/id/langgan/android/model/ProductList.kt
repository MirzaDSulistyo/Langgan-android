package id.langgan.android.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductList {

    @SerializedName("data")
    @Expose
    var products: List<Product>? = null

    @SerializedName("meta")
    @Expose
    val status: Int = 0

}