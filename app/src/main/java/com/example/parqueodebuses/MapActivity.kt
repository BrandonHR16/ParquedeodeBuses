package com.example.parqueodebuses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.parqueodebuses.databinding.ActivityMapBinding
import com.google.android.gms.maps.OnMapReadyCallback

class MapActivity : AppCompatActivity() {

    lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapView3.onCreate(savedInstanceState)
        binding.mapView3.getMapAsync(OnMapReadyCallback {
            it.setOnMapClickListener {

            }
    })



    }
}