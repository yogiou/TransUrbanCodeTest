package jie.wen.transurbancodetest.model.store

import android.app.Activity
import android.content.Context

// save user's favourite starship using SharedPreference, with the key starship's url
object FavouriteStoreSharePrefs {
    fun favourite(key: String, activity: Activity?) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean(key, true)
            apply()
        }
    }

    fun unFavourite(key: String, activity: Activity?) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean(key, false)
            apply()
        }
    }

    fun isFavourite(key: String, activity: Activity?): Boolean {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return false
        return sharedPref.getBoolean(key, false)
    }
}