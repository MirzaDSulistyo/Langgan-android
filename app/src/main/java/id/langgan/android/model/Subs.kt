package id.langgan.android.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import id.langgan.android.data.database.converter.BoxListTypeConverter

@Entity
@TypeConverters(BoxListTypeConverter::class)
data class Subs(
    @PrimaryKey
    @SerializedName("_id")
    @Expose
    val id: String = "",
    @SerializedName("store_id")
    @Expose
    val storeId: String? = null,
    @SerializedName("address")
    @Expose
    val address: String? = null,
    @SerializedName("city")
    @Expose
    val city: String? = null,
    @SerializedName("state")
    @Expose
    val state: String? = null,
    @SerializedName("country")
    @Expose
    val country: String? = null,
    @SerializedName("delivery")
    @Expose
    val delivery: String? = null,
    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null,
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null,
    @SerializedName("plan")
    @Expose
    var plans: List<Box>? = null
)