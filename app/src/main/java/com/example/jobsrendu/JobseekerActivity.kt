package com.example.jobsrendu

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
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class JobseekerActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobseeker)
        sharedPreferences = getSharedPreferences("login_user", Context.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()
        getCandidature(user_id)
    }

    fun getCandidature(user_id: String) {
        val candidatures_container = findViewById<LinearLayout>(R.id.candidatures_container)
        candidatures_container.removeAllViews()
        var view: View


        val db = Firebase.firestore
        db.collection("Candidatures")
            .get()
            .addOnCompleteListener {
                    task ->
                run {
                    val documents = task.result
                    for (document : QueryDocumentSnapshot in documents) {
                        if (document.get("job seeker id").toString().equals(user_id) == true) {
                            var job_id = document.get("job id").toString()

                           db.collection("Jobs")
                                .document(job_id)
                                .get()
                                .addOnCompleteListener {
                                    task2 ->
                                    run {
                                        view = LayoutInflater.from(this).inflate(R.layout.createdjob_jobseeker, null)
                                        val jobDocument = task2.result
                                        view.findViewById<TextView>(R.id.created_title_jobseeker).text = jobDocument.get("job title") as String
                                        view.findViewById<TextView>(R.id.created_city_jobseeker).text = jobDocument.get("city") as String
                                        view.findViewById<TextView>(R.id.provider_jobseeker).text = jobDocument.get("company") as String

                                        view.findViewById<TextView>(R.id.created_date_jobseeker).text = "Candidature date: " + document.get("date") as String
                                        view.findViewById<TextView>(R.id.status_jobseeker).text = "Status: "+document.get("status") as String
                                        view.findViewById<TextView>(R.id.created_description_jobseeker).text = "Response: "+document.get("response") as String
                                        candidatures_container.addView(view)
                                    }
                                }
                        }
                    }
                }

            }


    }
}