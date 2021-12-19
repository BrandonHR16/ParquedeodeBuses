package com.example.parqueodebuses

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.parqueodebuses.databinding.ActivityDejarBusBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class DejarBusActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDejarBusBinding
    var tvLatitud = ""
    var tvLongitud = ""
    var repetido = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDejarBusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinner: Spinner = findViewById(R.id.spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.tiposE,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        infoUser()

        binding.EstacionarBus.setOnClickListener{
            posicionActualGPS()
            verificaEstacionamiento()
        }

        binding.varmapa.setOnClickListener{
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
    }

    private fun infoUser() {
        val useraunth = FirebaseAuth.getInstance().currentUser
        val db = Firebase.firestore
        val info = db.collection("Usuarios").document(useraunth!!.uid).get()
        info.addOnSuccessListener {
            val numeroBus = it.get("NumBus")

           val bus = db.collection("Buses").document(numeroBus.toString()).get()
            bus.addOnSuccessListener {
                val placa = it.get("Placa")
                val estacionamiento = it.get("Estacionamiento")
                binding.txtplaca.text = placa.toString()
                binding.txtNumeroBus.text = estacionamiento.toString()
            }
        }
    }


    private fun verificaEstacionamiento(){
        val db = Firebase.firestore
        val info = db.collection("Buses").get()
        //recorreo la coleccion de buses
        info.addOnSuccessListener {
            for (document in it) {
                val estacionamiento = document.get("Estacionamiento")
                if (estacionamiento.toString() == binding.txtNumeroEs.text.toString()){
                    Toast.makeText(this, "El estacinamiento se encuentra ocupado", Toast.LENGTH_LONG).show()
                }else if( binding.txtNumeroEs.text.toString().toInt() < 165 && binding.txtNumeroEs.text.toString().toInt() > 0){
                    GuardarDatosFireBase()

                }else{
                    Toast.makeText(this, "El estacinamiento no existe", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun GuardarDatosFireBase() {
        val useraunth = FirebaseAuth.getInstance().currentUser
        val db = Firebase.firestore
        val info = db.collection("Usuarios").document(useraunth!!.uid).get()
        info.addOnSuccessListener {
        val numeroBus = it.get("NumBus")
            val Buses = hashMapOf(
                "Cords" to "$tvLongitud,$tvLatitud",
                "Estacionamiento" to binding.txtNumeroEs.text.toString(),
                "Tipo de estacionamiento" to binding.spinner.selectedItem.toString()
            )
           db.collection("Buses").document(numeroBus.toString()).update(Buses as Map<String, Any>).addOnSuccessListener {
               infoUser()
               Toast.makeText(this, "Informacion actualizada", Toast.LENGTH_SHORT).show()
           }
               .addOnFailureListener{
                   Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
               }
        }
    }



    private var conPermisos = true
    private fun posicionActualGPS() {
        tvLatitud = ""
        tvLongitud = ""
        val fusedLocation: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 105
            )
        }
        if (conPermisos) {
            fusedLocation.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        tvLatitud = "${location.latitude}"
                         tvLongitud= "${location.longitude}"
                    } else {
                         tvLatitud = "No se pudo obtener la ubicación"
                         tvLongitud = "No se pudo obtener la ubicación"
                    }
                }
        }

    }



}