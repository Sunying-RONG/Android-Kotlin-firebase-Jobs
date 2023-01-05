package com.example.jobsrendu

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TableRow
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateJobActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_job)

        sharedPreferences = getSharedPreferences("login_user", Context.MODE_PRIVATE)
        val role = sharedPreferences.getString("role", "")
        val user_id = sharedPreferences.getString("user_id", "")
        val user_name = sharedPreferences.getString("login", "")

        val btn_create_job = findViewById<Button>(R.id.btn_create_job)
        btn_create_job.setOnClickListener {
            if (role != null && user_id != null && user_name != null) {
                createJob(role, user_id, user_name)
                backTo(role)
            }
        }
    }

    fun createJob(role: String, user_id: String, user_name: String) {
        var company: String
        if (role.equals("Employer")) {
            findViewById<TableRow>(R.id.company_row).visibility = View.GONE
            company = user_name
        } else {
            company = findViewById<EditText>(R.id.job_company).text.toString()
        }
        val job_field = findViewById<EditText>(R.id.job_field).text.toString()
        val city = findViewById<EditText>(R.id.city).text.toString()
        val job_title = findViewById<EditText>(R.id.job_title).text.toString()
        val description = findViewById<EditText>(R.id.description).text.toString()

        val db = Firebase.firestore
        val job: HashMap<String, String>
        job = hashMapOf(
            "role" to role,
            "provider id" to user_id,
            "job field" to job_field,
            "city" to city,
            "job title" to job_title,
            "description" to description,
            "company" to company
        )
        db.collection("Jobs")
            .add(job)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(this, "Job created !", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
                Toast.makeText(this, "Job creation failed !", Toast.LENGTH_SHORT).show()
            }
    }

    fun backTo(role: String) {
        if (role.equals("Employer")) {
            val intentBackTo = Intent(this, EmployerActivity::class.java)
            startActivity(intentBackTo)
        } else if (role.equals("Agency")) {
            val intentBackTo = Intent(this, AgencyActivity::class.java)
            startActivity(intentBackTo)
        }
    }

}