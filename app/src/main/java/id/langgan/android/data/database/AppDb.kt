package id.langgan.android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import id.langgan.android.data.database.dao.FavoriteDao
import id.langgan.android.data.database.dao.ProductDao
import id.langgan.android.data.database.dao.UserDao
import id.langgan.android.model.Auth
import id.langgan.android.model.Favorite
import id.langgan.android.model.Product
import id.langgan.android.model.User

/**
 * Main database description.
 */
@Database(
    entities = [
        User::class,
        Product::class,
        Favorite::class,
        Auth::class],
    version = 8,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun productDao(): ProductDao

    abstract fun favoriteDao(): FavoriteDao

}