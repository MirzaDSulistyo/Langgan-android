package id.langgan.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import id.langgan.android.data.vo.Resource
import id.langgan.android.model.Favorite
import id.langgan.android.repository.FavoriteRepository
import id.langgan.android.utility.AbsentLiveData
import javax.inject.Inject

class FavoriteViewModel
@Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel()
{
    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    fun setAuth(token: String?) {
        if (_token.value != token) {
            _token.value = token
        }
    }

    fun refresh() {
        _token.value?.let {
            _token.value = it
        }
    }

    val favorites: LiveData<Resource<List<Favorite>>> = Transformations
        .switchMap(_token) {token ->
            if (token == null) {
                AbsentLiveData.create()
            } else {
                favoriteRepository.getFavorites(token)
            }
        }
}