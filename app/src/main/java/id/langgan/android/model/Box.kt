package id.langgan.android.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import id.langgan.android.data.database.converter.ProductListTypeConverter

@Entity
@TypeConverters(ProductListTypeConverter::class)
data class Box (
    @PrimaryKey
    @SerializedName("_id")
    @Expose
    val id: String = "",
    @SerializedName("store_id")
    @Expose
    val storeId: String? = null,
    @SerializedName("name")
    @Expose
    val name: String? = null,
    @SerializedName("description")
    @Expose
    val description: String? = null,
    @SerializedName("price")
    @Expose
    val price: Int = 0,
    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null,
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null,
    @SerializedName("products")
    @Expose
    var products: List<Product>? = null
)