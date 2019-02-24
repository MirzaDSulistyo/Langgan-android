package id.langgan.android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import id.langgan.android.data.database.dao.FavoriteDao
import id.langgan.android.data.database.dao.PlanDao
import id.langgan.android.data.database.dao.ProductDao
import id.langgan.android.data.database.dao.UserDao
import id.langgan.android.model.*

/**
 * Main database description.
 */
@Database(
    entities = [
        User::class,
        Product::class,
        Favorite::class,
        Box::class,
        BoxObj::class,
        Plan::class,
        Auth::class],
    version = 10,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun productDao(): ProductDao

    abstract fun favoriteDao(): FavoriteDao

    abstract fun planDao(): PlanDao

}