package com.sagara.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoryProductActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categoryproduct)

        // Initialize DatabaseHelper
        databaseHelper = DatabaseHelper(this)

        val backArrow = findViewById<ImageView>(R.id.back_arrowCategoryProduct)
        backArrow.setOnClickListener {
            onBackPressed()
        }

        // Retrieve the category name from the intent
        val categoryName = intent.getStringExtra("CATEGORY_NAME")

        // Optional: Display the category name in a TextView
        val categoryTitleTextView: TextView = findViewById(R.id.textView3)
        categoryName?.let {
            categoryTitleTextView.text = it
        }

        val recyclerView = findViewById<RecyclerView>(R.id.productRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columns

        // Fetch data from Firebase and set adapter
        fetchCategoryProducts(recyclerView, categoryName)
    }

    private fun fetchCategoryProducts(recyclerView: RecyclerView, categoryName: String?) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Products")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<Product>()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    if (product != null
                                && (product.category.lowercase()
                            .contains(categoryName?.lowercase() as CharSequence) == true
                                || product.tag.lowercase()
                            .contains(categoryName?.lowercase() as CharSequence) == true
                                || product.name.lowercase()
                            .contains(categoryName?.lowercase() as CharSequence) == true
                            )
                    ) {
                        productList.add(product)
                    }
                }
                populateRecyclerView(recyclerView, productList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@CategoryProductActivity,
                    "Failed to load data: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun populateRecyclerView(recyclerView: RecyclerView, productList: List<Product>) {
        // Pass DatabaseHelper to ProductAdapter
        val adapter = ProductAdapter(productList, databaseHelper)
        recyclerView.adapter = adapter
    }
}
