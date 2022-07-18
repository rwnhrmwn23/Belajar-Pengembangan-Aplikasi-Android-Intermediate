package com.onedev.storyapp.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.onedev.storyapp.ui.fragment.etc.ProgressDialogFragment
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

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

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun putPreference(context: Context, key: String, value: String) {
    preferenceManager = PreferenceManager(context)
    preferenceManager.putString(key, value)
}

fun getPreference(context: Context, key: String): String {
    preferenceManager = PreferenceManager(context)
    return preferenceManager.getString(key)
}

fun putListPreference(context: Context, key: String, value: List<String>) {
    preferenceManager = PreferenceManager(context)
    preferenceManager.putListString(key, value)
}

fun getListPreference(context: Context, key: String): MutableSet<String>? {
    preferenceManager = PreferenceManager(context)
    return preferenceManager.getListString(key)
}

fun clearPreference(context: Context) {
    preferenceManager = PreferenceManager(context)
    return preferenceManager.clearPreferences()
}

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun createTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

fun reduceFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > 1000000)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}