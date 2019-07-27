package id.langgan.android.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import id.langgan.android.BuildConfig
import id.langgan.android.R
import id.langgan.android.data.vo.Status
import id.langgan.android.di.Injectable
import id.langgan.android.model.Auth
import id.langgan.android.model.Profile
import id.langgan.android.ui.fragment.*
import id.langgan.android.utility.Vars
import id.langgan.android.viewmodel.UserViewModel
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, Injectable {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: UserViewModel

    var auth: Auth? = null
    var profile: Profile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(UserViewModel::class.java)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val userPrefs = this.getSharedPreferences(Vars.PREF_AUTH, Context.MODE_PRIVATE)
        val userStr = userPrefs.getString(Vars.PREF_AUTH_KEY, "")

        if (userStr.isNullOrEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            auth = Gson().fromJson(userStr, Auth::class.java)
            viewModel.setAuth(auth?.token)
            Timber.d("user : %s", auth?.token)
            Timber.d("user : %s", auth?.user?.email)
            viewModel.profile.observe(this, Observer {
                if (it.status == Status.SUCCESS) {
                    Timber.d("profile ${Gson().toJson(it.data)}")
                    profile = it.data

                    val profilePrefs = this.getSharedPreferences(Vars.PREF_PROFILE, Context.MODE_PRIVATE)
                    val editor = profilePrefs.edit()
                    editor.putString(Vars.PREF_PROFILE_KEY, Gson().toJson(it.data))
                    editor.apply()
                }
            })
        }

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            var fragment : Fragment? = null

            when (item.itemId) {
                R.id.home_menu -> fragment = HomeFragment()
                R.id.favorite_menu -> fragment = FavoriteFragment()
                R.id.subscriptions_menu -> fragment = SubscriptionsFragment()
                R.id.inbox_menu -> fragment = InboxFragment()
                R.id.account_menu -> fragment = ProfileFragment()
            }
            loadFragment(fragment)
        }

        loadFragment(HomeFragment())
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit()
            return true
        }
        return false
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

}
