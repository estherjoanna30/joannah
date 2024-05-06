package com.example.joannah

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class home : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var cardViewRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance()
        cardViewRef = database.reference.child("selected_card")

        // Set OnClickListener for the first CardView
        findViewById<CardView>(R.id.CardView).setOnClickListener {
            saveSelectedCard("others")
            val intent = Intent(this, others::class.java)
            intent.putExtra("SelectedOccasion", "OTHERS")
            startActivity(intent)
        }


        findViewById<CardView>(R.id.cardView3).setOnClickListener {
            saveSelectedCard("birthday")
            startActivity(intent)
            val intent = Intent(this, birthday::class.java)
            intent.putExtra("SelectedOccasion", "BIRTHDAY")
            startActivity(intent)
        }

        // Set OnClickListener for the third CardView
        findViewById<CardView>(R.id.cardView4).setOnClickListener {
            saveSelectedCard("anniversary")
            val intent = Intent(this, anniversary::class.java)
            intent.putExtra("SelectedOccasion", "ANNIVERSARY")
            startActivity(intent)
        }
    }

    private fun saveSelectedCard(cardName: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            cardViewRef.child(userId).setValue(cardName)
                .addOnSuccessListener {
                    // Data saved successfully
                }
                .addOnFailureListener { e ->
                    // Failed to save data
                }
        }
    }
}
