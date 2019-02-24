package id.langgan.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import id.langgan.android.data.vo.Resource
import id.langgan.android.model.Plan
import id.langgan.android.repository.PlanRepository
import id.langgan.android.utility.AbsentLiveData
import javax.inject.Inject

class PlanViewModel
@Inject constructor(
    private val planRepository: PlanRepository
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

    val plans: LiveData<Resource<List<Plan>>> = Transformations
        .switchMap(_token) {token ->
            if (token == null) {
                AbsentLiveData.create()
            } else {
                planRepository.getPlans(token)
            }
        }
}