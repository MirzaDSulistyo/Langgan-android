package id.langgan.android.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import id.langgan.android.BuildConfig
import id.langgan.android.R
import id.langgan.android.model.Auth
import id.langgan.android.utility.Vars
import timber.log.Timber

class MainActivity : AppCompatActivity() {

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
    }
}
