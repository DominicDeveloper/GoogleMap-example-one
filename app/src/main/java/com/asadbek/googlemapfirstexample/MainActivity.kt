package com.asadbek.googlemapfirstexample

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.asadbek.googlemapfirstexample.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener


/*
    Reja: Google map bilan ishlash birinchi dars
    1. Qurilma joylashuvini aniqlash(lan lon orqali)
    Bunga ikki xil xolat bor
    1. Dasturga kirilishiga aniqlab oladi va o`zgaruvchiga tenglab qo`yish mumkin
    2. Ma`lum bir vaqtda locationni tekshirib turadi

 */
const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDeviceLocation()

    }

    // ip joylashuvni olish - permission nastroykadan yoqiladi
   /*
       kutubxona ->
       implementation(libs.play.services.location)
    */
    /*
    Permissionslar
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
     */
    // Dastur ishga tushganda faqat bir marta ishlaydi
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
                    binding.tvIp.text = "Lat: ${location.latitude}\nLong: ${location.longitude}"
                    // log da natija kelmasa textView da chiqishi mumkin.
                    Log.d(TAG, "getDeviceLocation: lat: ${location.latitude}")
                    Log.d(TAG, "getDeviceLocation: long: ${location.longitude}")
                }
            }

        }catch (e:Exception){
            Log.d(TAG, "getDeviceLocation: ")
        }
    }
}