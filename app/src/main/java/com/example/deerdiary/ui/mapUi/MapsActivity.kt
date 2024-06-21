package com.example.deerdiary.ui.mapUi

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.deerdiary.R
import com.example.deerdiary.ViewModelFactory
import com.example.deerdiary.databinding.ActivityMapsBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(this) }

    private val mViewModel: MapsViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment =
            supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        getData()
    }

    private fun getData() {
        mViewModel.processEvent(MapsEvent.ListStory(this))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mViewModel.listStory.observe(this) { story ->
            story.forEach {
                Log.d("MapsActivity", "onMapReady: ${it.name}")
                val lat = it.lat.toString().toDouble()
                val lon = it.lon.toString().toDouble()
                val name = it.name
                val description = it.description
                val location = LatLng(lat, lon)
                mMap.addMarker(MarkerOptions().position(location).title(name).snippet(description))
            }
        }
    }
}
