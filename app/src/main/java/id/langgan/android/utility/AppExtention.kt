package id.langgan.android.utility

import android.app.Activity
import android.content.Context
import android.view.View
import com.google.gson.Gson
import com.kaopiz.kprogresshud.KProgressHUD
import id.langgan.android.model.Auth
import id.langgan.android.model.Profile

fun View.goVisible() {
    visibility = View.VISIBLE
}

fun View.goInvisible() {
    visibility = View.INVISIBLE
}

fun View.goGone() {
    visibility = View.GONE
}

fun Context?.setHud(): KProgressHUD {
    return KProgressHUD.create(this)
        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        .setLabel("Please wait")
        .setCancellable(false)
        .setAnimationSpeed(2)
        .setDimAmount(0.5f)
}

fun Activity.getAuth(): Auth? {
    val userPrefs = this.getSharedPreferences(Vars.PREF_AUTH, Context.MODE_PRIVATE)

    val userStr = userPrefs.getString(Vars.PREF_AUTH_KEY, "") ?: return null

    return Gson().fromJson(userStr, Auth::class.java)
}

fun Activity.setAuth(auth: Auth?): Boolean {
    val userPrefs = this.getSharedPreferences(Vars.PREF_AUTH, Context.MODE_PRIVATE)
    val editor = userPrefs.edit()
    editor.putString(Vars.PREF_AUTH_KEY, Gson().toJson(auth))
    editor.apply()

    if (auth == null)
        return false

    return true
}

fun Activity.setProfile(profile: Profile?): Boolean {
    if (profile == null)
        return false

    val userPrefs = this.getSharedPreferences(Vars.PREF_PROFILE, Context.MODE_PRIVATE)
    val editor = userPrefs.edit()
    editor.putString(Vars.PREF_PROFILE_KEY, Gson().toJson(profile))
    editor.apply()

    return true
}

fun Activity.getProfile(): Profile? {
    val profilePrefs = this.getSharedPreferences(Vars.PREF_PROFILE, Context.MODE_PRIVATE)

    val profileStr = profilePrefs.getString(Vars.PREF_PROFILE_KEY, "") ?: return null

    return Gson().fromJson(profileStr, Profile::class.java)
}