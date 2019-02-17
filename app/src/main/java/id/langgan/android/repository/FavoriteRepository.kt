package id.langgan.android.repository

import androidx.lifecycle.LiveData
import id.langgan.android.AppExecutors
import id.langgan.android.data.api.ApiService
import id.langgan.android.data.database.AppDb
import id.langgan.android.data.database.dao.FavoriteDao
import id.langgan.android.data.vo.Resource
import id.langgan.android.model.Favorite
import id.langgan.android.model.FavoriteList
import id.langgan.android.utility.RateLimiter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository
@Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: AppDb,
    private val favoriteDao: FavoriteDao,
    private val apiService: ApiService
)
{
    private val rateLimit = RateLimiter<String>(2, TimeUnit.MINUTES)

    fun getFavorites(token: String): LiveData<Resource<List<Favorite>>> {
        return object : NetworkBoundResource<List<Favorite>, FavoriteList>(appExecutors) {
            override fun saveCallResult(item: FavoriteList) {
                db.beginTransaction()
                try {
                    favoriteDao.insertFavorites(item.data!!)
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<Favorite>?): Boolean {
                return true
            }

            override fun createCall() = apiService.getFavorites(token)

            override fun loadFromDb() = favoriteDao.load()

            override fun onFetchFailed() {
                rateLimit.reset(token)
            }
        }.asLiveData()
    }
}