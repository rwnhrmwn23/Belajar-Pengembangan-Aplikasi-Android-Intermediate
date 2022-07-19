package com.onedev.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.onedev.storyapp.core.data.source.local.entity.StoryEntity
import com.onedev.storyapp.databinding.LayoutListStoryBinding

class StoryAdapter :
    PagingDataAdapter<StoryEntity, StoryAdapter.HomeViewHolder>(DIFF_CALLBACK) {

    var onItemClick: ((StoryEntity, ImageView, TextView, TextView) -> Unit)? = null

    inner class HomeViewHolder(private val binding: LayoutListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryEntity) {
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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(
                oldItem: StoryEntity,
                newItem: StoryEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryEntity,
                newItem: StoryEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}