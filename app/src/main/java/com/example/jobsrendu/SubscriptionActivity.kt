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
import androidx.cardview.widget.CardView
import androidx.core.view.marginBottom
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SubscriptionActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription)

        sharedPreferences = getSharedPreferences("login_user", Context.MODE_PRIVATE)
        val role = sharedPreferences.getString("role", "").toString()
//        val role = intent.getStringExtra("role").toString()
        getSubscritionsInfo(role)
    }

    fun getSubscritionsInfo(role: String) {
        val subscription_container = findViewById<LinearLayout>(R.id.subscription_container)
        subscription_container.removeAllViews()
        var view: View

        val db = Firebase.firestore
        db.collection("Subscriptions")
            .get()
            .addOnCompleteListener {
                    task ->
                run {
                    val documents = task.result
                    for (document : QueryDocumentSnapshot in documents) {
                        view = LayoutInflater.from(this).inflate(R.layout.subscripiton, null)
                        view.findViewById<TextView>(R.id.sub_name).text = document.get("name") as String
                        view.findViewById<TextView>(R.id.sub_time).text = document.get("time") as String
                        view.findViewById<TextView>(R.id.sub_price).text = document.get("price") as String
                        view.setOnClickListener {
                            val intentToSignUp = Intent(this, SignupActivity::class.java)
                            intentToSignUp.putExtra("role", role)
                            intentToSignUp.putExtra("plan", document.id)
                            startActivity(intentToSignUp)
                        }
                        subscription_container.addView(view)
                    }
                }

            }


    }
}