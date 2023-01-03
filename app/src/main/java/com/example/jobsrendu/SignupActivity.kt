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

class SignupActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        sharedPreferences = getSharedPreferences("login_user", Context.MODE_PRIVATE)
//        val role = intent.getStringExtra("role").toString()
        val role = sharedPreferences.getString("role", "").toString()
        if (role != null) {
            Log.d("role in sign up: ", role)
        }
        val subscription = intent.getStringExtra("plan").toString()
        Log.d("subscription: ", subscription)

        val btn_submit = findViewById<Button>(R.id.submit)
        btn_submit.setOnClickListener {
            if (role != null) {
                createUser(role, subscription)
            }
            val intent = Intent(this, SigninActivity::class.java)
            intent.putExtra("role", role)
            startActivity(intent)
        }

    }

    fun createUser(role: String, subscription: String) {
        var userCollection = ""
        val user_name = findViewById<EditText>(R.id.user_name).text.toString()
        val phone = findViewById<EditText>(R.id.phone).text.toString()
        val email = findViewById<EditText>(R.id.email).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()

        val db = Firebase.firestore
        // Create a new user with a first and last name
        val user: HashMap<String, String>
        if (role.equals("Employer") || role.equals("Agency")) {
            user = hashMapOf(
                "user name" to user_name,
                "phone" to phone,
                "email" to email,
                "password" to password,
                "plan" to subscription,
                "valid" to "pending"
            )
        } else {
            user = hashMapOf(
                "user name" to user_name,
                "phone" to phone,
                "email" to email,
                "password" to password
            )
        }
        when(role) {
            "Job seeker" -> {userCollection = "Job seeker"}
            "Employer" -> {userCollection = "Employer"}
            "Agency" -> {userCollection = "Agency"}
            "Administrator" -> {userCollection = "Administrator"}
        }
        db.collection(userCollection)
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(this, "Sign up sucess !", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
                Toast.makeText(this, "Sign up failed !", Toast.LENGTH_SHORT).show()
            }
    }
}