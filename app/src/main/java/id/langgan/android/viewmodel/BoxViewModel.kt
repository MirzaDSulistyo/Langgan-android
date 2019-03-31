package id.langgan.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import id.langgan.android.data.vo.Resource
import id.langgan.android.model.Box
import id.langgan.android.repository.BoxRepository
import javax.inject.Inject
import id.langgan.android.utility.AbsentLiveData

class BoxViewModel
@Inject constructor(
    private val boxRepository: BoxRepository
) : ViewModel()
{
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

    val boxes: LiveData<Resource<List<Box>>> = Transformations
        .switchMap(_token) {token ->
            if (token == null) {
                AbsentLiveData.create()
            } else {
                boxRepository.getBoxes(token, id.value!!)
            }
        }
}