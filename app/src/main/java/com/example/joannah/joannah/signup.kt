package com.example.joannah

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class signup : AppCompatActivity() {
    private lateinit var usernameTextView: TextView
    private lateinit var passwordTextView: TextView
    private lateinit var confirmPasswordTextView: TextView
    private lateinit var registerButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance()
        usersRef = database.reference.child("users")

        // Initialize views
        usernameTextView = findViewById(R.id.editTextTextEmailAddress2)
        passwordTextView = findViewById(R.id.editTextTextPassword2)
        confirmPasswordTextView = findViewById(R.id.editTextTextPassword3)
        registerButton = findViewById(R.id.button4)

        // Set click listener for register button
        registerButton.setOnClickListener {
            register() // Call register function
        }
    }

    object EmailValidator {
        fun isValidEmail(target: CharSequence?): Boolean {
            return target != null && Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    private fun register() {
        val username = usernameTextView.text.toString()
        val password = passwordTextView.text.toString()
        val confirmPassword = confirmPasswordTextView.text.toString()

        // Perform validation
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (!EmailValidator.isValidEmail(username)) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        // Firebase Authentication: Create a new user with email and password
        firebaseAuth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                    // Save user data to Firebase Realtime Database
                    val userId = firebaseAuth.currentUser?.uid
                    userId?.let {
                        val userData = HashMap<String, Any>()
                        userData["email"] = username

                        usersRef.child(userId).setValue(userData)
                            .addOnSuccessListener {
                                // Data saved successfully
                                Toast.makeText(this, "User data saved successfully", Toast.LENGTH_SHORT).show()

                                // Navigate to LoginActivity
                                val intent = Intent(this, LOGIN::class.java)
                                startActivity(intent)
                                finish() // Close the current activity
                            }
                            .addOnFailureListener { e ->
                                // Failed to save data
                                Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    // If registration fails, display a message to the user.
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}