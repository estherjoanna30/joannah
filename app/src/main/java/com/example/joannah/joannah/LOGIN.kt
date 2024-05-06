package com.example.joannah

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent // Import Intent class
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LOGIN: AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize EditText, loginButton, and signupButton
        usernameEditText = findViewById(R.id.editTextTextEmailAddress)
        passwordEditText = findViewById(R.id.editTextTextPassword)
        loginButton = findViewById(R.id.button2)
        signupButton = findViewById(R.id.button3)

        // Set click listener for loginButton
        loginButton.setOnClickListener {
            val enteredUsername = usernameEditText.text.toString()
            val enteredPassword = passwordEditText.text.toString()
            val correctUsername = "joannah02@gmail.com"
            val correctPassword = "cc@1010"

            // Authenticate user using Firebase
            firebaseAuth.signInWithEmailAndPassword(enteredUsername, enteredPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // If login is successful, navigate to HomeActivity
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LOGIN, home::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If login fails, display a message to the user.
                        Toast.makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        // Set click listener for signupButton
        signupButton.setOnClickListener {
            // Start the RegistrationActivity when signup button is clicked
            val intent = Intent(this@LOGIN, signup::class.java)
            startActivity(intent)
        }
    }
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}


