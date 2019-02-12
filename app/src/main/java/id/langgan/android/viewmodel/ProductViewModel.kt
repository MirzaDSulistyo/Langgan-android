package id.langgan.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import id.langgan.android.data.vo.Resource
import id.langgan.android.model.Product
import id.langgan.android.repository.ProductRepository
import id.langgan.android.utility.AbsentLiveData
import javax.inject.Inject

class ProductViewModel
@Inject constructor(
    private val productRepository: ProductRepository
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

    val products: LiveData<Resource<List<Product>>> = Transformations
        .switchMap(_token) {token ->
            if (token == null) {
                AbsentLiveData.create()
            } else {
                productRepository.getProducts(token)
            }
        }
}