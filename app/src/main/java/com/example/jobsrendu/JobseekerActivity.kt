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
        val candidatures_container = findViewById<ScrollView>(R.id.candidatures_container)
        var view: View
        view = LayoutInflater.from(this).inflate(R.layout.createdjob, null)

        val db = Firebase.firestore
        db.collection("Candidatures")
            .get()
            .addOnCompleteListener {
                    task ->
                run {
                    val documents = task.result
                    for (document : QueryDocumentSnapshot in documents) {
                        Log.d("user_id", user_id)
                        Log.d("job seeker id", document.get("job seeker id").toString())
                        if (document.get("job seeker id").toString().equals(user_id) == true) {

                            var job_id = document.get("job id").toString()

                            db.collection("Jobs")
                                .get()
                                .addOnCompleteListener {
                                    task2 ->
                                    run {
                                        val jobDocuments = task2.result
                                        for (jobDocument : QueryDocumentSnapshot in jobDocuments) {
                                            if (jobDocument.id.equals(job_id)) {
                                                view.findViewById<TextView>(R.id.created_title).text = jobDocument.get("job title") as String
                                                view.findViewById<TextView>(R.id.created_city).text = jobDocument.get("city") as String
                                                var provider_role = jobDocument.get("role") as String
                                                var provider_id = jobDocument.get("provider id") as String
                                                if (provider_role.equals("Employer")) {

                                                    db.collection("Employer")
                                                        .get()
                                                        .addOnCompleteListener {
                                                            task3 ->
                                                            run {
                                                                val employerDocuments = task3.result
                                                                for (employerDocument : QueryDocumentSnapshot in employerDocuments) {
                                                                    if (employerDocument.id.equals(provider_id)) {
                                                                        view.findViewById<TextView>(R.id.provider).text = employerDocument.get("user name") as String
                                                                        break
                                                                    }
                                                                }
                                                            }
                                                        }
                                                } else if (provider_role.equals("Agency")) {

                                                    db.collection("Agency")
                                                        .get()
                                                        .addOnCompleteListener {
                                                            task4 ->
                                                            run {
                                                                val agencyDocuments = task4.result
                                                                for (agencyDocument : QueryDocumentSnapshot in agencyDocuments) {
                                                                    if (agencyDocument.id.equals(provider_id)) {
                                                                        view.findViewById<TextView>(R.id.provider).text = agencyDocument.get("user name") as String
                                                                        break
                                                                    }
                                                                }
                                                            }
                                                        }
                                                }
                                            }
                                            break
                                        }
                                    }
                                }

                            view.findViewById<TextView>(R.id.created_date).text = "Candidature date: " + document.get("date") as String
                            view.findViewById<TextView>(R.id.candidature_profile).text = "Candidature profile: "+document.get("profile name") as String
                            view.findViewById<TextView>(R.id.status).text = "Status: "+document.get("status") as String
                            view.findViewById<TextView>(R.id.created_description).visibility = View.GONE
//                            view.setOnClickListener {
//                                val intentToSignUp = Intent(this, SignupActivity::class.java)
//                                intentToSignUp.putExtra("role", role)
//                                intentToSignUp.putExtra("plan", document.id)
//                                startActivity(intentToSignUp)
//                            }
                            candidatures_container.addView(view)
                        }
                    }
                }

            }


    }
}