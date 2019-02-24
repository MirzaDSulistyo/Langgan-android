package id.langgan.android.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.langgan.android.model.Plan

@Dao
abstract class PlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPlans(plans: List<Plan>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun save(plan: Plan)

    @Query("SELECT * FROM `plan`")
    abstract fun load(): LiveData<List<Plan>>

    @Query("DELETE FROM `plan` WHERE id = :id")
    abstract fun deleteByPlanId(id: Int)
}