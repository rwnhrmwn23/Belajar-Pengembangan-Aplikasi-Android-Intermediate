package com.onedev.storyapp.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.onedev.storyapp.ui.fragment.ProgressDialogFragment

private lateinit var mOptionDialogFragment: ProgressDialogFragment
private lateinit var preferenceManager: PreferenceManager

fun Fragment.showLoading() {
    mOptionDialogFragment = ProgressDialogFragment()
    val mFragmentManager = this.childFragmentManager
    mOptionDialogFragment.show(
        mFragmentManager,
        ProgressDialogFragment::class.java.simpleName
    )
}

fun hideLoading() {
    mOptionDialogFragment.dismiss()
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun navigateUp(view: View?) {
    val controller = view?.let { Navigation.findNavController(view) }
    controller?.navigateUp()
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun putPreference(context: Context, key: String, value: String) {
    preferenceManager = PreferenceManager(context)
    preferenceManager.putString(key, value)
}

fun getPreference(context: Context, key: String): String {
    preferenceManager = PreferenceManager(context)
    return preferenceManager.getString(key)
}

fun clearPreference(context: Context) {
    preferenceManager = PreferenceManager(context)
    return preferenceManager.clearPreferences()
}