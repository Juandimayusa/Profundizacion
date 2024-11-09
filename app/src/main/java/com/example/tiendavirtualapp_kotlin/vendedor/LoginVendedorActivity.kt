package com.example.tiendavirtualapp_kotlin.vendedor


import android.app.ProgressDialog
import android.content.Intent

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast


import androidx.appcompat.app.AppCompatActivity


import com.example.tiendavirtualapp_kotlin.databinding.ActivityLoginVendedorBinding


import com.google.firebase.auth.FirebaseAuth

class LoginVendedorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginVendedorBinding
    private lateinit var  firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()
        progressDialog = ProgressDialog (this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnLoginV.setOnClickListener {
            ValidarInfo()

        }

        binding.tvRegistrarV.setOnClickListener {
            startActivity(Intent(applicationContext, RegistroVendedorActivity::class.java))

        }




    }

    private var correo=""
    private var contrasenia=""

    private fun ValidarInfo() {
        correo=binding.etEmailV.text.toString().trim()
        contrasenia=binding.etPassword.text.toString().trim()

        if (correo.isEmpty()){
            binding.etEmailV.error="Ingrese el correo"
            binding.etEmailV.requestFocus()
            }else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            binding.etEmailV.error="Ingrese un correo valido"
            binding.etEmailV.requestFocus()
        }else if (contrasenia.isEmpty()){
            binding.etPassword.error="Ingrese la contraseña"
            binding.etPassword.requestFocus()
        }
            else{
                loginVendedor ()
        }

    }

    private fun loginVendedor() {
        progressDialog.setMessage("Ingresando")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(correo,contrasenia)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this,MainActivityVendedor::class.java))
                finishAffinity()
                Toast.makeText(
                    this,
                    "Bienvenido(a)",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "No se pudo iniciar sesion debido a ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }





    }
}