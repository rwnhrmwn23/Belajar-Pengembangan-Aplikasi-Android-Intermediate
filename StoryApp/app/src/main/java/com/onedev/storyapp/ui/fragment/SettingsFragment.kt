package com.onedev.storyapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.system.Os.accept
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.onedev.storyapp.R
import com.onedev.storyapp.databinding.FragmentProgressDialogBinding
import com.onedev.storyapp.databinding.FragmentSettingBinding
import com.onedev.storyapp.ui.activity.OnBoardingActivity
import com.onedev.storyapp.utils.clearPreference
import com.onedev.storyapp.utils.navigateUp
import com.onedev.storyapp.utils.putPreference

class SettingsFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.imgBack?.setOnClickListener(this)
        binding?.tvLogOut?.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v) {
            binding?.imgBack -> {
                navigateUp(v)
            }
            binding?.tvLogOut -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.logout))
                    .setMessage(getString(R.string.logout_description))
                    .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                        dialog.cancel()
                    }
                    .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                        dialog.cancel()
                        clearPreference(requireContext())
                        startActivity(Intent(requireActivity(), OnBoardingActivity::class.java))
                        requireActivity().finish()
                    }
                    .show()
            }
        }
    }
}