package id.langgan.android.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.kaopiz.kprogresshud.KProgressHUD
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import id.langgan.android.AppExecutors
import id.langgan.android.R
import id.langgan.android.data.vo.Resource
import id.langgan.android.data.vo.Status
import id.langgan.android.di.Injectable
import id.langgan.android.viewmodel.BoxViewModel
import javax.inject.Inject
import id.langgan.android.databinding.ActivityFormBoxBinding
import id.langgan.android.model.Auth
import id.langgan.android.model.Box
import id.langgan.android.model.Product
import id.langgan.android.model.Profile
import id.langgan.android.ui.adapter.ProductSelectedAdapter
import id.langgan.android.ui.dialog.DeleteConfirmationDialog
import id.langgan.android.utility.Prefs
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_form_box.*
import okhttp3.MultipartBody
import timber.log.Timber

class FormBoxActivity : AppCompatActivity(), HasSupportFragmentInjector, Injectable, DeleteConfirmationDialog.NoticeDialogListener {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: BoxViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var binding: ActivityFormBoxBinding

    private lateinit var adapter: ProductSelectedAdapter

    private var auth: Auth? = null
    private var profile: Profile? = null

    private var products = ArrayList<Product>()

    private var box: Box? = null

    private var hud: KProgressHUD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_form_box)

        if (intent.getStringExtra("json") != null) {
            box = Gson().fromJson(intent.getStringExtra("json"), Box::class.java)
            binding.box = box

            products.addAll(box!!.products!!)
        }

        val prefs = Prefs()
        prefs.context = this
        auth = Gson().fromJson(prefs.user, Auth::class.java)
        profile = Gson().fromJson(prefs.profile, Profile::class.java)

        hud = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(false)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(BoxViewModel::class.java)
        viewModel.setAuth(auth?.token)
        viewModel.setStoreId(profile?.stores!![0].id)

        binding.lifecycleOwner = this

        initRecyclerView()

        binding.close.setOnClickListener { finish() }
        binding.addProducts.setOnClickListener { selectProducts() }

        binding.save.setOnClickListener {
            if (box == null) {
                onSaveData()
            } else {
                onUpdateData()
            }
        }

        binding.delete.setOnClickListener { onDelete() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PRODUCT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val json = data?.getStringExtra("product")
                Timber.d("productnya $json")

                val product = Gson().fromJson(json, Product::class.java)
                products.add(product)

                for (p in products) {
                    Timber.d("productnya size ${p.name}")
                }

                binding.products = products

                Timber.d("productnya count ${adapter.itemCount}")
                adapter.notifyDataSetChanged()

            }
        }
    }

    private fun initRecyclerView() {
        val rvAdapter = ProductSelectedAdapter(
            appExecutors = appExecutors
        ) { product -> details(product) }

        binding.rvProducts.adapter = rvAdapter
        adapter = rvAdapter
        adapter.submitList(products)
    }

    private fun details(product: Product) {
//        products.add(product)
    }

    private fun selectProducts() {
        startActivityForResult(Intent(this, SelectProductActivity::class.java), PRODUCT_REQUEST_CODE)
    }

    private fun onDelete() {
        val fragment = DeleteConfirmationDialog.newInstance(box?.name!!)
        fragment.show(supportFragmentManager, "delete")
    }


    private fun onSaveData() {
        hud?.show()

        val name = name.text.toString()
        val description = descriptions.text.toString()
        val price = price.text.toString()

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        builder.addFormDataPart("name", name)
        builder.addFormDataPart("description", description)
        builder.addFormDataPart("price", price)
        builder.addFormDataPart("store_id", profile!!.stores!![0].id!!)

        for (i in products.indices) {
            builder.addFormDataPart("products[$i]", products[i].id)
        }

        val body = builder.build()

        viewModel.save(auth?.token!!, body)
            .observe(this, Observer<Resource<Box>> { response ->
                if (response.status == Status.SUCCESS) {
                    hud!!.dismiss()
                    finish()
                }

                if (response.status == Status.ERROR) {
                    hud!!.dismiss()
                    Timber.d("message : %s", response.message)
                }

            })
    }

    private fun onUpdateData() {
        hud?.show()

        val name = name.text.toString()
        val description = descriptions.text.toString()
        val price = price.text.toString()

        val fieldMap = HashMap<String, String>()

        fieldMap["name"] = name
        fieldMap["description"] = description
        fieldMap["price"] = price

        for (i in products.indices) {
            fieldMap["products[$i]"] = products[i].id
        }

        viewModel.update(auth!!.token!!, box?.id!!, fieldMap)
            .observe(this, Observer<Resource<Box>> { response ->
                if (response.status == Status.SUCCESS) {
                    Timber.d("message : %s", Gson().toJson(response.data))
                    hud?.dismiss()
                    finish()
                }

                if (response.status == Status.ERROR) {
                    hud?.dismiss()
                    Timber.d("message : %s", response.message)
                }

            })
    }

    override fun onDeleteItem() {
        hud?.show()

        viewModel.delete(auth!!.token!!, box!!.id)
            .observe(this, Observer<Resource<Box>> { response ->
                if (response.status == Status.SUCCESS) {
                    hud!!.dismiss()
                    finish()
                }

                if (response.status == Status.ERROR) {
                    Timber.d("message : %s", response.message)
                    hud!!.dismiss()
                }

            })
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    companion object {
        private const val PRODUCT_REQUEST_CODE = 1001
    }

}
