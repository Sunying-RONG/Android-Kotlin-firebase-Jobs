package com.example.jobsrendu

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val role = intent.getStringExtra("role").toString()
        Log.d("role in sign up: ", role)
        val btn_submit = findViewById<Button>(R.id.submit)
        btn_submit.setOnClickListener {
            createUser(role)
            val intent = Intent(this, SigninActivity::class.java)
            intent.putExtra("role", role)
            startActivity(intent)
        }

    }

    fun createUser(role: String) {
        var userCollection = ""
        val user_name = findViewById<EditText>(R.id.user_name).text.toString()
        val phone = findViewById<EditText>(R.id.phone).text.toString()
        val email = findViewById<EditText>(R.id.email).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()

        val db = Firebase.firestore
        // Create a new user with a first and last name
        val user = hashMapOf(
            "user name" to user_name,
            "phone" to phone,
            "email" to email,
            "password" to password
        )
        when(role) {
            "Job seeker" -> {userCollection = "Job seeker"}
            "Employer" -> {userCollection = "Employer"}
            "Agency" -> {userCollection = "Agency"}
            "Administrator" -> {userCollection = "Administrator"}
        }
// Add a new document with a generated ID
        db.collection(userCollection)
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }

    }
}