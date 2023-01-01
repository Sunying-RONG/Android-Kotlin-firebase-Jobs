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
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class SigninActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    val navigationFragment = NavigationFragment()
    val mBundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        sharedPreferences = getSharedPreferences("login_user", Context.MODE_PRIVATE)

        val role = intent.getStringExtra("role").toString()
        val btn_sign_up = findViewById<Button>(R.id.sign_up)

        val to_sign_up_layout = findViewById<LinearLayout>(R.id.to_sign_up)
        if (role.equals("Administrator")) {
            to_sign_up_layout.visibility = LinearLayout.GONE
        }
        btn_sign_up.setOnClickListener {
            if (role.equals("Employer") || role.equals("Agency")) {
                val intentToSubscription = Intent(this, SubscriptionActivity::class.java)
                intentToSubscription.putExtra("role", role)
                startActivity(intentToSubscription)
            } else {
                val intentToSignUp = Intent(this, SignupActivity::class.java)
                intentToSignUp.putExtra("role", role)
                startActivity(intentToSignUp)
            }
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
                    if (documents.size() == 0) {
                        Toast.makeText(this, "Wrong email or password !", Toast.LENGTH_SHORT).show()
                    }
                    for (document : QueryDocumentSnapshot in documents) {
                        if (document.get("email")?.equals(email) == true &&
                            document.get("password")?.equals(password) == true) {
                            if ((role.equals("Employer") || role.equals("Agency")) && document.get("valid")?.equals("pending") == true){
                                    Toast.makeText(this, "Please wait for Admin validation !", Toast.LENGTH_SHORT).show()
                            } else if ((role.equals("Employer") || role.equals("Agency")) && document.get("valid")?.equals("no") == true) {
                                Toast.makeText(this, "Sign up denied !", Toast.LENGTH_SHORT).show()
                            } else {
                                Log.d("Login", document.get("user name") as String)
                                var editor = sharedPreferences.edit()
                                editor.putString("login", document.get("user name") as String)
                                editor.commit()

//                                mBundle.putString("user_name", document.get("user name") as String?)
//                                navigationFragment.arguments = mBundle
//                                supportFragmentManager.beginTransaction().replace(R.id.fragment_navigation, navigationFragment).commit()
                                Log.d("role sign in", role)
                                var intentTo : Intent? = null
                                when(role) {
                                    "Job seeker" -> {intentTo = Intent(this, JobseekerActivity::class.java)}
                                    "Employer" -> {intentTo = Intent(this, EmployerActivity::class.java)}
                                    "Agency" -> {intentTo = Intent(this, AgencyActivity::class.java)}
                                    "Administrator" -> {intentTo = Intent(this, AdminActivity::class.java)}
                                }
                                startActivity(intentTo)
                            }
                        } else {
                            Log.d("sign in: ", "fail")
                            Toast.makeText(this, "Wrong email or password !", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }


    }
}