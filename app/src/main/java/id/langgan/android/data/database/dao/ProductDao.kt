package id.langgan.android.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.langgan.android.model.Product

@Dao
abstract class ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertProducts(users: List<Product>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun save(product: Product)

    @Query("SELECT * FROM product")
    abstract fun load(): LiveData<List<Product>>

    @Query("DELETE FROM product WHERE id = :id")
    abstract fun deleteByProductId(id: Int)
}