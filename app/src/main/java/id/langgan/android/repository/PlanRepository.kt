package id.langgan.android.repository

import androidx.lifecycle.LiveData
import id.langgan.android.AppExecutors
import id.langgan.android.data.api.ApiService
import id.langgan.android.data.database.AppDb
import id.langgan.android.data.database.dao.PlanDao
import id.langgan.android.data.vo.Resource
import id.langgan.android.model.Plan
import id.langgan.android.model.PlanList
import id.langgan.android.utility.RateLimiter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlanRepository
@Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: AppDb,
    private val planDao: PlanDao,
    private val apiService: ApiService
)
{
    private val rateLimit = RateLimiter<String>(2, TimeUnit.MINUTES)

    fun getPlans(token: String): LiveData<Resource<List<Plan>>> {
        return object : NetworkBoundResource<List<Plan>, PlanList>(appExecutors) {
            override fun saveCallResult(item: PlanList) {
                db.beginTransaction()
                try {
                    planDao.insertPlans(item.data!!)
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<Plan>?): Boolean {
                return true
            }

            override fun createCall() = apiService.getSubscriptions(token)

            override fun loadFromDb() = planDao.load()

            override fun onFetchFailed() {
                rateLimit.reset(token)
            }
        }.asLiveData()
    }
}