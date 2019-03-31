package id.langgan.android.utility

import java.util.*

object Vars {

    const val PREF_AUTH = "pref:auth"
    const val PREF_AUTH_KEY = "auth"
    const val PREF_PROFILE = "pref:profile"
    const val PREF_PROFILE_KEY = "profile"

    fun getLocale(): String {
        var locale = Locale.getDefault().language
        if (locale == "in")
            locale = "id"
        else
            locale = "en"

        return locale
    }

}