package com.example.jobsrendu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.cardview.widget.CardView

class SubscriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription)

        val role = intent.getStringExtra("role").toString()
        
        val card_plan1 = findViewById<CardView>(R.id.plan1)
        card_plan1.setOnClickListener {
            val intentToSignUp = Intent(this, SignupActivity::class.java)
            intentToSignUp.putExtra("role", role)
            intentToSignUp.putExtra("plan", "plan1")
            startActivity(intentToSignUp)
        }

        val card_plan2 = findViewById<CardView>(R.id.plan2)
        card_plan2.setOnClickListener {
            val intentToSignUp = Intent(this, SignupActivity::class.java)
            intentToSignUp.putExtra("role", role)
            intentToSignUp.putExtra("plan", "plan2")
            startActivity(intentToSignUp)
        }

        val card_plan3 = findViewById<CardView>(R.id.plan3)
        card_plan3.setOnClickListener {
            val intentToSignUp = Intent(this, SignupActivity::class.java)
            intentToSignUp.putExtra("role", role)
            intentToSignUp.putExtra("plan", "plan3")
            startActivity(intentToSignUp)
        }
    }
}