package id.langgan.android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import id.langgan.android.data.database.dao.UserDao
import id.langgan.android.model.Auth
import id.langgan.android.model.User

/**
 * Main database description.
 */
@Database(
    entities = [
        User::class,
        Auth::class],
    version = 1,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {

    abstract fun userDao(): UserDao

}