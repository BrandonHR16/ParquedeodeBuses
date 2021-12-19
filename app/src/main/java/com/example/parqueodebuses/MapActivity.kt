package com.example.parqueodebuses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.parqueodebuses.databinding.ActivityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap


import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

internal class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        val useraunth = FirebaseAuth.getInstance().currentUser
        val db = Firebase.firestore
        val info = db.collection("Usuarios").document(useraunth!!.uid).get()
        info.addOnSuccessListener {
            val numeroBus = it.get("NumBus")

            val bus = db.collection("Buses").document(numeroBus.toString()).get()
            bus.addOnSuccessListener {
                val cords = it.get("Cords")
               cords.toString().split(",")
                val lat = cords.toString().split(",")[1]
                val lon = cords.toString().split(",")[0]
                val latLng = LatLng(lat.toDouble(), lon.toDouble())
                mMap.addMarker(MarkerOptions().position(latLng).title("Bus"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            }
        }

    }
}