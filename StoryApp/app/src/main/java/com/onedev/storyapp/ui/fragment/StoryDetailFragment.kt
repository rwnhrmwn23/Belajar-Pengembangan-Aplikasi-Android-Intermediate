package com.onedev.storyapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.onedev.storyapp.core.data.source.remote.response.Story
import com.onedev.storyapp.databinding.FragmentStoryDetailBinding
import com.onedev.storyapp.utils.navigateUp

class StoryDetailFragment : Fragment() {
    private var _binding: FragmentStoryDetailBinding? = null
    private val binding get() = _binding
    private val args: StoryDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoryDetailBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.toolbar?.setNavigationOnClickListener { navigateUp(it) }

        populateView(args.data)
    }

    private fun populateView(data: Story.GetResponse.DataStory) {
        binding?.apply {
            viewmodel = data
            executePendingBindings()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}