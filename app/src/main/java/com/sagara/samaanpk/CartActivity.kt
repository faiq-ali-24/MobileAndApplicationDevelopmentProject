package com.sagara.samaanpk

import androidx.activity.ComponentActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class CartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        val startShoppingButton = findViewById<Button>(R.id.startShoppingButton)
        startShoppingButton.setOnClickListener {
            finish()
        }
    }
}
