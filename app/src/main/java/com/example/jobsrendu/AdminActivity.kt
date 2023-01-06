package com.example.jobsrendu

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    var subscriptions = HashMap<String, HashMap<String, String>> ()
    lateinit var subscription_admin_container: LinearLayout
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        sharedPreferences = getSharedPreferences("login_user", Context.MODE_PRIVATE)
        subscription_admin_container = findViewById<LinearLayout>(R.id.subscription_admin_container)
        subscription_admin_container.removeAllViews()
        getSubscriptionsInfo()

    }

    fun getSubscriptionsInfo() {
        db.collection("Subscriptions")
            .get()
            .addOnCompleteListener {
                task ->
                run {
                    val documents = task.result
                    for (document : QueryDocumentSnapshot in documents) {
                        Log.d("document", document.id)
                        var docCollection = hashMapOf<String, String>(
                            "name" to document.get("name") as String,
                            "time" to document.get("time") as String,
                            "price" to document.get("price") as String
                        )
                        subscriptions.set(document.id, docCollection)
                    }
                }
                getEmployerSubscritionsInfo()
                getAgencySubscritionsInfo()
            }
    }

    fun getEmployerSubscritionsInfo() {
        var view: View
        db.collection("Employer")
            .get()
            .addOnCompleteListener {
                    task ->
                run {
                    val documents = task.result
                    for (document : QueryDocumentSnapshot in documents) {
                        if (document.get("valid")?.equals("pending")==true) {
                            var plan = subscriptions.get(document.get("plan"))
                            view = LayoutInflater.from(this).inflate(R.layout.subscripiton_admin, null)
                            view.findViewById<TextView>(R.id.employer_name).text = document.get("user name") as String
                            view.findViewById<TextView>(R.id.employer_phone).text = document.get("phone") as String
                            view.findViewById<TextView>(R.id.employer_email).text = document.get("email") as String

                            view.findViewById<TextView>(R.id.employer_sub_time).text = plan?.get("time")
                            view.findViewById<TextView>(R.id.employer_sub_price).text = plan?.get("price")

                            var btn_valid = view.findViewById<Button>(R.id.valid)
                            var btn_refuse = view.findViewById<Button>(R.id.refuse)
                            btn_valid.setOnClickListener {
                                db.collection("Employer").document(document.id).update("valid", "yes")
                                Toast.makeText(this, "Valid subscription", Toast.LENGTH_SHORT).show()
                            }
                            btn_refuse.setOnClickListener {
                                db.collection("Employer").document(document.id).update("valid", "no")
                                Toast.makeText(this, "Refuse subscription", Toast.LENGTH_SHORT).show()
                            }
                            subscription_admin_container.addView(view)
                        }
                    }
                }
            }
    }

    fun getAgencySubscritionsInfo() {
        var view: View
        db.collection("Agency")
            .get()
            .addOnCompleteListener {
                    task ->
                run {
                    val documents = task.result
                    for (document : QueryDocumentSnapshot in documents) {
                        if (document.get("valid")?.equals("pending")==true) {
                            view = LayoutInflater.from(this).inflate(R.layout.subscripiton_admin, null)
                            view.findViewById<TextView>(R.id.employer_name).text = document.get("user name") as String
                            view.findViewById<TextView>(R.id.employer_phone).text = document.get("phone") as String
                            view.findViewById<TextView>(R.id.employer_email).text = document.get("email") as String
                            var plan = subscriptions.get(document.get("plan"))
                            view.findViewById<TextView>(R.id.employer_sub_time).text = plan?.get("time")
                            view.findViewById<TextView>(R.id.employer_sub_price).text = plan?.get("price")

                            var btn_valid = view.findViewById<Button>(R.id.valid)
                            var btn_refuse = view.findViewById<Button>(R.id.refuse)
                            btn_valid.setOnClickListener {
                                db.collection("Agency").document(document.id).update("valid", "yes")
                            }
                            btn_refuse.setOnClickListener {
                                db.collection("Agency").document(document.id).update("valid", "no")
                            }
                            subscription_admin_container.addView(view)
                        }
                    }
                }
            }
    }
}