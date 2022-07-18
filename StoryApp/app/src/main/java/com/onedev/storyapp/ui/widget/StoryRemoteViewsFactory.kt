package com.onedev.storyapp.ui.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.onedev.storyapp.R
import com.onedev.storyapp.utils.Constant.LIST_STRING
import com.onedev.storyapp.utils.getListPreference

internal class StoryRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val datas = ArrayList<String>()

    override fun onCreate() {
        val listImg = getListPreference(context, LIST_STRING)
        listImg?.let {
            datas.addAll(it)
        }
    }

    override fun onDataSetChanged() {
        val listImg = getListPreference(context, LIST_STRING)
        listImg?.let {
            datas.addAll(it)
        }
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = datas.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.layout_item_widget).apply {
            val image = Glide.with(context)
                .asBitmap()
                .load(datas[position])
                .submit()
                .get()

            setImageViewBitmap(R.id.img_widget_story, image)
        }

        val extras = bundleOf(
            StoryWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}