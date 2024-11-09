package com.example.tiendavirtualapp_kotlin.vendedor


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tiendavirtualapp_kotlin.Constantes
import com.example.tiendavirtualapp_kotlin.databinding.ActivityRegistroVendedorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class RegistroVendedorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroVendedorBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistroVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()
        progressDialog =ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.bntRegistrarV.setOnClickListener {
            validarInforacion()
        }

    }
        private var nombres = ""
        private var correo  = ""
        private  var contrasenia = ""
        private var ccontrasenia = ""
    private fun validarInforacion() {
        nombres= binding.etNombresV.text.toString().trim()
        correo= binding.etEmailV.text.toString().trim()
        contrasenia= binding.etPassword.text.toString().trim()
        ccontrasenia= binding.etCPassword.text.toString().trim()

        if (nombres.isEmpty()){
            binding.etNombresV.error = "Ingrese sus nombres"
            binding.etNombresV.requestFocus()
        }else if (correo.isEmpty()){
            binding.etEmailV.error = "Ingrese su correo electronico"
            binding.etEmailV.requestFocus()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            binding.etEmailV.error = "Correo no valido"
            binding.etEmailV.requestFocus()
        }else if (contrasenia.isEmpty()){
            binding.etPassword.error = "Ingrese su contraseña"
            binding.etPassword.requestFocus()
        } else if (contrasenia.length <=6){
            binding.etPassword.error = "Necesita 6 o mas caracteres"
            binding.etPassword.requestFocus()
        }else if (ccontrasenia.isEmpty()){
            binding.etCPassword.error = "por favor confirme la contraseña"
            binding.etCPassword.requestFocus()
        }else if (contrasenia!=ccontrasenia){
            binding.etCPassword.error = "Las contraseñas no coinciden"
            binding.etCPassword.requestFocus()
        }else{
            registrarVendedor()
        }
    }

    private fun registrarVendedor() {
        progressDialog.setMessage("Creando Cuenta")
        progressDialog.show()


        firebaseAuth.createUserWithEmailAndPassword(correo,contrasenia)
            .addOnSuccessListener {
                insertanInfoBD()
            }
            .addOnFailureListener {e->
                Toast.makeText(this,"Fallo el registro debido a ${e.message}",Toast.LENGTH_SHORT).show()

            }

    }

    private fun insertanInfoBD(){
        progressDialog.setMessage("Guardando informacion...")


        val uidBD= firebaseAuth.uid
        val nombreBD =nombres
        val correoBD = correo
        val tiempoBD = Constantes().obtenerTiempoD()

        val datosVendedor = HashMap<String, Any>()

        datosVendedor["uid"]= "$uidBD"
        datosVendedor["nombres"]= "$nombreBD"
        datosVendedor["correo"]="$correoBD"
        datosVendedor["tiempoUsuario"]="Vendedor"
        datosVendedor["tiempo_registro"]=tiempoBD

        val reference = FirebaseDatabase.getInstance().getReference("Usuarios")
        reference.child(uidBD!!)
            .setValue(datosVendedor)

            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityVendedor::class.java))
                finish()
            }

            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(this,"Fallo el registro en base de datos debido a ${e.message}",Toast.LENGTH_SHORT).show()

            }

    }
}