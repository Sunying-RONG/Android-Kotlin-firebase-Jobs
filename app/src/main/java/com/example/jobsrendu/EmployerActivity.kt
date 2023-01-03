package com.example.jobsrendu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EmployerActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employer)

        sharedPreferences = getSharedPreferences("login_user", Context.MODE_PRIVATE)
        val btn_create_job = findViewById<Button>(R.id.to_create_job)
        btn_create_job.setOnClickListener {
            val intentToCreateJob = Intent(this, CreateJobActivity::class.java)
            startActivity(intentToCreateJob)
        }

        val user_id = sharedPreferences.getString("user_id", "").toString()
        getJobs(user_id)
    }

    fun getJobs(user_id: String) {
        val created_jobs_container = findViewById<LinearLayout>(R.id.created_jobs_container)
        var view: View

        val db = Firebase.firestore
        db.collection("Jobs")
            .get()
            .addOnCompleteListener {
                    task ->
                run {
                    val documents = task.result
                    for (document : QueryDocumentSnapshot in documents) {
                        if (document.get("provider id")?.equals(user_id) == true) {
                            view = LayoutInflater.from(this).inflate(R.layout.createdjob, null)
                            view.findViewById<TextView>(R.id.created_title).text = document.get("job title") as String
                            view.findViewById<TextView>(R.id.created_city).text = document.get("city") as String
                            view.findViewById<TextView>(R.id.created_description).text = document.get("description") as String
//                            view.setOnClickListener {
//                                val intentToSignUp = Intent(this, SignupActivity::class.java)
//                                intentToSignUp.putExtra("role", role)
//                                intentToSignUp.putExtra("plan", document.id)
//                                startActivity(intentToSignUp)
//                            }
                            created_jobs_container.addView(view)
                        }

                    }
                }

            }


    }
}