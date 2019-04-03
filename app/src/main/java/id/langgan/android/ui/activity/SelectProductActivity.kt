package id.langgan.android.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import id.langgan.android.AppExecutors
import id.langgan.android.R
import id.langgan.android.databinding.ActivitySelectProductBinding
import id.langgan.android.model.Auth
import id.langgan.android.model.Product
import id.langgan.android.ui.adapter.ProductSelectedAdapter
import id.langgan.android.utility.Prefs
import id.langgan.android.viewmodel.ProductViewModel
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class SelectProductActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ProductViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    private lateinit var adapter: ProductSelectedAdapter

    private lateinit var binding: ActivitySelectProductBinding

    private var auth: Auth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_product)

        val prefs = Prefs()
        prefs.context = this
        auth = Gson().fromJson(prefs.user, Auth::class.java)

        // Handle Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.select_product)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ProductViewModel::class.java)
        viewModel.setAuth(auth?.token)

        binding.lifecycleOwner = this

        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.form, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_close -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        val rvAdapter = ProductSelectedAdapter(
            appExecutors = appExecutors
        ) { product -> selectProduct(product) }

        binding.rvProducts.adapter = rvAdapter
        adapter = rvAdapter

        viewModel.products.observe(this, Observer { result ->
            adapter.submitList(result?.data)
        })
    }

    private fun selectProduct(product: Product) {
        Toast.makeText(this, "Select product is ${product.name}", Toast.LENGTH_SHORT).show()
        val intent = Intent()
        intent.putExtra("product", Gson().toJson(product))
        setResult(Activity.RESULT_OK, intent)
        finish()

    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}
