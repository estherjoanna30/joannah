package com.example.joannah

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class report : AppCompatActivity() {

    private lateinit var textViewCustName: TextView
    private lateinit var textViewType: TextView
    private lateinit var textViewWeight: TextView
    private lateinit var textViewDate: TextView
    private lateinit var textViewTier: TextView
    private lateinit var textViewTime: TextView

    private lateinit var database: FirebaseDatabase
    private lateinit var customizationRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        // Initialize Firebase database
        database = FirebaseDatabase.getInstance()
        customizationRef = database.reference.child("customization_data")

        // Initialize TextViews
        textViewCustName = findViewById(R.id.textViewCustName)
        textViewType = findViewById(R.id.textViewType)
        textViewWeight = findViewById(R.id.textViewWeight)
        textViewDate = findViewById(R.id.textViewDate)
        textViewTier = findViewById(R.id.textViewtier)
        textViewTime = findViewById(R.id.textViewTime)

        // Retrieve the most recently saved data from "customization_data" node
        customizationRef.orderByChild("timestamp").limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (customizationSnapshot in dataSnapshot.children) {
                            val custName =
                                customizationSnapshot.child("custName").getValue(String::class.java)
                            val type =
                                customizationSnapshot.child("type").getValue(String::class.java)
                            val weight =
                                customizationSnapshot.child("weight").getValue(String::class.java)
                            val date =
                                customizationSnapshot.child("date").getValue(String::class.java)
                            val tier =
                                customizationSnapshot.child("tier").getValue(String::class.java)
                            val spinnerOption =
                                customizationSnapshot.child("spinner_option")
                                    .getValue(String::class.java)

                            // Set retrieved data to TextViews
                            textViewCustName.text = "Customer Name: $custName"
                            textViewType.text = "Theme Selected: $type"
                            textViewWeight.text = "With Photography: $weight"
                            textViewDate.text = "Selected Date: $date"
                            textViewTier.text = "Complimentary: $tier"
                            textViewTime.text = "Selected Time: $spinnerOption"

                            // Log retrieved data for debugging
                            Log.d("ReportActivity", "Customer Name: $custName")
                            Log.d("ReportActivity", "Type: $type")
                            Log.d("ReportActivity", "Weight: $weight")
                            Log.d("ReportActivity", "Date: $date")
                            Log.d("ReportActivity", "Tier: $tier")
                            Log.d("ReportActivity", "Spinner Option: $spinnerOption")

                            // Set the title of the activity to customer's name
                            title = custName
                        }
                    } else {
                        Log.d("ReportActivity", "No data found in customization_data node")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(
                        "ReportActivity",
                        "Error retrieving data from customization_data node: $databaseError"
                    )
                }
            })
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this@report, MainActivity::class.java)
            startActivity(intent)
        }
    }
}