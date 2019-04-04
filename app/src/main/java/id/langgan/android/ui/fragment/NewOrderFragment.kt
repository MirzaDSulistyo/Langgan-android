package id.langgan.android.ui.fragment

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
import id.langgan.android.data.binding.FragmentDataBindingComponent
import id.langgan.android.model.Auth
import id.langgan.android.utility.autoCleared
import javax.inject.Inject
import id.langgan.android.utility.Prefs
import id.langgan.android.R
import id.langgan.android.databinding.FragmentNewOrderBinding
import id.langgan.android.model.Profile
import id.langgan.android.model.Subs
import id.langgan.android.viewmodel.SubsViewModel
import id.langgan.android.ui.adapter.SubsAdapter
import kotlinx.android.synthetic.main.fragment_new_order.*
import timber.log.Timber
import id.langgan.android.data.vo.Status.SUCCESS

class NewOrderFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private lateinit var viewModel: SubsViewModel

    var binding by autoCleared<FragmentNewOrderBinding>()

    var adapter by autoCleared<SubsAdapter>()

    private var auth: Auth? = null
    private var profile: Profile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = Prefs()
        prefs.context = context
        auth = Gson().fromJson(prefs.user, Auth::class.java)
        profile = Gson().fromJson(prefs.profile, Profile::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_order, container, false, dataBindingComponent)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SubsViewModel::class.java)
        viewModel.setAuth(auth?.token)
        viewModel.setStoreId(profile!!.stores!![0].id)

        binding.lifecycleOwner = viewLifecycleOwner
        initRecyclerView()

        val rvAdapter = SubsAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors
        ) { product -> details(product) }

        binding.rvSubs.adapter = rvAdapter
        adapter = rvAdapter

    }

    override fun onResume() {
        super.onResume()

        viewModel.refresh()
    }

    private fun initRecyclerView() {
        refresh_rv.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        refresh_rv.setOnRefreshListener {
            viewModel.refresh()
        }

        refresh_rv.visibility = View.GONE
//        shimmer.startShimmer()

        binding.subs = viewModel.data
        viewModel.data.observe(viewLifecycleOwner, Observer { result ->
            adapter.submitList(result?.data)
            refresh_rv.isRefreshing = false
            refresh_rv.visibility = View.VISIBLE
            if (result.status == SUCCESS) {
                Timber.d("results data ${result.data?.size}")
            }
//            shimmer.visibility = View.GONE
//            shimmer.stopShimmer()
        })
    }

    private fun details(sub: Subs) {
        Timber.d("subs ${sub.address}")
    }

}