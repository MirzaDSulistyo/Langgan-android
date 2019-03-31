package id.langgan.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import id.langgan.android.data.vo.Resource
import id.langgan.android.model.Auth
import id.langgan.android.model.Profile
import id.langgan.android.repository.UserRepository
import id.langgan.android.utility.AbsentLiveData
import javax.inject.Inject

class UserViewModel
@Inject constructor(
    private val userRepository: UserRepository
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

    fun login(email: String, pass: String): LiveData<Resource<Auth>> {
        return userRepository.login(email, pass)
    }

    val profile: LiveData<Resource<Profile>> = Transformations
        .switchMap(_token) {token ->
            if (token == null) {
                AbsentLiveData.create()
            } else {
                userRepository.profile(token)
            }
        }

}