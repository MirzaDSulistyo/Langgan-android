package id.langgan.android.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.langgan.android.model.Box

@Dao
abstract class BoxDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertBoxes(users: List<Box>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun save(box: Box)

    @Query("SELECT * FROM box")
    abstract fun load(): LiveData<List<Box>>

    @Query("DELETE FROM box WHERE id = :id")
    abstract fun deleteByBoxId(id: String)
}