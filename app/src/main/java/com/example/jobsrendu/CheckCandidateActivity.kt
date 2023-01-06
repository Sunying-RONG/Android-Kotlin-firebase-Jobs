package com.example.jobsrendu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CheckCandidateActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var view: View
    lateinit var selectedResponse: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_candidate)

        sharedPreferences = getSharedPreferences("login_user", Context.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()
        val role = sharedPreferences.getString("role", "").toString()
//        var view: View

        val job_id = intent.getStringExtra("job_id")
        val title = intent.getStringExtra("title")
        val city = intent.getStringExtra("city")
        findViewById<TextView>(R.id.select_title).text = title
        findViewById<TextView>(R.id.select_city).text = city
        if (job_id != null) {
            getCandidates(job_id, role)
        }
    }

    fun getCandidates(job_id: String, role: String) {
        val check_candidate_container = findViewById<LinearLayout>(R.id.check_candidate_container)
        check_candidate_container.removeAllViews()

        val responses = resources.getStringArray(R.array.response)

        val db = Firebase.firestore
        db.collection("Candidatures")
            .get()
            .addOnCompleteListener {
                    task ->
                run {
                    val documents = task.result
                    for (document : QueryDocumentSnapshot in documents) {
                        if (document.get("job id")?.equals(job_id) == true) {
                            val profile_id = document.get("profile id") as String

                            db.collection("Profiles")
                                .document(profile_id)
                                .get()
                                .addOnCompleteListener {
                                    task1 ->
                                    run {
                                        val profile = task1.result
                                        val job_seeker_id = profile.get("job seeker id").toString()

                                        db.collection("Job seeker")
                                            .document(job_seeker_id)
                                            .get()
                                            .addOnCompleteListener {
                                                task2 ->
                                                run {
                                                    val job_seeker = task2.result
                                                    view = LayoutInflater.from(this).inflate(R.layout.candidate_forjob, null)
                                                    check_candidate_container.addView(view)
                                                    view.findViewById<TextView>(R.id.candidate_phone).text = job_seeker.get("phone").toString()
                                                    view.findViewById<TextView>(R.id.candidate_email).text = "Email: "+job_seeker.get("email").toString()

                                                    view.findViewById<TextView>(R.id.candidate_name).text = profile.get("first name").toString()+" "+profile.get("last name").toString()
                                                    view.findViewById<TextView>(R.id.candidate_residence).text = profile.get("residence").toString()
                                                    view.findViewById<TextView>(R.id.candidate_experience).text = profile.get("experience").toString()
                                                    view.findViewById<TextView>(R.id.candidate_education).text = profile.get("education").toString()

                                                    Log.d("response EditText",
                                                        view.findViewById<EditText>(R.id.candidate_response)
                                                            .toString()
                                                    )

                                                    val spinner = view.findViewById<Spinner>(R.id.candidate_status)
                                                    val adapter = ArrayAdapter(
                                                        this,
                                                        android.R.layout.simple_spinner_item,
                                                        responses
                                                    )
                                                    spinner.adapter = adapter
                                                    val formerStatus = document.get("status").toString()
                                                    Log.d("formerStatus: ", formerStatus)
                                                    when(formerStatus) {
                                                        "waiting for reply" -> {
                                                            spinner.setSelection(1)
                                                        }
                                                        "accept" -> {
                                                            spinner.setSelection(2)
                                                        }
                                                        "refuse" -> {
                                                            spinner.setSelection(3)
                                                        }
                                                    }

                                                    spinner.onItemSelectedListener = object :
                                                        AdapterView.OnItemSelectedListener {
                                                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                                                            selectedResponse = spinner.selectedItem.toString()
                                                        }
                                                        override fun onNothingSelected(parent: AdapterView<*>) {
                                                        }
                                                    }

                                                    val btn_submit = view.findViewById<Button>(R.id.submit_to_candidate)

                                                    btn_submit.setOnClickListener {
                                                        val responseEdit = view.findViewById<EditText>(R.id.candidate_response).text.toString()
                                                        Log.d("responseEdit", responseEdit)
                                                        Log.d("status", selectedResponse)
                                                        db.collection("Candidatures")
                                                            .document(document.id)
                                                            .update(mapOf(
                                                                "status" to selectedResponse,
                                                                "response" to responseEdit
                                                            ))
                                                        var intentTo : Intent? = null
                                                        when(role) {
                                                            "Employer" -> {intentTo = Intent(this, EmployerActivity::class.java)}
                                                            "Agency" -> {intentTo = Intent(this, AgencyActivity::class.java)}
                                                        }
                                                        startActivity(intentTo)
                                                    }

                                                }
                                            }

                                    }
                                }

                        }

                    }
                }

            }


    }
}