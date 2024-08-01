package com.asadbek

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.asadbek.googlemapfirstexample.R
import com.asadbek.googlemapfirstexample.TAG

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.asadbek.googlemapfirstexample.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.mapType =  GoogleMap.MAP_TYPE_HYBRID // harita ko`rinishini o`zgartirish

        getDeviceLocation()

    }
    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getDeviceLocation(){

        // instlizatsiya fusedLocation uchun
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        try {
            // lokatsiya olish uchun lastLocation - oxirgi joylashuv
            val locationResult = fusedLocationProviderClient.lastLocation
            // joylashuv natijasi kelganda isSuccessful bo`ladi va natijani logda korish mumkin
            locationResult.addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    // joylashuvni olish
                    // natija logcat da chiqadi
                    // permission berilishi va gps yoqilishi kerak
                    val location = task.result

                    // cameraPosition - 3D xolatda kameradan foydalanish
                    // tilt(60f) - og`ish darajasi 0f bo`lganda tepadan ko`rinadi
                    // bearing(90f) - shimol tarafga nisbatan burilishi
                    // zoom(15f) - yaqinlashtirib ko`rinishi
                    // target(LatLang(10,10)) - joylashuvimiz
                    val cameraPosition =
                        CameraPosition.Builder().target(LatLng(location.latitude,location.longitude)).tilt(60F)
                            .zoom(15f).bearing(0f).build()

                    // Add a marker in Sydney and move the camera
                    mMap.addMarker(MarkerOptions().position(LatLng(location.latitude,location.longitude)).title("Marker joylashuv"))

                    // newLatLngZoom - yaqinlashtirib ko`rinishi
                    // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude,location.longitude),17.0F))

                    // 3d camerani ulash
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                }
            }

        }catch (e:Exception){
            Log.d(TAG, "getDeviceLocation: ")
        }
    }
}