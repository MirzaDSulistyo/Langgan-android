package id.langgan.android.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
class User {
    @PrimaryKey
    @SerializedName("_id")
    @Expose
    var id: String = ""

    @SerializedName("first_name")
    @Expose
    var firstName: String? = null

    @SerializedName("last_name")
    @Expose
    var lastName: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
}