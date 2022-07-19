package com.onedev.storyapp.ui.fragment.story

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.onedev.storyapp.R
import com.onedev.storyapp.core.data.Resource
import com.onedev.storyapp.databinding.BottomSheetAddPhotoBinding
import com.onedev.storyapp.databinding.FragmentStoryAddBinding
import com.onedev.storyapp.utils.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File


class StoryAddFragment : Fragment(), View.OnClickListener {

    private val storyViewModel: StoryViewModel by viewModel()
    private var _binding: FragmentStoryAddBinding? = null
    private val binding get() = _binding
    private var getFile: File? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoryAddBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.toolbar?.setNavigationOnClickListener { navigateUp(it) }

        binding?.imgAddPhoto?.setOnClickListener(this)
        binding?.btnUpload?.setOnClickListener(this)
        binding?.switchShareMyLocation?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getMyLocation()
            } else {
                latitude = null
                longitude = null
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

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

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    Log.d(TAG, "getMyLocation: $latitude, $longitude")
                } else {
                    binding?.switchShareMyLocation?.isChecked = false
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissions ->
        Log.d(TAG, "$permissions")
        when (permissions) {
            true -> {
                getMyLocation()
            }
            else -> {
                Snackbar
                    .make(requireView(), getString(R.string.access_location_off), Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.turn_on)) {
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    .show()
                binding?.switchShareMyLocation?.isChecked = false
            }
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
                val binding = BottomSheetAddPhotoBinding.inflate(layoutInflater)

                binding.fabCamera.setOnClickListener {
                    dialog.cancel()
                    openCamera()
                }

                binding.fabGallery.setOnClickListener {
                    dialog.cancel()
                    openGallery()
                }

                dialog.setCancelable(true)
                dialog.setContentView(binding.root)
                dialog.show()
            }
            binding?.btnUpload -> {
                if (getFile != null) {
                    binding?.apply {
                        val file = reduceFileImage(getFile as File)
                        val description = edtDescription.text.toString()
                        val requestImageFile =
                            file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        val imageMultipart: MultipartBody.Part =
                            MultipartBody.Part.createFormData(
                                "photo",
                                file.name,
                                requestImageFile
                            )
                        if (description.isNotEmpty()) {
                            val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
                            val latitudeRequestBody = if (latitude != null)
                                latitude.toString().toRequestBody("text/plain".toMediaType()) else null

                            val longitudeRequestBody = if (longitude != null)
                                longitude.toString().toRequestBody("text/plain".toMediaType()) else null

                            storyViewModel.story(
                                imageMultipart,
                                descriptionRequestBody,
                                latitudeRequestBody,
                                longitudeRequestBody
                            ).observe(viewLifecycleOwner) { response ->
                                if (response != null) {
                                    when (response) {
                                        is Resource.Loading -> {
                                            this@StoryAddFragment.showLoading()
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

    companion object {
        private const val TAG = "StoryAddFragment"
    }
}