package id.langgan.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import id.langgan.android.data.vo.Resource
import id.langgan.android.model.Subs
import id.langgan.android.repository.SubsRepository
import javax.inject.Inject
import id.langgan.android.utility.AbsentLiveData
import okhttp3.RequestBody

class SubsViewModel
@Inject constructor(
    private val repository: SubsRepository
) : ViewModel() {
    private val _token = MutableLiveData<String>()
    private val _id = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    val id: LiveData<String>
        get() = _id

    fun setAuth(token: String?) {
        if (_token.value != token) {
            _token.value = token
        }
    }

    fun setStoreId(id: String?) {
        if (_id.value != id) {
            _id.value = id
        }
    }

    fun refresh() {
        _token.value?.let {
            _token.value = it
        }
    }

    val data: LiveData<Resource<List<Subs>>> = Transformations
        .switchMap(_token) { token ->
            if (token == null) {
                AbsentLiveData.create()
            } else {
                repository.get(token, id.value!!)
            }
        }

    fun save(token: String, body: RequestBody): LiveData<Resource<Subs>> {
        return repository.save(token, body)
    }

    fun update(token: String, id: String, fields: Map<String, String>): LiveData<Resource<Subs>> {
        return repository.update(token, id, fields)
    }

    fun delete(token: String, id: String): LiveData<Resource<Subs>> {
        return repository.delete(token, id)
    }
}