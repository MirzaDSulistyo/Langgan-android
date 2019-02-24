package id.langgan.android.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.langgan.android.model.Product
import java.util.*

object ProductListTypeConverter {

    @TypeConverter
    @JvmStatic
    fun stringToProductList(data: String?) : List<Product>? {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType =  object : TypeToken<List<Product>>() {}.type

        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    @JvmStatic
    fun productListToString(data: List<Product>?): String? {
        return Gson().toJson(data)
    }
}