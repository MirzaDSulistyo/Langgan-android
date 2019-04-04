package id.langgan.android.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.langgan.android.AppExecutors
import id.langgan.android.data.api.*
import id.langgan.android.data.database.AppDb
import id.langgan.android.data.database.dao.SubsDao
import id.langgan.android.data.vo.Resource
import id.langgan.android.model.Subs
import id.langgan.android.model.SubsList
import id.langgan.android.utility.RateLimiter
import okhttp3.RequestBody
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubsRepository
@Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: AppDb,
    private val dao: SubsDao,
    private val apiService: ApiService
) {
    private val rateLimit = RateLimiter<String>(2, TimeUnit.MINUTES)

    fun get(token: String, id: String): LiveData<Resource<List<Subs>>> {
        return object : NetworkBoundResource<List<Subs>, SubsList>(appExecutors) {
            override fun saveCallResult(item: SubsList) {
                db.beginTransaction()
                try {
                    dao.insert(item.data!!)
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<Subs>?): Boolean {
                return true
            }

            override fun createCall() = apiService.getSubs(token, id)

            override fun loadFromDb() = dao.load()

            override fun onFetchFailed() {
                rateLimit.reset(token)
            }
        }.asLiveData()
    }

    fun save(token: String, body: RequestBody): LiveData<Resource<Subs>> {

        val data = MutableLiveData<Resource<Subs>>()

        appExecutors.networkIO().execute {
            try {
                val response = apiService.postSubs(token, body).execute()
                val apiResponse = ApiResponse.create(response)

                when (apiResponse) {
                    is ApiSuccessResponse -> {
                        data.postValue(Resource.success(apiResponse.body))
                    }
                    is ApiEmptyResponse -> {
                        data.postValue(Resource.success(data = null))
                    }
                    is ApiErrorResponse -> {
                        data.postValue(Resource.error(apiResponse.errorMessage, null))
                    }
                }
            } catch (e: SocketTimeoutException) {
                data.postValue(Resource.error("Socket Timeout", null))
            } catch (e: ConnectException) {
                data.postValue(Resource.error("Connection Error", null))
            }

        }

        return data
    }

    fun update(token: String, id: String, fields: Map<String, String>): LiveData<Resource<Subs>> {

        val data = MutableLiveData<Resource<Subs>>()

        appExecutors.networkIO().execute {
            try {
                val response = apiService.putSubs(token, id, fields).execute()
                val apiResponse = ApiResponse.create(response)

                when (apiResponse) {
                    is ApiSuccessResponse -> {
                        data.postValue(Resource.success(apiResponse.body))
                    }
                    is ApiEmptyResponse -> {
                        data.postValue(Resource.success(data = null))
                    }
                    is ApiErrorResponse -> {
                        data.postValue(Resource.error(apiResponse.errorMessage, null))
                    }
                }
            } catch (e: SocketTimeoutException) {
                data.postValue(Resource.error("Socket Timeout", null))
            }

        }

        return data
    }

    fun delete(token: String, id: String): LiveData<Resource<Subs>> {

        val data = MutableLiveData<Resource<Subs>>()

        appExecutors.networkIO().execute {
            try {
                val response = apiService.deleteSubs(token, id).execute()
                val apiResponse = ApiResponse.create(response)

                when (apiResponse) {
                    is ApiSuccessResponse -> {
                        data.postValue(Resource.success(apiResponse.body))

                        db.beginTransaction()
                        try {
                            dao.deleteById(id)
                            db.setTransactionSuccessful()
                        } finally {
                            db.endTransaction()
                        }
                    }
                    is ApiEmptyResponse -> {
                        data.postValue(Resource.success(data = null))
                    }
                    is ApiErrorResponse -> {
                        data.postValue(Resource.error(apiResponse.errorMessage, null))
                    }
                }
            } catch (e: SocketTimeoutException) {
                data.postValue(Resource.error("Socket Timeout", null))
            }
        }

        return data
    }
}