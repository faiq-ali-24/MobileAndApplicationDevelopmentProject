package com.sagara.myapplication

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;


class ProductActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val cancelIcon = findViewById<ImageView>(R.id.cancel_iconProduct)
        cancelIcon.setOnClickListener {
            finish()
        }
    }
}