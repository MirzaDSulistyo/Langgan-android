package id.langgan.android.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import id.langgan.android.AppExecutors
import id.langgan.android.di.Injectable
import id.langgan.android.viewmodel.ProductViewModel
import javax.inject.Inject
import id.langgan.android.data.binding.FragmentDataBindingComponent
import id.langgan.android.model.Auth
import id.langgan.android.utility.autoCleared
import id.langgan.android.utility.Prefs
import id.langgan.android.R
import id.langgan.android.databinding.FragmentProductBinding
import id.langgan.android.model.Product
import id.langgan.android.ui.activity.FormProductActivity
import id.langgan.android.ui.adapter.ProductStoreAdapter
import kotlinx.android.synthetic.main.fragment_product.*
import timber.log.Timber

class ProductFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ProductViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentProductBinding>()

    private var adapter by autoCleared<ProductStoreAdapter>()

    private var auth: Auth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = Prefs()
        prefs.context = context
        auth = Gson().fromJson(prefs.user, Auth::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product, container, false, dataBindingComponent)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ProductViewModel::class.java)
        viewModel.setAuth(auth?.token)

        binding.lifecycleOwner = viewLifecycleOwner
        initRecyclerView()

        val rvAdapter = ProductStoreAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors
        ) { product -> details(product) }

        binding.rvProducts.adapter = rvAdapter
        adapter = rvAdapter

        btn_add_product.setOnClickListener {
            startActivity(Intent(activity, FormProductActivity::class.java))
//            activity?.startActivity<FormProductActivity>()
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.refresh()
    }

    private fun initRecyclerView() {
        refresh_products.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        refresh_products.setOnRefreshListener {
            viewModel.refresh()
        }

        binding.products = viewModel.products
        viewModel.products.observe(viewLifecycleOwner, Observer { result ->
            adapter.submitList(result?.data)
            refresh_products.isRefreshing = false
        })
    }

    private fun details(product: Product) {
        Timber.d("product ${product.name}")
        val intent = Intent(activity, FormProductActivity::class.java)
        intent.putExtra("json", Gson().toJson(product))
        startActivity(intent)
//        activity?.startActivity<FormProductActivity>("json" to Gson().toJson(product))
    }

    companion object {

        fun newInstance(): ProductFragment {
            return ProductFragment()
        }
    }

}