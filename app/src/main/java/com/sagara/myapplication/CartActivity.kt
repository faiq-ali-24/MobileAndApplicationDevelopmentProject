package com.sagara.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.time.times

class CartActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var emptyCartTextView: TextView
    private lateinit var totalPriceTextView: TextView
    private lateinit var payButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        databaseHelper = DatabaseHelper(this)

        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        emptyCartTextView = findViewById(R.id.cartEmptyBagTextView)
        totalPriceTextView = findViewById(R.id.totalPriceTextView)
        payButton = findViewById(R.id.payButton)

        val startShoppingButton = findViewById<Button>(R.id.startShoppingButton)
        startShoppingButton.setOnClickListener {
            finish()
        }

        loadCartItems()

        payButton.setOnClickListener {
            // Show a toast message
            Toast.makeText(this, "Your order is on its way", Toast.LENGTH_SHORT).show()

            // Get the current user's ID (Assuming you're using Firebase Authentication for this)
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            // Prepare the order data (productId and quantity)
            val orderItems = mutableMapOf<String, Int>()
            val cartItems = databaseHelper.getAllProducts() // Get cart items from the database helper

            for ((productId, quantity) in cartItems) {
                orderItems[productId] = quantity // Add productId and quantity to the order map
            }

            // Store the order in Firebase
            if (userId != null) {
                val databaseRef = FirebaseDatabase.getInstance().getReference("Orders").child(userId)
                val orderId = databaseRef.push().key // Generate a unique ID for the order

                if (orderId != null) {
                    // Store the order details under the user ID and the generated order ID
                    databaseRef.child(orderId).setValue(orderItems).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Clear the cart after the order is successfully stored
                            databaseHelper.clearCart()

                            // Reload cart items to update the UI
                            loadCartItems()
                            Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            // Handle failure in storing the order
                            Toast.makeText(this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                // Handle the case where user is not logged in
                Toast.makeText(this, "Please log in to place an order.", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun loadCartItems() {
        val cartItems = databaseHelper.getAllProducts().toMutableList() // Convert to MutableList

        if (cartItems.isEmpty()) {
            emptyCartTextView.visibility = View.VISIBLE
            cartRecyclerView.visibility = View.GONE
            totalPriceTextView.visibility = View.GONE // Hide total price
            payButton.visibility = View.GONE         // Hide pay button
        } else {
            emptyCartTextView.visibility = View.GONE
            cartRecyclerView.visibility = View.VISIBLE
            totalPriceTextView.visibility = View.VISIBLE // Show total price
            payButton.visibility = View.VISIBLE         // Show pay button

            cartRecyclerView.layoutManager = LinearLayoutManager(this)
            cartRecyclerView.adapter = CartAdapter(cartItems, databaseHelper) { updatedCartItems ->
                // Callback invoked when the cart changes
                recalculateTotalPrice(updatedCartItems)
            }

            // Initial calculation of the total price
            calculateTotalPrice(cartItems) { totalPrice ->
                runOnUiThread { // Ensure UI updates happen on the main thread
                    totalPriceTextView.text = "Total: Rs %.2f".format(totalPrice) // Show total price
                }
            }
        }
    }


    private fun recalculateTotalPrice(cartItems: List<Pair<String, Int>>) {
        calculateTotalPrice(cartItems) { totalPrice ->
            runOnUiThread {
                totalPriceTextView.text = "Total: Rs %.2f".format(totalPrice)

                // Hide total and pay button if the cart is now empty
                if (cartItems.isEmpty()) {
                    totalPriceTextView.visibility = View.GONE
                    payButton.visibility = View.GONE
                }
            }
        }
    }



    private fun calculateTotalPrice(
        cartItems: List<Pair<String, Int>>,
        callback: (Double) -> Unit
    ) {
        var total = 0.0 // Use Double for total to match product.price type
        var processedItems = 0

        // Iterate through cart items to fetch prices
        for ((productId, quantity) in cartItems) {
            getProductById(productId) { product ->
                if (product != null) {
                    total += product.price * quantity // Perform multiplication
                }

                processedItems++
                // When all items are processed, invoke the callback with the total price
                if (processedItems == cartItems.size) {
                    callback(total)
                }
            }
        }

        // If the cart is empty, immediately call the callback with 0
        if (cartItems.isEmpty()) {
            callback(0.0)
        }
    }



    private fun getProductById(productId: String, callback: (Product?) -> Unit) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Products")
        databaseRef.child(productId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val product = snapshot.getValue(Product::class.java)
                callback(product) // Return the product via the callback
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null) // Return null in case of an error
            }
        })
    }


}
