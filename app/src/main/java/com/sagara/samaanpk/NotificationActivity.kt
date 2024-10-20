package com.sagara.samaanpk

import androidx.activity.ComponentActivity
import android.os.Bundle
import android.widget.ImageView

class NotificationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val backArrowButton = findViewById<ImageView>(R.id.notificationBlackBarImageView)
        backArrowButton.setOnClickListener {
            finish()
        }
    }
}
