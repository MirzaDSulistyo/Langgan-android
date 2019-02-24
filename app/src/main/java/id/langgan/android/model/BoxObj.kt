package id.langgan.android.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import id.langgan.android.data.database.converter.StringListTypeConverter

@Entity
@TypeConverters(StringListTypeConverter::class)
data class BoxObj(
    @PrimaryKey
    @SerializedName("_id")
    @Expose
    val id: String = "",
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("store_id")
    @Expose
    val storeId: String = "",
    @SerializedName("description")
    @Expose
    val description: String = "",
    @SerializedName("price")
    @Expose
    val price: String = "",
    @SerializedName("created_at")
    @Expose
    val createdAt: String = "",
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String = "",
    @SerializedName("products")
    @Expose
    val products: List<String>? = null
)