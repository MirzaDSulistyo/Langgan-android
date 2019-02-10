package id.langgan.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.langgan.android.data.vo.Resource
import id.langgan.android.model.Auth
import id.langgan.android.repository.UserRepository
import javax.inject.Inject

class UserViewModel
@Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun login(email: String, pass: String): LiveData<Resource<Auth>> {
        return userRepository.login(email, pass)
    }

}