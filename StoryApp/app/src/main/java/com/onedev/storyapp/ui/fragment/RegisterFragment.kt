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
import com.onedev.storyapp.core.data.source.remote.response.Register
import com.onedev.storyapp.core.viewmodel.MainViewModel
import com.onedev.storyapp.databinding.FragmentRegisterBinding
import com.onedev.storyapp.ui.activity.MainActivity
import com.onedev.storyapp.utils.*
import com.onedev.storyapp.utils.Constant.USER_ID
import com.onedev.storyapp.utils.Constant.USER_NAME
import com.onedev.storyapp.utils.Constant.USER_TOKEN
import org.koin.android.viewmodel.ext.android.viewModel


class RegisterFragment : Fragment(), View.OnClickListener {

    private val mainViewModel: MainViewModel by viewModel()
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linkLoginNow()
        validationInputUser()

        binding?.imgBack?.setOnClickListener(this)
        binding?.btnLogin?.setOnClickListener(this)
    }

    private fun validationInputUser() {
        binding?.apply {
            edtName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    setEnabledButton()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

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
                    edtName.text.toString().isNotEmpty() && edtEmail.text.toString().isNotEmpty() &&
                    edtPassword.text.toString().isNotEmpty()
        }
    }

    private fun linkLoginNow() {
        val clickLogin = object : ClickableSpan() {
            override fun onClick(textView: View) {
                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(requireContext(), R.color.color_primary)
            }
        }

        val textLoginNow = SpannableString(getString(R.string.login))
        textLoginNow.setSpan(clickLogin, 0, getString(R.string.login).length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding?.tvRegister?.apply {
            text = TextUtils.concat(
                getString(R.string.have_account_login), " ",
                textLoginNow
            )
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun login(email: String, password: String) {
        binding?.apply {
            val body = Login.Request(email, password)
            mainViewModel.login(body).observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    when (response) {
                        is Resource.Loading -> { }
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

    override fun onClick(v: View?) {
        when (v) {
            binding?.imgBack -> {
                navigateUp(v)
            }
            binding?.btnLogin -> {
                binding?.apply {
                    val name = edtName.text.toString()
                    val email = edtEmail.text.toString()
                    val password = edtPassword.text.toString()

                    val body = Register.Request(name, email, password)
                    mainViewModel.register(body).observe(viewLifecycleOwner) { response ->
                        if (response != null) {
                            when (response) {
                                is Resource.Loading -> {
                                    this@RegisterFragment.showLoading()
                                }
                                is Resource.Success -> {
                                    login(email, password)
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