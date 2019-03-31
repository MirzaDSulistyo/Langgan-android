package id.langgan.android.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import id.langgan.android.R
import id.langgan.android.databinding.ActivityStoreBinding

class StoreActivity : AppCompatActivity() {

    lateinit var binding: ActivityStoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_store)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_store)

        // Handle Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.store)

        binding.products.setOnClickListener {
            startActivity(Intent(this, ProductActivity::class.java))
        }

        binding.box.setOnClickListener {
            startActivity(Intent(this, BoxActivity::class.java))
        }
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
}
