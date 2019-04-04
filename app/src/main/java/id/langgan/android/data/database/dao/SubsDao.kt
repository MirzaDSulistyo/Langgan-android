package id.langgan.android.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.langgan.android.model.Subs

@Dao
abstract class SubsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(data: List<Subs>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun save(data: Subs)

    @Query("SELECT * FROM subs")
    abstract fun load(): LiveData<List<Subs>>

    @Query("DELETE FROM subs WHERE id = :id")
    abstract fun deleteById(id: String)
}