package com.sagara.myapplication

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

class CategoryProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categoryproduct)
        val backArrow = findViewById<ImageView>(R.id.back_arrowCategoryProduct)


        backArrow.setOnClickListener {
            onBackPressed()
        }

        val productImageView: ImageView = findViewById(R.id.productImageView)

        productImageView.setOnClickListener {
            val intent = Intent(this, ProductActivity::class.java)
            startActivity(intent)
        }
    }
}