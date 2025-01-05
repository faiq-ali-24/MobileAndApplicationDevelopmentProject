package com.sagara.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Views
        val firstNameInput = findViewById<EditText>(R.id.signUpFirstNameInputView)
        val lastNameInput = findViewById<EditText>(R.id.signUpLastNameInputView)
        val emailInput = findViewById<EditText>(R.id.signUpEmailView)
        val passwordInput = findViewById<EditText>(R.id.signUpPasswordPasswordView)
        val signUpButton = findViewById<Button>(R.id.signUpSignUpButton)
        val loginTextView = findViewById<TextView>(R.id.signUpSignInTextView)

        // Sign-up button click listener
        signUpButton.setOnClickListener {
            val firstName = firstNameInput.text.toString().trim()
            val lastName = lastNameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (TextUtils.isEmpty(firstName)) {
                firstNameInput.error = "First name is required"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(lastName)) {
                lastNameInput.error = "Last name is required"
                return@setOnClickListener
            }

            if (!validateInputs(email, password)) {
                // Validation failed, error logs will provide details
                return@setOnClickListener
            }

            // Register user in Firebase
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        startActivity(intent)
                        val userId = auth.currentUser?.uid
                        val databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)

                        val userMap = hashMapOf(
                            "firstName" to firstName,
                            "lastName" to lastName,
                            "email" to email
                        )

                        databaseRef.setValue(userMap).addOnCompleteListener { dbTask ->
                            if (dbTask.isSuccessful) {
                                Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this, "Failed to save user data: ${dbTask.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Login text view click listener
        loginTextView.setOnClickListener {
            finish() // Close the sign-up activity and return to the login activity
        }
    }

    // Validation functions
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8 && password.any { it.isDigit() } && password.any { it.isLetter() }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        if (!isValidEmail(email)) {
            Log.e("Validation", "Invalid email format.")
            Toast.makeText(this, "Invalid email format.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!isValidPassword(password)) {
            Log.e("Validation", "Password must be at least 8 characters long and include letters and digits.")
            Toast.makeText(this, "Password must be at least 8 characters long and include letters and digits.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
