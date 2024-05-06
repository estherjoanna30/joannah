package com.example.joannah

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import java.util.*

class customization : AppCompatActivity() {

    private lateinit var nameTextView: TextView
    private lateinit var custNameEditText: EditText
    private lateinit var radioGroupWeight: RadioGroup
    private lateinit var radioGroupTier: RadioGroup
    private lateinit var typeTextView: TextView
    private lateinit var editTextDate3: EditText // Corrected variable name
    private lateinit var spinner: Spinner
    private lateinit var confirmButton: Button

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customization)
        val selectedText = intent.getStringExtra("selectedText")
        val textView: TextView = findViewById(R.id.name)

        // Set the text to the TextView
        textView.text = selectedText // Set the selected text



        // Initialize Firebase
        database = FirebaseDatabase.getInstance().reference.child("customization_data")




        // Initialize Views
        nameTextView = findViewById(R.id.name)
        custNameEditText = findViewById(R.id.custname)
        radioGroupWeight = findViewById(R.id.radioGroup)
        radioGroupTier = findViewById(R.id.radioGroup2)

        editTextDate3 = findViewById(R.id.editTextDate3) // Corrected variable name
        spinner = findViewById(R.id.spinner)
        confirmButton = findViewById(R.id.confirmapp)

        // Initialize Spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.spinner_options, // Replace with your array resource
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Set click listener for the Confirm button
        confirmButton.setOnClickListener {

            // Handle customization confirmation
            confirmCustomization()
        }

        editTextDate3.setOnClickListener {
            selectDate()
        }
    }

    private fun selectDate() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                if (isDateValid(selectedDate)) {
                    editTextDate3.setText(selectedDate) // Corrected variable name
                } else {
                    Toast.makeText(this, "Please select a valid date", Toast.LENGTH_SHORT).show()
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun isDateValid(selectedDate: String): Boolean {
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        val selectedDateParts = selectedDate.split("/")
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(Calendar.YEAR, selectedDateParts[2].toInt())
        selectedCalendar.set(Calendar.MONTH, selectedDateParts[1].toInt() - 1)
        selectedCalendar.set(Calendar.DAY_OF_MONTH, selectedDateParts[0].toInt())

        return selectedCalendar.timeInMillis >= today.timeInMillis
    }

    private fun confirmCustomization() {
        // Get values from EditTexts
        val name = nameTextView.text.toString()
        val custName = custNameEditText.text.toString()

        val selectedDate = editTextDate3.text.toString() // Corrected variable name

        // Get selected weight option
        val selectedWeightId = radioGroupWeight.checkedRadioButtonId
        val selectedWeightOption = findViewById<RadioButton>(selectedWeightId)?.text.toString()

        // Get selected tier option
        val selectedTierId = radioGroupTier.checkedRadioButtonId
        val selectedTierOption = findViewById<RadioButton>(selectedTierId)?.text.toString()

        // Get selected spinner option
        val selectedSpinnerOption = spinner.selectedItem.toString()

        // Save to Firebase with timestamp
        val customizationData = hashMapOf(
            "type" to name,
            "custName" to custName,

            "date" to selectedDate,
            "weight" to selectedWeightOption,
            "tier" to selectedTierOption,
            "spinner_option" to selectedSpinnerOption,
            "timestamp" to ServerValue.TIMESTAMP // Add timestamp
        )

        val customizationRef = database.push()
        customizationRef.setValue(customizationData)
        // Redirect to MainActivity
        startActivity(Intent(this, report::class.java))
        finish() // Optional, to close this activity
    }

    fun ClickMenu(view: View) {
        // Redirect to home page
        val intent = Intent(this, report::class.java)
        startActivity(intent)
        // Finish the current activity if necessary
        finish()
    }
}