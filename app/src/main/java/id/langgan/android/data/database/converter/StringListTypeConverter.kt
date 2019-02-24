package id.langgan.android.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

object StringListTypeConverter {

    @TypeConverter
    @JvmStatic
    fun stringToStringList(data: String?) : List<String>? {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType =  object : TypeToken<List<String>>() {}.type

        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    @JvmStatic
    fun stringListToString(data: List<String>?): String? {
        return Gson().toJson(data)
    }
}