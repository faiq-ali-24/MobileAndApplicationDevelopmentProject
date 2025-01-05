package com.sagara.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileFragment : Fragment(R.layout.activity_profile) {

    private lateinit var profileSettingsTextView: TextView
    private lateinit var database: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        profileSettingsTextView = view.findViewById(R.id.profileSettingsTextView)

        // Get user information (Assuming user is logged in)
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            database = FirebaseDatabase.getInstance().getReference("Users").child(userId)

            // Fetch user details from Firebase Realtime Database
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Check if data exists
                    if (snapshot.exists()) {
                        val firstName = snapshot.child("firstName").getValue(String::class.java)
                        val lastName = snapshot.child("lastName").getValue(String::class.java)

                        // Set the user's full name as the title
                        val userName = "$firstName $lastName"
                        profileSettingsTextView.text = "Hello, $userName"  // Displaying "Hello, [User's Name]"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error if needed
                }
            })
        }
    }
}
