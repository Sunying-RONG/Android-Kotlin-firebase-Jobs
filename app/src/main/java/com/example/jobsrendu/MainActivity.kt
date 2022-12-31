package com.example.jobsrendu

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private var country_name = "Country"
    private val locationPermissionCode = 2
    val navigationFragment = NavigationFragment()
    val mBundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBundle.putString("country_name", "Country")
        getLocation()
//        initializeCloudFirestore()
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }
    override fun onLocationChanged(location: Location) {
//        tvGpsLocation = findViewById(R.id.tv_countryName)
//        tvGpsLocation.text = getCountryFromCoordinate(location.latitude, location.longitude)
        country_name = getCountryFromCoordinate(location.latitude, location.longitude)
        mBundle.putString("country_name", country_name)
        navigationFragment.arguments = mBundle
        supportFragmentManager.beginTransaction().replace(R.id.fragment_navigation, navigationFragment).commit()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getCountryFromCoordinate(latitude: Double, longitude: Double): String {
        var geocoder = Geocoder(getApplicationContext(), Locale.getDefault())
        var country: String = ""
        try {
            val listAddresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
            if (null != listAddresses && listAddresses.size > 0) {
                country = listAddresses[0].countryName
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return country;
    }

//    fun initializeCloudFirestore() {
//        val db = Firebase.firestore
//        // Create a new user with a first and last name
//        val user = hashMapOf(
//            "first" to "Ada",
//            "last" to "Lovelace",
//            "born" to 1815
//        )
//// Add a new document with a generated ID
//        db.collection("users")
//        .add(user)
//        .addOnSuccessListener { documentReference ->
//            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//        }
//        .addOnFailureListener { e ->
//            Log.w(TAG, "Error adding document", e)
//        }
//
//    }

}