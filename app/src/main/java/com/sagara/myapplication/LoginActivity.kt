package com.sagara.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Views
        val emailInput = findViewById<EditText>(R.id.signInEmailView)
        val passwordInput = findViewById<EditText>(R.id.signInPasswordPasswordView)
        val loginButton = findViewById<Button>(R.id.signInSignInButton)
        val resetPasswordTextView = findViewById<TextView>(R.id.signInResetPasswordTextView)
        val signUpTextView = findViewById<TextView>(R.id.signInSignUpTextView)

        // Login button click listener
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                emailInput.error = "Email is required"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                passwordInput.error = "Password is required"
                return@setOnClickListener
            }

            // Sign in with Firebase
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                        // Redirect to the main activity or home screen
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Reset password text view click listener
        resetPasswordTextView.setOnClickListener {
            val email = emailInput.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                emailInput.error = "Please enter your email to reset password"
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Sign-up text view click listener
        signUpTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
