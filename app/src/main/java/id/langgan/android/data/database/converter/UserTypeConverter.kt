package id.langgan.android.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import id.langgan.android.model.User

object UserTypeConverter {

    var gson = Gson()

    @TypeConverter
    @JvmStatic
    fun stringToUser(data: String?) : User {

        return gson.fromJson(data, User::class.java)
    }

    @TypeConverter
    @JvmStatic
    fun userToString(data: User?): String? {
        return gson.toJson(data)
    }

}