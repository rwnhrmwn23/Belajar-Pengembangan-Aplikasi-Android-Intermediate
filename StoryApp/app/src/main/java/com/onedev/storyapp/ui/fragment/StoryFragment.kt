package com.onedev.storyapp.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.onedev.storyapp.R
import com.onedev.storyapp.core.data.Resource
import com.onedev.storyapp.core.viewmodel.MainViewModel
import com.onedev.storyapp.databinding.FragmentStoryBinding
import com.onedev.storyapp.ui.adapter.StoryAdapter
import com.onedev.storyapp.utils.*
import org.koin.android.viewmodel.ext.android.viewModel

class StoryFragment : Fragment() {

    private lateinit var storyAdapter: StoryAdapter
    private val mainViewModel: MainViewModel by viewModel()
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
        setHasOptionsMenu(true)

        storyAdapter = StoryAdapter()
        binding?.rvStory?.apply {
            setHasFixedSize(true)
            adapter = storyAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }

        storyAdapter.onItemClick = {
            val action = StoryFragmentDirections.actionStoryFragmentToStoryDetailFragment(it)
            findNavController().navigate(action)
        }

        binding?.toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_add -> {
                    findNavController().navigate(StoryFragmentDirections.actionMainFragmentToAddStoryFragment())
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
            mainViewModel.story().observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    when (response) {
                        is Resource.Loading -> {
                            this@StoryFragment.showLoading()
                            lottieError.gone()
                            tvError.gone()
                            rvStory.gone()
                        }
                        is Resource.Success -> {
                            hideLoading()
                            response.data?.listStory?.apply {
                                storyAdapter.setListData(this)
                                lottieError.gone()
                                tvError.gone()
                                rvStory.visible()
                            }
                        }
                        is Resource.Error -> {
                            hideLoading()
                            tvError.text = response.message.toString()
                            lottieError.visible()
                            tvError.visible()
                            rvStory.gone()
                        }
                    }
                }
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