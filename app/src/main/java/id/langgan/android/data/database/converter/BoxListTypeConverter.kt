package id.langgan.android.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.langgan.android.model.Box
import java.util.*

object BoxListTypeConverter {

    @TypeConverter
    @JvmStatic
    fun stringToBoxList(data: String?) : List<Box>? {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType =  object : TypeToken<List<Box>>() {}.type

        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    @JvmStatic
    fun boxListToString(data: List<Box>?): String? {
        return Gson().toJson(data)
    }
}