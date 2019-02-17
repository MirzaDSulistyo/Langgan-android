package id.langgan.android.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.langgan.android.model.Favorite

@Dao
abstract class FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertFavorites(favorites: List<Favorite>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun save(favorite: Favorite)

    @Query("SELECT * FROM favorite")
    abstract fun load(): LiveData<List<Favorite>>

    @Query("DELETE FROM favorite WHERE id = :id")
    abstract fun deleteByFavoriteId(id: Int)
}