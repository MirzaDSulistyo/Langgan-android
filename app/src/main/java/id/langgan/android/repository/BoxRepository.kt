package id.langgan.android.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.langgan.android.AppExecutors
import id.langgan.android.data.api.*
import id.langgan.android.data.database.AppDb
import id.langgan.android.data.database.dao.BoxDao
import id.langgan.android.data.vo.Resource
import id.langgan.android.model.Box
import id.langgan.android.model.BoxList
import id.langgan.android.utility.RateLimiter
import okhttp3.RequestBody
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoxRepository
@Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: AppDb,
    private val boxDao: BoxDao,
    private val apiService: ApiService
) {
    private val rateLimit = RateLimiter<String>(2, TimeUnit.MINUTES)

    fun getBoxes(token: String, id: String): LiveData<Resource<List<Box>>> {
        return object : NetworkBoundResource<List<Box>, BoxList>(appExecutors) {
            override fun saveCallResult(item: BoxList) {
                db.beginTransaction()
                try {
                    boxDao.insertBoxes(item.boxes!!)
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<Box>?): Boolean {
                return true
            }

            override fun createCall() = apiService.getBox(token, id)

            override fun loadFromDb() = boxDao.load()

            override fun onFetchFailed() {
                rateLimit.reset(token)
            }
        }.asLiveData()
    }

    fun save(token: String, body: RequestBody): LiveData<Resource<Box>> {

        val data = MutableLiveData<Resource<Box>>()

        appExecutors.networkIO().execute {
            try {
                val response = apiService.postBox(token, body).execute()
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

    fun update(token: String, id: String, fields: Map<String, String>): LiveData<Resource<Box>> {

        val data = MutableLiveData<Resource<Box>>()

        appExecutors.networkIO().execute {
            try {
                val response = apiService.putBox(token, id, fields).execute()
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

    fun delete(token: String, id: String): LiveData<Resource<Box>> {

        val data = MutableLiveData<Resource<Box>>()

        appExecutors.networkIO().execute {
            try {
                val response = apiService.deleteBox(token, id).execute()
                val apiResponse = ApiResponse.create(response)

                when (apiResponse) {
                    is ApiSuccessResponse -> {
                        data.postValue(Resource.success(apiResponse.body))

                        db.beginTransaction()
                        try {
                            boxDao.deleteByBoxId(id)
                            db.setTransactionSuccessful()
                        }finally {
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