package com.onedev.storyapp.ui.fragment.story

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.onedev.storyapp.R
import com.onedev.storyapp.core.data.source.remote.response.Story
import com.onedev.storyapp.databinding.FragmentStoryBinding
import com.onedev.storyapp.ui.adapter.LoadingStateAdapter
import com.onedev.storyapp.ui.adapter.StoryAdapter
import com.onedev.storyapp.utils.gone
import com.onedev.storyapp.utils.hideLoading
import com.onedev.storyapp.utils.showLoading
import com.onedev.storyapp.utils.visible
import org.koin.android.viewmodel.ext.android.viewModel

class StoryFragment : Fragment() {

    private lateinit var storyAdapter: StoryAdapter
    private val storyViewModel: StoryViewModel by viewModel()
    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoryBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storyAdapter = StoryAdapter()
        binding?.rvStory?.apply {
            setHasFixedSize(true)
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                }
            )
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }

        storyAdapter.onItemClick =
            { dataStory: Story.GetResponse.DataStory, imgStory: ImageView, tvNameStory: TextView, tvDescriptionStory: TextView ->
                val extras = FragmentNavigatorExtras(
                    imgStory to "storyImg",
                    tvNameStory to "storyName",
                    tvDescriptionStory to "storyDescription"
                )
                val action =
                    StoryFragmentDirections.actionStoryFragmentToStoryDetailFragment(dataStory)
                findNavController().navigate(action, extras)
            }

        binding?.toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_add -> {
                    findNavController().navigate(StoryFragmentDirections.actionMainFragmentToAddStoryFragment())
                }
                R.id.menu_maps -> {
                    findNavController().navigate(StoryFragmentDirections.actionStoryFragmentToMapsFragment())
                }
                R.id.menu_setting -> {
                    findNavController().navigate(StoryFragmentDirections.actionMainFragmentToSettingsFragment())
                }
            }
            true
        }
    }

    private fun loadStory() {
        binding?.apply {
            this@StoryFragment.showLoading()
            storyViewModel.story(page = 1, size = 10, location = 0)
                .observe(viewLifecycleOwner) { response ->
                    if (response != null) {
                        storyAdapter.submitData(lifecycle, response)
                        rvStory.visible()
                        lottieError.gone()
                        tvError.gone()
                    } else {
                        lottieError.visible()
                        tvError.visible()
                        rvStory.gone()
                    }
                    hideLoading()
                }
        }
    }

    override fun onResume() {
        super.onResume()
        loadStory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}