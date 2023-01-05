package com.example.jobsrendu

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class CandidateProfileActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate_profile)

        val job_id = intent.getStringExtra("job_id")

        sharedPreferences = getSharedPreferences("login_user", Context.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()
        if (job_id != null) {
            getProfiles(user_id, job_id)
        }
    }

    fun getProfiles(user_id: String, job_id: String) {
        val profile_container = findViewById<LinearLayout>(R.id.profile_container)
        profile_container.removeAllViews()
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
                            view = LayoutInflater.from(this).inflate(R.layout.createdjob_profile, null)
                            view.findViewById<TextView>(R.id.created_title_profile).text = "Profile: "+document.get("profile name") as String

                            val formatter = SimpleDateFormat("dd/MM/yyyy");
                            var date = Date()
                            var createDate = formatter.format(date)

                            val candidature: HashMap<String, String>
                            candidature = hashMapOf(
                                "job seeker id" to user_id,
                                "job id" to job_id,
                                "status" to "waiting for reply",
                                "response" to "",
                                "date" to createDate,
                                "profile id" to document.id
                            )
                            view.setOnClickListener {
                                db.collection("Candidatures")
                                    .add(candidature)
                                    .addOnSuccessListener { documentReference ->
                                        Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                                        Toast.makeText(this, "Candidate sucess !", Toast.LENGTH_SHORT).show()
                                        val intentToJobseeker = Intent(this, JobseekerActivity::class.java)
                                        startActivity(intentToJobseeker)
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w(ContentValues.TAG, "Error adding document", e)
                                        Toast.makeText(this, "Candidate failed !", Toast.LENGTH_SHORT).show()
                                    }
                            }
                            profile_container.addView(view)
                        }

                    }
                }

            }


    }
}