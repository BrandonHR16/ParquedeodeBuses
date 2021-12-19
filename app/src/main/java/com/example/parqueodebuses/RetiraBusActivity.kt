package com.example.parqueodebuses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.parqueodebuses.databinding.ActivityMapBinding
import com.example.parqueodebuses.databinding.ActivityRetiraBusBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RetiraBusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRetiraBusBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRetiraBusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userInfo()

        binding.button2.setOnClickListener {

        }
    }

    private fun userInfo() {
        val useraunth = FirebaseAuth.getInstance().currentUser
        val db = Firebase.firestore
        val info = db.collection("Usuarios").document(useraunth!!.uid).get()
        info.addOnSuccessListener {
            val numeroBus = it.get("NumBus")

            val bus = db.collection("Buses").document(numeroBus.toString()).get()
            bus.addOnSuccessListener {
                val placa = it.get("Placa")
                val estacionamiento = it.get("Estacionamiento")
                binding.txtNumPlaca.text = placa.toString()
                binding.txtNumBus.text = estacionamiento.toString()
            }
        }

    }
}