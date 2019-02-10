package id.langgan.android.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.langgan.android.model.User

@Dao
abstract class UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUsers(users: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun save(user: User)

    @Query("SELECT * FROM user")
    abstract fun load(): LiveData<User>

    @Query("DELETE FROM user WHERE id = :id")
    abstract fun deleteByUserId(id: Int)
}