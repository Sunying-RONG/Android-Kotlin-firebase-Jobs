package com.example.jobsrendu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class EmployerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employer)

        val btn_create_job = findViewById<Button>(R.id.to_create_job)
        btn_create_job.setOnClickListener {
            val intentToCreateJob = Intent(this, CreateJobActivity::class.java)
            startActivity(intentToCreateJob)
        }
    }
}