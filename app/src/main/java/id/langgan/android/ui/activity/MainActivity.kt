package id.langgan.android.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import id.langgan.android.BuildConfig
import id.langgan.android.R
import id.langgan.android.model.Auth
import id.langgan.android.ui.fragment.*
import id.langgan.android.utility.Vars
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val userPrefs = this.getSharedPreferences(Vars.PREF_AUTH, Context.MODE_PRIVATE)
        val userStr = userPrefs.getString(Vars.PREF_AUTH_KEY, "")

        if (userStr.isNullOrEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            val auth = Gson().fromJson(userStr, Auth::class.java)
            Timber.d("user : %s", auth.token)
            Timber.d("user : %s", auth.user?.email)
        }

        bottom_navigation.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                var fragment : Fragment? = null

                when (item.itemId) {
                    R.id.home_menu -> fragment = HomeFragment()
                    R.id.favorite_menu -> fragment = FavoriteFragment()
                    R.id.subscriptions_menu -> fragment = SubscriptionsFragment()
                    R.id.inbox_menu -> fragment = InboxFragment()
                    R.id.account_menu -> fragment = ProfileFragment()
                }
                return loadFragment(fragment)
            }
        })

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
