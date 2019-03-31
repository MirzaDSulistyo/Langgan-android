package id.langgan.android.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import id.langgan.android.R
import id.langgan.android.ui.fragment.BoxFragment
import id.langgan.android.ui.fragment.ProductFragment
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class BoxActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_box)

        // Handle Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.products)

        loadFragment(BoxFragment())
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

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}
