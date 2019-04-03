package id.langgan.android.utility

import android.content.Context
import android.content.SharedPreferences

class Prefs {

    val user: String
        get() = getUserStr()

    val profile: String
        get() = getProfileStr()

    var context: Context? = null

    private fun getUserStr(): String {
        val userPrefs: SharedPreferences = context?.getSharedPreferences(PREF_AUTH, Context.MODE_PRIVATE)!!
        return userPrefs.getString(PREF_AUTH_KEY, "")!!
    }

    private fun getProfileStr(): String {
        val userPrefs: SharedPreferences = context?.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE)!!
        return userPrefs.getString(PREF_PROFILE_KEY, "")!!
    }

    fun putUser(data: String?) {
        val userPrefs = context?.getSharedPreferences(PREF_AUTH, Context.MODE_PRIVATE)
        val editor = userPrefs?.edit()
        editor?.putString(PREF_AUTH_KEY, data)
        editor?.apply()
    }

    fun putStores(data: String) {
        val storesPrefs = context?.getSharedPreferences(PREF_STORES, Context.MODE_PRIVATE)
        val editor = storesPrefs?.edit()
        editor?.putString(PREF_STORES_KEY, data)
        editor?.apply()
    }

    companion object {
        const val PREF_AUTH = "pref:auth"
        const val PREF_AUTH_KEY = "auth"

        const val PREF_STORES = "pref:stores"
        const val PREF_STORES_KEY = "stores"

        const val PREF_PROFILE = "pref:profile"
        const val PREF_PROFILE_KEY = "profile"
    }
}