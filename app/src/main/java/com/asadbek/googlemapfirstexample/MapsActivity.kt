package com.asadbek.googlemapfirstexample

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.asadbek.googlemapfirstexample.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.util.Locale

// android studio da mavjud bo`lgan MapsActvitiy dan foydalanish va lokatsiyani ko`rish

// 2 ta mavzu bittada!
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

        // polyLine - locationlar orqali map ni chizib beradi, taxi ilovalarida va google map da ko`rgan bo`lishingiz mumkin.
        val polyLine = googleMap.addPolyline(
            PolylineOptions()
                .clickable(true)
                .add(
                    LatLng(-35.016,143.321),
                    LatLng(-36.116,145.421),
                    LatLng(-37.316,146.521),
                    LatLng(-39.516,149.621)
                )

        )
       /*

       // Polygon - ustiga chizish mumkin bo`lgan usuli

        val polygon = googleMap.addPolygon(
            PolygonOptions()
                .clickable(true)
                .add(
                    LatLng(-35.016,143.321),
                    LatLng(-36.116,145.421),
                    LatLng(-37.316,146.521),
                    LatLng(-39.516,149.621)
                )
                .strokeColor(Color.BLUE)
                .fillColor(Color.GREEN)
        )
        */
        // yuqoridagi polyline dagi lokatsiya utkazish uchun cameraPosition va move cameralar
        // bular ishlashi uchun pastdagi getDeviceLocation nomli funktsiyamizni ishlatlmasligimiz kerak.
        val cameraPosition = CameraPosition.Builder().target(LatLng(-36.116,145.421))
            .zoom(10f).build()
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        // bosilganda marker qo`shish - realtime map ikkinchi dars misollari uchun
        mMap.setOnMapClickListener {

            // marker qo`yilgan joyning address ni olish uchun
            try {
                val geo = Geocoder(this, Locale.getDefault())
                val addresses = geo.getFromLocation(it.latitude,it.longitude,1)
                if (addresses!!.isEmpty()){
                    Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show()
                }else{
                    if (addresses.size > 0){
                        // address ni olib o`zgaruvchiga tenglashtiradi
                        val title = "${addresses.get(0).featureName}, ${addresses.get(0).adminArea}, ${addresses.get(0).locality}"
                        Toast.makeText(this, "Address: "+addresses.get(0).featureName + addresses.get(0).adminArea + addresses.get(0).locality, Toast.LENGTH_SHORT).show()
                        // market qo`shish uchun
                        mMap.addMarker(MarkerOptions().position(it).title(title))
                    }
                }
            }catch (e:Exception){
                Log.d(TAG, "onMapReady: ${e.message}")
            }
        }

       // getDeviceLocation()

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

                     //   .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_my_location_24))) // icon qo`shishda rasm hajmi katta bo`lsa hato chiqishi mumkin
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    // newLatLngZoom - yaqinlashtirib ko`rinishi
                    // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude,location.longitude),17.0F))
                    // 3d camerani ulash

                }
            }

        }catch (e:Exception){
            Log.d(TAG, "getDeviceLocation: ")
        }
    }
}