package id.langgan.android.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import id.langgan.android.model.Product

object ProductTypeConverter {

    @TypeConverter
    @JvmStatic
    fun stringToProduct(data: String?) : Product {
        return if (data != null)
            Gson().fromJson(data, Product::class.java)
        else
            Product()
    }

    @TypeConverter
    @JvmStatic
    fun productToString(data: Product?): String? {
        return Gson().toJson(data)
    }

}