package com.example.tiendavirtualapp_kotlin.vendedor

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PathPermission
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.view.GravityCompat

import androidx.fragment.app.Fragment
import com.example.tiendavirtualapp_kotlin.R
import com.example.tiendavirtualapp_kotlin.databinding.ActivityMainVendedorBinding
import com.example.tiendavirtualapp_kotlin.vendedor.Bottom_Nav_Frangments_Vendedor.FragmentMisProductosV
import com.example.tiendavirtualapp_kotlin.vendedor.Bottom_Nav_Frangments_Vendedor.FragmentOrdenesV
import com.example.tiendavirtualapp_kotlin.vendedor.Nav_Fragments_Vendedor.FragmentInicioV
import com.example.tiendavirtualapp_kotlin.vendedor.Nav_Fragments_Vendedor.FragmentMiTiendaV
import com.example.tiendavirtualapp_kotlin.vendedor.Nav_Fragments_Vendedor.FragmentReseniaV
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivityVendedor : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainVendedorBinding



    private var firebaseAuth : FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.Ubicacion.setOnClickListener{
            startActivity(Intent(applicationContext,UbicationActivity::class.java))
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        firebaseAuth = FirebaseAuth.getInstance()
        comprobarSesion()

        binding.navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLAYOUT,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer

        )
        binding.drawerLAYOUT.addDrawerListener(toggle)
        toggle.syncState()

        replaceFragment(FragmentInicioV())
        binding.navigationView.setCheckedItem(R.id.op_inicio_v)

    }



    private fun getCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(
            this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            &&ActivityCompat.checkSelfPermission(
                this,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }


    }



    private fun cerrarSesion(){
        firebaseAuth!!.signOut()
       startActivity(Intent(applicationContext,LoginVendedorActivity::class.java))
        finish()
        Toast.makeText(applicationContext,"Has cerraado sesion",Toast.LENGTH_SHORT).show()
    }
    private fun comprobarSesion() {
        /*Si el usuario no ha iniciado sesion */
        if (firebaseAuth!!.currentUser==null){
            startActivity(Intent(applicationContext,LoginVendedorActivity::class.java))
            Toast.makeText(applicationContext,"Vendedor no registrado o no logeado",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(applicationContext,"Vendedor en linea",Toast.LENGTH_SHORT).show()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment, fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.op_inicio_v->{
                replaceFragment(FragmentInicioV())
            }
            R.id.op_mi_tienda_v->{
                replaceFragment(FragmentMiTiendaV())
            }
            R.id.op_resenia_v->{
                replaceFragment(FragmentReseniaV())
            }
            R.id.op_cerrar_sesion_v->{
                cerrarSesion()
            }
            R.id.op_mis_productos_v->{
                replaceFragment(FragmentMisProductosV())
            }
            R.id.op_mis_ordenes_v->{
                replaceFragment(FragmentOrdenesV())
            }
        }
        binding.drawerLAYOUT.closeDrawer(GravityCompat.START)
        return true
    }
}

