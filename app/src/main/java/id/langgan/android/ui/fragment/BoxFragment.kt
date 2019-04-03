package id.langgan.android.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import id.langgan.android.AppExecutors
import id.langgan.android.di.Injectable
import id.langgan.android.viewmodel.BoxViewModel
import javax.inject.Inject
import id.langgan.android.data.binding.FragmentDataBindingComponent
import id.langgan.android.model.Auth
import id.langgan.android.model.Box
import id.langgan.android.utility.autoCleared
import id.langgan.android.utility.Prefs
import id.langgan.android.R
import timber.log.Timber
import id.langgan.android.databinding.FragmentBoxBinding
import id.langgan.android.ui.adapter.BoxAdapter
import kotlinx.android.synthetic.main.fragment_box.*
import id.langgan.android.data.vo.Status.SUCCESS
import id.langgan.android.model.Profile
import id.langgan.android.ui.activity.FormBoxActivity
import org.jetbrains.anko.startActivity

class BoxFragment: Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: BoxViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentBoxBinding>()

    private var adapter by autoCleared<BoxAdapter>()

    private var auth: Auth? = null
    private var profile: Profile? = null

    private var boxes = ArrayList<Box>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = Prefs()
        prefs.context = context
        auth = Gson().fromJson(prefs.user, Auth::class.java)
        profile = Gson().fromJson(prefs.profile, Profile::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_box, container, false, dataBindingComponent)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(BoxViewModel::class.java)
        viewModel.setAuth(auth?.token)
        viewModel.setStoreId(profile?.stores!![0].id)

        binding.lifecycleOwner = viewLifecycleOwner
        initRecyclerView()

        val rvAdapter = BoxAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors
        ) {  box -> details(box) }

        var isUserScrolling = false
        var isListGoingUp = false

        binding.rvBoxes.adapter = rvAdapter
        val linearLayoutManager = LinearLayoutManager(context)
        binding.rvBoxes.layoutManager = linearLayoutManager
        binding.rvBoxes.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    Timber.d("TOP")
                }

                if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {
                    Timber.d("TOP 2")
                }

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isUserScrolling = true
                    if (isListGoingUp) {
                        //my recycler view is actually inverted so I have to write this condition instead
                        if (linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1 == boxes.size) {
                            val handler = Handler()
                            handler.postDelayed(Runnable {
                                if (isListGoingUp) {
                                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1 == boxes.size) {
                                        Toast.makeText(context, "execute something", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }, 50)
                            //waiting for 50ms because when scrolling down from top, the variable isListGoingUp is still true until the onScrolled method is executed
                        }
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(isUserScrolling){
                    isListGoingUp = dy <= 0
                }
            }
        })
        adapter = rvAdapter

        btn_add_box.setOnClickListener {
            startActivity(Intent(activity, FormBoxActivity::class.java))
        }
    }

    private fun initRecyclerView() {
        refresh_boxes.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        refresh_boxes.setOnRefreshListener {
            viewModel.refresh()
        }

        refresh_boxes.visibility = View.GONE
        shimmer.startShimmer()

        binding.boxes = viewModel.boxes
        viewModel.boxes.observe(viewLifecycleOwner, Observer { result ->
            if (result.status == SUCCESS) {
                adapter.submitList(result?.data)
                refresh_boxes.isRefreshing = false
                boxes.addAll(result?.data!!)
                refresh_boxes.visibility = View.VISIBLE
                shimmer.visibility = View.GONE
                shimmer.stopShimmer()
            }
        })
    }

    private fun details(box: Box) {
        Timber.d("box ${box.name}")
        activity?.startActivity<FormBoxActivity>("json" to Gson().toJson(box))
    }

    companion object {

        fun newInstance(): BoxFragment {
            return BoxFragment()
        }
    }
}