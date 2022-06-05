package com.onedev.storyapp.utils

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.onedev.storyapp.R

@BindingAdapter("android:loadImage")
fun ImageView.loadImage(url: Any?) {
    try {
        val options = RequestOptions()
            .placeholder(getProgressDrawable(context))
            .error(R.drawable.ic_baseline_broken_image)
        Glide.with(context)
            .setDefaultRequestOptions(options)
            .load(url)
            .into(this)
    } catch (e: Exception) {
        e.toString()
    }
}

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10F
        centerRadius = 50F
        start()
    }
}