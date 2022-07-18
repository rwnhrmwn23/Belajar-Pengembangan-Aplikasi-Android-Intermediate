package com.onedev.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.onedev.storyapp.core.data.source.remote.response.Story
import com.onedev.storyapp.databinding.LayoutListStoryBinding

class StoryAdapter :
    PagingDataAdapter<Story.GetResponse.DataStory, StoryAdapter.HomeViewHolder>(DIFF_CALLBACK) {

    var onItemClick: ((Story.GetResponse.DataStory, ImageView, TextView, TextView) -> Unit)? = null

    inner class HomeViewHolder(private val binding: LayoutListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Story.GetResponse.DataStory) {
            binding.viewmodel = data
            binding.executePendingBindings()

            itemView.setOnClickListener {
                onItemClick?.invoke(data, binding.imgStory, binding.tvName, binding.tvDescription)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding =
            LayoutListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story.GetResponse.DataStory>() {
            override fun areItemsTheSame(
                oldItem: Story.GetResponse.DataStory,
                newItem: Story.GetResponse.DataStory
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Story.GetResponse.DataStory,
                newItem: Story.GetResponse.DataStory
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}