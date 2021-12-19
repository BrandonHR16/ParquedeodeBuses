package com.example.parqueodebuses

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.parqueodebuses.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        infoUser()


        binding.Ebus.setOnClickListener{
            startActivity(Intent(this, DejarBusActivity::class.java))
        }

               binding.btnLlamar.setOnClickListener{
                   llamarservcio()
               }

        binding.button.setOnClickListener{
            startActivity(Intent(this, RetiraBusActivity::class.java))
        }

    }

    private fun llamarservcio() {



        if(checkSelfPermission(android.Manifest.permission.CALL_PHONE) ==
            PackageManager.PERMISSION_GRANTED){
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:89511592")
            startActivity(intent)
        }else{
            requestPermissions(arrayOf(android.Manifest.permission.CALL_PHONE), 1)
        }
    }

    private fun infoUser() {
        val useraunth = FirebaseAuth.getInstance().currentUser
        val db = Firebase.firestore
        val info = db.collection("Usuarios").document(useraunth!!.uid).get()
        info.addOnSuccessListener {
            val Cedula = it.get("Cedula").toString()
            val Nombre = it.get("Nombre").toString()
            val NombreRuta = it.get("NombreRuta").toString()
            val HoraInicio = it.get("HoraInicio").toString()
            val HoraInicioPC = it.get("HoraIncioPC").toString()

            binding.NombreCon.setText(Nombre)
            binding.Cedula.setText(Cedula)
            binding.Ruta.setText(NombreRuta)
            binding.Ingreso.setText(HoraInicio)
            binding.Pcarrera.setText(HoraInicioPC)

        }

    }
}