package id.langgan.android.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import id.langgan.android.AppExecutors
import id.langgan.android.R
import id.langgan.android.di.Injectable
import id.langgan.android.ui.activity.MainActivity
import id.langgan.android.viewmodel.ProductViewModel
import javax.inject.Inject
import id.langgan.android.data.binding.FragmentDataBindingComponent
import id.langgan.android.databinding.FragmentProfileBinding
import id.langgan.android.model.Auth
import id.langgan.android.model.Profile
import id.langgan.android.utility.autoCleared
import id.langgan.android.ui.activity.StoreActivity

class ProfileFragment: Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ProductViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private var binding by autoCleared<FragmentProfileBinding>()

    private var auth: Auth? = null
    private var profile: Profile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity = activity as MainActivity
        auth = activity.auth
        profile = activity.profile
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false, dataBindingComponent)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ProductViewModel::class.java)
        viewModel.setAuth(auth?.token)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.profile = profile

        binding.store.setOnClickListener {
            startActivity(Intent(activity, StoreActivity::class.java))
        }
    }
}