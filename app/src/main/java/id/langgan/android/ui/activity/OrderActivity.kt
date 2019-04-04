package id.langgan.android.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import id.langgan.android.R
import id.langgan.android.ui.adapter.TabAdapter
import id.langgan.android.ui.fragment.NewOrderFragment
import id.langgan.android.ui.fragment.SubscriberFragment
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class OrderActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        // Handle Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Title"

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = findViewById<ViewPager>(R.id.view_pager_tabs)
        val adapter = TabAdapter(supportFragmentManager)

        val newOrderFragment = NewOrderFragment()
        val subscriberFragment = SubscriberFragment()

        adapter.addFragment(newOrderFragment, "New Order")
        adapter.addFragment(subscriberFragment, "Subscriber")

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
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