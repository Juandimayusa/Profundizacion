package com.example.tiendavirtualapp_kotlin.vendedor

import android.content.pm.PackageManager
import android.health.connect.datatypes.ExerciseRoute.Location
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tiendavirtualapp_kotlin.R
import com.example.tiendavirtualapp_kotlin.databinding.ActivityUbicationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class UbicationActivity : AppCompatActivity() {
private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
private lateinit var locationTv : TextView
    private lateinit var binding: ActivityUbicationBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ubication)

        locationTv= findViewById(R.id.locationTv)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        getCurrentLocation()
    }
    private fun getCurrentLocation(){
        if(ActivityCompat.checkSelfPermission(
            this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: android.location.Location? ->
            if(location == null){
                locationTv.text = "No se pudo obtener la ubicaion"
            }else {
                val latitud = location.latitude
                val longitud = location.longitude
                locationTv.text="Latitud: $latitud \n Longitud: $longitud"
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResult: IntArray
    ){
        super.onRequestPermissionsResult(requestCode,permissions,grantResult)
        if(requestCode== 1 && grantResult.isNotEmpty() && grantResult[0]==PackageManager.PERMISSION_GRANTED){
            getCurrentLocation()
        }
    }

}