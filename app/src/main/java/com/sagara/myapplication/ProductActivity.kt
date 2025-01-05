package com.sagara.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.appcompat.app.AppCompatActivity

class ProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        // Get the Product ID passed from the intent
        val productId = intent.getStringExtra("PRODUCT_ID")

        if (productId != null) {
            // Fetch product details from Firebase
            fetchProductDetails(productId)
            fetchAllProducts()
        } else {
            Toast.makeText(this, "No Product ID Found", Toast.LENGTH_SHORT).show()
        }

        val cancelIcon = findViewById<ImageView>(R.id.cancel_iconProduct)
        cancelIcon.setOnClickListener {
            finish()
        }
    }

    private fun fetchProductDetails(productId: String) {
        // Reference to the specific product in Firebase
        val databaseRef = FirebaseDatabase.getInstance().getReference("Products/$productId")

        // Fetch data from Firebase
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val product = snapshot.getValue(Product::class.java)
                if (product != null) {
                    // Populate the UI with product details
                    populateProductUI(product)
                } else {
                    Toast.makeText(this@ProductActivity, "Product not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProductActivity, "Failed to load product", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun populateProductUI(product: Product) {
        // Get references to the UI elements
        val productName = findViewById<TextView>(R.id.textViewProductDescription)
        val productPrice = findViewById<TextView>(R.id.textViewPrice)
        val productImage = findViewById<ImageView>(R.id.productImage4)
        val productImage2 = findViewById<ImageView>(R.id.e2)
        val productDescription = findViewById<TextView>(R.id.paragraphTextView)
        val productCategory = findViewById<TextView>(R.id.categoryPowerBanksAndCharger)
        val productTag = findViewById<TextView>(R.id.tags20wCharger)

        // Set the product details to the UI elements
        productName.text = product.name
        productPrice.text = "Price: RS ${product.price}"
        productDescription.text = product.description
        productCategory.text = product.category
        productTag.text = product.tag

        // Use Glide to load the product image from URL (if available)
        Glide.with(this)
            .load(product.image) // Use URL from Firebase or placeholder if necessary
            .into(productImage)
        Glide.with(this)
            .load(product.image) // Use URL from Firebase or placeholder if necessary
            .into(productImage2)
    }

    private fun fetchAllProducts() {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Products")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<Product>()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    if (product != null) {
                        productList.add(product)
                    }
                }
                populateTechGadgets(productList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProductActivity, "Failed to load products", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun populateTechGadgets(productList: List<Product>) {
        val horizontalScrollView = findViewById<HorizontalScrollView>(R.id.cardScrollView)
        val containerLayout = findViewById<LinearLayout>(R.id.cardContainerLayout)

        // Clear previous views
        containerLayout.removeAllViews()

        for (product in productList) {
            val cardView = LayoutInflater.from(this).inflate(R.layout.product_card_layout, containerLayout, false)

            val imageView = cardView.findViewById<ImageView>(R.id.productImage5)
            val nameTextView = cardView.findViewById<TextView>(R.id.productName5)
            val priceTextView = cardView.findViewById<TextView>(R.id.productPrice5)
            val addToCartButton = cardView.findViewById<Button>(R.id.addToCartButton5)

            // Set product details
            nameTextView.text = product.name
            priceTextView.text = "Price: RS ${product.price}"
            Glide.with(this).load(product.image).into(imageView)

            // Add functionality to the button
            addToCartButton.setOnClickListener {
                Toast.makeText(this, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
            }

            // Add the card to the container layout
            containerLayout.addView(cardView)
        }
    }




}
