package id.langgan.android.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.kaopiz.kprogresshud.KProgressHUD
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import id.langgan.android.AppExecutors
import id.langgan.android.R
import id.langgan.android.data.vo.Resource
import id.langgan.android.data.vo.Status
import id.langgan.android.model.Auth
import id.langgan.android.utility.Helper
import id.langgan.android.utility.InternetConnection
import id.langgan.android.utility.Vars
import id.langgan.android.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: UserViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    private var hud: KProgressHUD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(UserViewModel::class.java)

        login.setOnClickListener { onLogin() }
    }

    private fun onLogin() {
        Helper.hideSoftKeyboard(this@LoginActivity)

        if (InternetConnection.checkConnection(applicationContext)) {
//            hud = KProgressHUD.create(this)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setLabel("Please wait")
//                .setCancellable(false)
//                .setAnimationSpeed(2)
//                .setDimAmount(0.5f)
//                .show()
            loading.visibility = View.VISIBLE

            val email = email.text.toString()
            val pass = password.text.toString()

            viewModel.login(email, pass)
                .observe(this, Observer<Resource<Auth>> {
                    if (it.status == Status.SUCCESS) {
                        Timber.d("auth token : %s", it.data?.token)

                        val userPrefs = this.getSharedPreferences(Vars.PREF_AUTH, Context.MODE_PRIVATE)
                        val editor = userPrefs.edit()
                        editor.putString(Vars.PREF_AUTH_KEY, Gson().toJson(it.data))
                        editor.apply()

                        startActivity(Intent(this, MainActivity::class.java))

                        loading.visibility = View.GONE
                    }

                    if (it.status == Status.ERROR) {
                        loading.visibility = View.GONE
                        Timber.d("message : %s", it.message)
                    }
                })
        } else {
            val snackBar = Snackbar.make(login_layout, getString(R.string.connection_not_available), Snackbar.LENGTH_INDEFINITE)
            snackBar.show()
        }
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}
