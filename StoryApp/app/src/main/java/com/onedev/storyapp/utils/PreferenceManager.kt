package com.onedev.storyapp.utils

import android.content.Context
import android.content.SharedPreferences


class PreferenceManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Constant.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun putString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String {
        return sharedPreferences.getString(key, "").toString()
    }

    fun putListString(key: String, value: List<String>) {
        val set: MutableSet<String> = HashSet()
        set.addAll(value)

        val editor = sharedPreferences.edit()
        editor.putStringSet(key, set)
        editor.apply()
    }

    fun getListString(key: String): MutableSet<String>? {
        return sharedPreferences.getStringSet(key, null)
    }

    fun clearPreferences() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}