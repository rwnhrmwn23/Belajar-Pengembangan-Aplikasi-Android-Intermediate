package com.onedev.storyapp.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.onedev.storyapp.R
import com.onedev.storyapp.core.data.Resource
import com.onedev.storyapp.core.data.source.remote.response.Login
import com.onedev.storyapp.core.viewmodel.MainViewModel
import com.onedev.storyapp.databinding.FragmentLoginBinding
import com.onedev.storyapp.ui.activity.MainActivity
import com.onedev.storyapp.utils.Constant.USER_ID
import com.onedev.storyapp.utils.Constant.USER_NAME
import com.onedev.storyapp.utils.Constant.USER_TOKEN
import com.onedev.storyapp.utils.hideLoading
import com.onedev.storyapp.utils.putPreference
import com.onedev.storyapp.utils.showLoading
import com.onedev.storyapp.utils.showSnackBar
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(), View.OnClickListener {

    private val mainViewModel: MainViewModel by viewModel()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linkRegisterNow()
        validationInputUser()

        binding?.btnLogin?.setOnClickListener(this)
    }

    private fun validationInputUser() {
        binding?.apply {
            edtEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setEnabledButton()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            edtPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setEnabledButton()
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun setEnabledButton() {
        binding?.apply {
            btnLogin.isEnabled = edtEmail.error == null && edtPassword.error == null &&
                    edtEmail.text.toString().isNotEmpty() && edtPassword.text.toString().isNotEmpty()
        }
    }

    private fun linkRegisterNow() {
        val clickRegister = object : ClickableSpan() {
            override fun onClick(textView: View) {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(requireContext(), R.color.color_primary)
            }
        }

        val textRegisterNow = SpannableString(getString(R.string.register))
        textRegisterNow.setSpan(clickRegister, 0, getString(R.string.register).length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding?.tvRegister?.apply {
            text = TextUtils.concat(
                getString(R.string.not_have_account_register), " ",
                textRegisterNow
            )
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v) {
            binding?.btnLogin -> {
                binding?.apply {
                    val email = edtEmail.text.toString()
                    val password = edtPassword.text.toString()

                    val body = Login.Request(email, password)
                    mainViewModel.login(body).observe(viewLifecycleOwner) { response ->
                        if (response != null) {
                            when (response) {
                                is Resource.Loading -> {
                                    this@LoginFragment.showLoading()
                                }
                                is Resource.Success -> {
                                    hideLoading()
                                    response.data?.loginResult?.apply {
                                        putPreference(requireContext(), USER_ID, userId)
                                        putPreference(requireContext(), USER_NAME, name)
                                        putPreference(requireContext(), USER_TOKEN, getString(R.string.token, token))

                                        startActivity(Intent(requireActivity(), MainActivity::class.java))
                                        requireActivity().finish()
                                    }
                                }
                                is Resource.Error -> {
                                    hideLoading()
                                    requireView().showSnackBar(response.message.toString())
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}