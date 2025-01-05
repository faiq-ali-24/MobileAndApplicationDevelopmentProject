package com.sagara.myapplication


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

class NotificationActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val backArrowButton = findViewById<ImageView>(R.id.backArrowButton)


        backArrowButton.setOnClickListener {
            finish()
        }

    }

}
