package com.example.jobsrendu

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class CandidateActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate)
        sharedPreferences = getSharedPreferences("login_user", Context.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "")
        val job_id = intent.getStringExtra("job_id")

        val btn_exit_profile = findViewById<Button>(R.id.use_exist_profile)
        btn_exit_profile.setOnClickListener {
            val intentToProfile = Intent(this, CandidateProfileActivity::class.java)
            startActivity(intentToProfile)
        }

        val btn_submit = findViewById<Button>(R.id.submit_candidature)
        btn_submit.setOnClickListener {
            if (user_id != null && job_id != null) {
                createCandidature(user_id, job_id)
            }
        }

    }

    fun createCandidature(user_id: String, job_id: String) {
        val last_name = findViewById<EditText>(R.id.last_name).text.toString()
        val first_name = findViewById<EditText>(R.id.first_name).text.toString()
        val residence = findViewById<EditText>(R.id.residence).text.toString()
        val experience = findViewById<EditText>(R.id.experience).text.toString()
        val education = findViewById<EditText>(R.id.education).text.toString()
        val profile_name = findViewById<EditText>(R.id.profile_name).text.toString()

        val formatter = SimpleDateFormat("dd/MM/yyyy");
        var date = Date()
        var createDate = formatter.format(date)

        val db = Firebase.firestore
        val profile: HashMap<String, String>
        profile = hashMapOf(
            "last name" to last_name,
            "first name" to first_name,
            "residence" to residence,
            "experience" to experience,
            "education" to education,
            "profile name" to profile_name,
            "job seeker id" to user_id
        )
        var profile_id: String
        db.collection("Profiles")
            .add(profile)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                profile_id = documentReference.id
                val candidature: HashMap<String, String>
                candidature = hashMapOf(
                    "job seeker id" to user_id,
                    "job id" to job_id,
                    "status" to "waiting for reply",
                    "date" to createDate,
                    "profile id" to profile_id
                )

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
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }


    }
}