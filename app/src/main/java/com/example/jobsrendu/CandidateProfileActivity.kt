package com.example.jobsrendu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CandidateProfileActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate_profile)

        sharedPreferences = getSharedPreferences("login_user", Context.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()
        getProfiles(user_id)
    }

    fun getProfiles(user_id: String) {
        val profile_container = findViewById<ScrollView>(R.id.profile_container)
        var view: View

        val db = Firebase.firestore
        db.collection("Profiles")
            .get()
            .addOnCompleteListener {
                    task ->
                run {
                    val documents = task.result
                    for (document : QueryDocumentSnapshot in documents) {
                        if (document.get("job seeker id").toString().equals(user_id)) {
                            view = LayoutInflater.from(this).inflate(R.layout.createdjob, null)
                            view.findViewById<TextView>(R.id.created_title).text = "Profile: "+document.get("profile name") as String
                            view.findViewById<TextView>(R.id.provider).visibility = View.GONE
                            view.findViewById<TextView>(R.id.created_city).visibility = View.GONE
                            view.findViewById<TextView>(R.id.created_description).visibility = View.GONE
                            view.findViewById<TextView>(R.id.created_date).visibility = View.GONE
                            view.findViewById<TextView>(R.id.status).visibility = View.GONE
                            view.setOnClickListener {
//                            val intentToSignUp = Intent(this, SignupActivity::class.java)
//                            startActivity(intentToSignUp)
                            }
                            profile_container.addView(view)
                        }

                    }
                }

            }


    }
}