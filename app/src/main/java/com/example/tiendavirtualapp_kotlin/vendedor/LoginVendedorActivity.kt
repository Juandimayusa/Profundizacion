package com.example.tiendavirtualapp_kotlin.vendedor
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tiendavirtualapp_kotlin.R
import com.example.tiendavirtualapp_kotlin.databinding.ActivityLoginVendedorBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginVendedorActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginVendedorBinding
    private lateinit var  firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private val Google_Sign_in = 100

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityLoginVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)



        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        //boton
        boton()




        binding.btnLoginV.setOnClickListener {
            ValidarInfo()

        }


        binding.tvRegistrarV.setOnClickListener {
            startActivity(Intent(applicationContext, RegistroVendedorActivity::class.java))

        }




    }

    private fun boton() {
        binding.btnGoogleSignIn.setOnClickListener {
            //configurar google sign in
            var googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            var googleClient = GoogleSignIn.getClient(this,googleConf)
            var singInintent = googleClient.signInIntent
            startActivityForResult(singInintent, Google_Sign_in)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Google_Sign_in) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                if (account != null) {
                    Log.d("Tag", "firebasegoogleid ${account.id}")
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            } catch (e: ApiException) {
                Log.d("Tag", "Error en el inicio de sesión de Google: $e")
                Toast.makeText(this, "Error en el inicio de sesión de Google: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Redirigir a MainActivityVendedor
                    val intent = Intent(this, MainActivityVendedor::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.w("Tag", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Autenticación fallida.", Toast.LENGTH_SHORT).show()
                }
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