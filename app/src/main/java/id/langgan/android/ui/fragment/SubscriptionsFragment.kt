package id.langgan.android.ui.fragment

import android.content.Context
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
import id.langgan.android.R
import id.langgan.android.di.Injectable
import id.langgan.android.model.Auth
import id.langgan.android.utility.autoCleared
import id.langgan.android.viewmodel.PlanViewModel
import javax.inject.Inject
import id.langgan.android.data.binding.FragmentDataBindingComponent
import id.langgan.android.databinding.FragmentSubscriptionsBinding
import id.langgan.android.model.Plan
import id.langgan.android.ui.adapter.PlanAdapter
import timber.log.Timber
import id.langgan.android.utility.Vars.PREF_AUTH
import id.langgan.android.utility.Vars.PREF_AUTH_KEY
import kotlinx.android.synthetic.main.fragment_subscriptions.*

class SubscriptionsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: PlanViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentSubscriptionsBinding>()
    private var adapter by autoCleared<PlanAdapter>()

    private var auth: Auth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userPrefs = activity?.getSharedPreferences(PREF_AUTH, Context.MODE_PRIVATE)
        val userStr = userPrefs?.getString(PREF_AUTH_KEY, "")
        auth = Gson().fromJson(userStr, Auth::class.java)

        Timber.d("auth token : %s", auth?.token)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subscriptions, container, false, dataBindingComponent)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(PlanViewModel::class.java)
        viewModel.setAuth(auth?.token)

        binding.lifecycleOwner = viewLifecycleOwner
        initRecyclerView()

        val rvAdapter = PlanAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors
        ) {  product -> details(product) }

        binding.rvPlans.adapter = rvAdapter
        adapter = rvAdapter
    }

    private fun initRecyclerView() {
        refresh_plans.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        refresh_plans.setOnRefreshListener {
            viewModel.refresh()
        }

        binding.plans = viewModel.plans
        viewModel.plans.observe(viewLifecycleOwner, Observer { result ->
            Timber.d("status : %s", result.status)
            Timber.d("message : %s", result.message)
            Timber.d("message : ${result.data?.size}")
            adapter.submitList(result?.data)
            refresh_plans.isRefreshing = false
        })
    }

    private fun details(plan: Plan) {
        Timber.d("plan ${plan.delivery}")
    }
}