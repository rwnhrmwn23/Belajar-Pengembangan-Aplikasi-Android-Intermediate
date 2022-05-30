package com.onedev.storyapp.ui.fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.onedev.storyapp.R
import com.onedev.storyapp.core.data.Resource
import com.onedev.storyapp.core.viewmodel.MainViewModel
import com.onedev.storyapp.databinding.FragmentAddStoryBinding
import com.onedev.storyapp.utils.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File


class AddStoryFragment : Fragment(), View.OnClickListener {

    private val mainViewModel: MainViewModel by viewModel()
    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding

    private var getFile: File? = null
    private lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddStoryBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.toolbar?.setNavigationOnClickListener { navigateUp(it) }

        binding?.imgAddPhoto?.setOnClickListener(this)
        binding?.btnUpload?.setOnClickListener(this)

        checkPermissionCamera()
    }

    private fun checkPermissionCamera() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    requireContext().showToast(getString(R.string.permission_granted))
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    requireContext().showToast(getString(R.string.permission_reject))
                    requireActivity().finish()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                }
            }).check()
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            getFile = myFile

            binding?.imgAddPhoto?.setImageURI(selectedImg)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            binding?.imgAddPhoto?.setImageBitmap(result)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)
        createCustomTempFile(requireActivity().application).also {
            val photoURI = FileProvider.getUriForFile(
                requireContext(),
                "com.onedev.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v) {
            binding?.imgAddPhoto -> {
                val dialog = BottomSheetDialog(requireContext())
                val view = layoutInflater.inflate(R.layout.bottom_sheet_add_photo, null)
                val fabCamera = view.findViewById<FloatingActionButton>(R.id.fab_camera)
                val fabGallery = view.findViewById<FloatingActionButton>(R.id.fab_gallery)

                fabCamera.setOnClickListener {
                    dialog.cancel()
                    openCamera()
                }

                fabGallery.setOnClickListener {
                    dialog.cancel()
                    openGallery()
                }

                dialog.setCancelable(true)
                dialog.setContentView(view)
                dialog.show()
            }
            binding?.btnUpload -> {
                if (getFile != null) {
                    binding?.apply {
                        val file = reduceFileImage(getFile as File)
                        val description = edtDescription.text.toString()
                        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                            "photo",
                            file.name,
                            requestImageFile
                        )
                        if (description.isNotEmpty()) {
                            val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
                            mainViewModel.story(imageMultipart, descriptionRequestBody).observe(viewLifecycleOwner) { response ->
                                if (response != null) {
                                    when (response) {
                                        is Resource.Loading -> {
                                            this@AddStoryFragment.showLoading()
                                        }
                                        is Resource.Success -> {
                                            hideLoading()
                                            navigateUp(v)
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
                } else {
                    requireContext().showToast(getString(R.string.invalid_image))
                }
            }
        }
    }
}