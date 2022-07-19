package com.onedev.storyapp.ui.fragment.maps

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.onedev.storyapp.R
import com.onedev.storyapp.core.data.Resource
import com.onedev.storyapp.databinding.FragmentMapsBinding
import com.onedev.storyapp.utils.hideLoading
import com.onedev.storyapp.utils.navigateUp
import com.onedev.storyapp.utils.showLoading
import org.koin.android.viewmodel.ext.android.viewModel


class MapsFragment : Fragment() {

    private lateinit var mMap: GoogleMap
    private val storyLocationViewModel: StoryLocationViewModel by viewModel()
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding?.toolbar?.apply {
            setNavigationOnClickListener {
                navigateUp(it)
            }
        }
    }

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mMap.setOnPoiClickListener { point ->
            val poiMarker = mMap.addMarker(
                MarkerOptions()
                    .position(point.latLng)
                    .title(point.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )
            poiMarker?.showInfoWindow()
        }

        loadStory()
        getMyLocation()
        setCustomMapStyle()
    }

    private fun setCustomMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }

    private fun loadStory() {
        binding?.apply {
            storyLocationViewModel.storyMap(page = 1, size = 10, location = 1).observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    when (response) {
                        is Resource.Loading -> {
                            this@MapsFragment.showLoading()
                        }
                        is Resource.Success -> {
                            hideLoading()
                            val listStory = response.data?.listStory
                            if (listStory != null) {
                                for (i in listStory) {
                                    val listLatLng = LatLng(i.lat ?: 0.0,  i.lon ?: 0.0)
                                    mMap.addMarker(
                                        MarkerOptions()
                                            .position(listLatLng)
                                            .title(i.name)
                                            .snippet(i.description)
                                    )
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(listLatLng, 15F))
                                }
                            }
                        }
                        is Resource.Error -> {
                            hideLoading()
                        }
                    }
                }
            }
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            getMyLocation()
        }
    }

    companion object {
        private const val TAG = "MapsFragment"
    }
}