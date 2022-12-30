package com.example.jobsrendu

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class SigninActivity : AppCompatActivity() {
    val navigationFragment = NavigationFragment()
    val mBundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        val role = intent.getStringExtra("role").toString()

        val btn_sign_up = findViewById<Button>(R.id.sign_up)
        btn_sign_up.setOnClickListener {
            val intentToSignUp = Intent(this, SignupActivity::class.java)
            intentToSignUp.putExtra("role", role)
            startActivity(intentToSignUp)
        }

        val btn_submit = findViewById<Button>(R.id.submit)
        btn_submit.setOnClickListener {
            login(role)
        }
    }

    fun login(role: String) {
        var userCollection = ""
        val email = findViewById<EditText>(R.id.email).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()

        when(role) {
            "Job seeker" -> {userCollection = "Job seeker"}
            "Employer" -> {userCollection = "Employer"}
            "Agency" -> {userCollection = "Agency"}
            "Administrator" -> {userCollection = "Administrator"}
        }

        val db = Firebase.firestore
        db.collection(userCollection)
            .get()
            .addOnCompleteListener {
                task ->
                run {
                    val documents = task.result
                    for (document : QueryDocumentSnapshot in documents) {
                        if (document.get("email")?.equals(email) == true &&
                            document.get("password")?.equals(password) == true) {
                            Log.d("Login: ", "success")
                            mBundle.putString("user_name", document.get("user name") as String?)
                            navigationFragment.arguments = mBundle
                            supportFragmentManager.beginTransaction().replace(R.id.fragment_navigation, navigationFragment).commit()
                            Log.d("role sign in: ", role)
                            var intentToEmployer : Intent? = null
                            when(role) {
                                "Job seeker" -> {intentToEmployer = Intent(this, JobseekerActivity::class.java)}
                                "Employer" -> {intentToEmployer = Intent(this, EmployerActivity::class.java)}
                                "Agency" -> {intentToEmployer = Intent(this, AgencyActivity::class.java)}
                                "Administrator" -> {intentToEmployer = Intent(this, AdminActivity::class.java)}
                            }
                            startActivity(intentToEmployer)
                        }
                    }
                }

            }


    }
}