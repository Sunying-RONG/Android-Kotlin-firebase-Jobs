package com.example.jobsrendu

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private var country_name = "Country"
    private val locationPermissionCode = 2
    val navigationFragment = NavigationFragment()
    val mBundle = Bundle()
    val db = Firebase.firestore
    var employerList = HashMap<String, String>()
    var agencyList = HashMap<String, String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBundle.putString("country_name", "Country")
        getLocation()
        getEmployerName()
        getAgencyName()

        val btn_go = findViewById<Button>(R.id.go)
        btn_go.setOnClickListener {
            searchJobs()
        }
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

    fun getEmployerName() {
        db.collection("Employer")
            .get()
            .addOnCompleteListener {
                task ->
                run {
                    val documents = task.result
                    for (document : QueryDocumentSnapshot in documents) {
                        employerList.put(document.id, document.get("user name") as String)
                    }
                }
            }
    }

    fun getAgencyName() {
        db.collection("Agency")
            .get()
            .addOnCompleteListener {
                    task ->
                run {
                    val documents = task.result
                    for (document : QueryDocumentSnapshot in documents) {
                        agencyList.put(document.id, document.get("user name") as String)
                    }
                }
            }
    }

    fun searchJobs() {
        val what = findViewById<EditText>(R.id.what).text.toString()
        val where = findViewById<EditText>(R.id.where).text.toString()
        val job_list_container = findViewById<LinearLayout>(R.id.job_list_container)
        job_list_container.removeAllViews()
        var view: View

        db.collection("Jobs")
            .get()
            .addOnCompleteListener {
                    task ->
                run {
                    val documents = task.result
                    if (documents.size() == 0) {
                        Toast.makeText(this, "Wrong email or password !", Toast.LENGTH_SHORT).show()
                    }
                    for (document : QueryDocumentSnapshot in documents) {
                        if ((document.get("job field").toString().contains(what) ||
                            document.get("job title").toString().contains(what)) &&
                            document.get("city").toString().contains(where)
                        ) {
                            view = LayoutInflater.from(this).inflate(R.layout.createdjob, null)
                            view.findViewById<TextView>(R.id.created_title).text = document.get("job title") as String
                            if ((document.get("role") as String).equals("Employer")) {
                                view.findViewById<TextView>(R.id.provider).text = employerList.get(document.get("provider id"))
                            } else if ((document.get("role") as String).equals("Agency")) {
                                view.findViewById<TextView>(R.id.provider).text = agencyList.get(document.get("provider id"))
                            }
                            view.findViewById<TextView>(R.id.created_city).text = document.get("city") as String
                            view.findViewById<TextView>(R.id.created_description).text = document.get("description") as String
//                            view.setOnClickListener {
//                                val intentToSignUp = Intent(this, SignupActivity::class.java)
//                                intentToSignUp.putExtra("role", role)
//                                intentToSignUp.putExtra("plan", document.id)
//                                startActivity(intentToSignUp)
//                            }
                            job_list_container.addView(view)
                        }
                    }

                }

            }


    }

}