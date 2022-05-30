package com.onedev.storyapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onedev.storyapp.core.data.source.remote.response.Story
import com.onedev.storyapp.databinding.LayoutListStoryBinding

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.HomeViewHolder>() {

    private val datas = ArrayList<Story.GetResponse.DataStory>()
    var onItemClick: ((Story.GetResponse.DataStory) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(listData: List<Story.GetResponse.DataStory>?) {
        if (listData == null) return
        datas.clear()
        datas.addAll(listData)
        notifyDataSetChanged()
    }

    inner class HomeViewHolder(private val binding: LayoutListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Story.GetResponse.DataStory) {
            binding.viewmodel = data
            binding.executePendingBindings()

            itemView.setOnClickListener {
                onItemClick?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding =
            LayoutListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int = datas.size

}