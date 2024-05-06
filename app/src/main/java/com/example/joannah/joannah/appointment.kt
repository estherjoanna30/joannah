package com.example.joannah

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import java.util.*

class appointment : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var appointmentsRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)
        data class Appointment(val date: String, val time: String)


        val calendarView: CalendarView = findViewById(R.id.calendarView)
        val radioGroupTimeSlots: RadioGroup = findViewById(R.id.radioGroupTimeSlots)
        val buttonConfirm: Button = findViewById(R.id.buttonConfirmAppointment)

        // Listener for Confirm Appointment button
        buttonConfirm.setOnClickListener {
            // Get selected date from calendarView
            val selected_Date = convertDate(calendarView.date)

            val selected_Time = findViewById<RadioButton>(radioGroupTimeSlots.checkedRadioButtonId).text.toString()

            val appointment = Appointment(selected_Date, selected_Time)

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                val appointmentsRef = FirebaseDatabase.getInstance().getReference("users/$userId/appointments").push()
                appointmentsRef.setValue(appointment)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Appointment scheduled successfully", Toast.LENGTH_SHORT).show()
                        // Optionally, navigate back to the previous screen
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to schedule appointment", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    // Function to convert selected date to a suitable format
    private fun convertDate(selectedDate: Long): String {
        // Convert the selected date from milliseconds to a suitable format (e.g., YYYY-MM-DD)
        // You can use SimpleDateFormat or other date formatting methods here
        // For simplicity, let's assume we have a method to format the date
        return formatDate(selectedDate)
    }

    // This is a placeholder function, replace it with your actual date formatting logic
    private fun formatDate(dateInMillis: Long): String {
        // Placeholder logic to format date
        return "YYYY-MM-DD" // Replace this with actual date formatting logic
    }
}






