package com.example.parqueodebuses

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.parqueodebuses.databinding.ActivityLogInBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase


class LogInActivity : AppCompatActivity() {
  private lateinit var binding: ActivityLogInBinding
  private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()


        binding.btnInicio.setOnClickListener {inicioSesion()}

//

}

    private fun inicioSesion() {
        val email = binding.txtCorreo.text.toString()
        val email2 = email + "@grupoatdcr.com"
        val password = binding.txtContra.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email2, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            Toast.makeText(this, getText(R.string.ErrorLogIn), Toast.LENGTH_SHORT).show()
        }
    }
}
