package com.example.joannah

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class birthday : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var textViewRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birthday)

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance()
        textViewRef = database.reference.child("selected_text")

        // Find all TextViews
        val textViewIds = listOf(
            R.id.textView14, R.id.textView13,
            R.id.textView12, R.id.textView11, R.id.textView10, R.id.textView7
        )

        // Set click listeners for each TextView
        textViewIds.forEach { textViewId ->
            findViewById<TextView>(textViewId).setOnClickListener {
                saveSelectedText(it as TextView)
                // Perform any other action if needed
            }
        }

        // Set click listeners for ImageViews
        val imageViewIds = listOf(
            R.id.imageView19, R.id.imageView18,
            R.id.imageView17, R.id.imageView16, R.id.imageView15, R.id.imageView14
        )

        // Set click listeners for each ImageView
        imageViewIds.forEach { imageViewId ->
            findViewById<ImageView>(imageViewId).setOnClickListener {
                onImageViewClick(it as ImageView)
                // Perform any other action if needed
            }
        }
    }

    private fun saveSelectedText(textView: TextView) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            val text = textView.text.toString()
            textViewRef.child(userId).setValue(text)
                .addOnSuccessListener {
                    // Data saved successfully
                    val intent = Intent(this@birthday, customization::class.java)
                    intent.putExtra("selectedText", text) // Pass the selected text as an extra
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    // Failed to save data
                }
        }
    }

    private fun onImageViewClick(imageView: ImageView) {
        // Get the resource ID of the clicked image
        val resourceId = resources.getIdentifier(imageView.tag.toString(), "drawable", packageName)

        // Start the next activity and pass the resource ID as an extra
        val intent = Intent(this, customization::class.java)
        intent.putExtra("imageResource", resourceId)
        startActivity(intent)
    }
}