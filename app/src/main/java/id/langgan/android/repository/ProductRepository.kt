package id.langgan.android.repository

import androidx.lifecycle.LiveData
import id.langgan.android.AppExecutors
import id.langgan.android.data.api.ApiService
import id.langgan.android.data.database.AppDb
import id.langgan.android.data.database.dao.ProductDao
import id.langgan.android.data.vo.Resource
import id.langgan.android.model.Product
import id.langgan.android.model.ProductList
import id.langgan.android.utility.RateLimiter
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository
@Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: AppDb,
    private val productDao: ProductDao,
    private val apiService: ApiService
)
{
    private val rateLimit = RateLimiter<String>(2, TimeUnit.MINUTES)

    fun getProducts(token: String): LiveData<Resource<List<Product>>> {
        Timber.d("auth token : %s", token)
        return object : NetworkBoundResource<List<Product>, ProductList>(appExecutors) {
            override fun saveCallResult(item: ProductList) {
                db.beginTransaction()
                try {
                    productDao.insertProducts(item.products!!)
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<Product>?): Boolean {
                return true
            }

            override fun createCall() = apiService.getProduct(token)

            override fun loadFromDb() = productDao.load()

            override fun onFetchFailed() {
                rateLimit.reset(token)
            }
        }.asLiveData()
    }
}