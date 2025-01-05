package com.sagara.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CartFragment : Fragment(R.layout.activity_cart) {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var emptyCartTextView: TextView
    private lateinit var totalPriceTextView: TextView
    private lateinit var payButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseHelper = DatabaseHelper(requireContext())

        cartRecyclerView = view.findViewById(R.id.cartRecyclerView)
        emptyCartTextView = view.findViewById(R.id.cartEmptyBagTextView)
        totalPriceTextView = view.findViewById(R.id.totalPriceTextView)
        payButton = view.findViewById(R.id.payButton)

        val startShoppingButton = view.findViewById<Button>(R.id.startShoppingButton)
        startShoppingButton.setOnClickListener {
            activity?.finish() // Close the activity if it's within an activity container
        }

        // Load cart items
        loadCartItems()

        // Handle the Pay button click
        payButton.setOnClickListener {
            // Show a toast message
            Toast.makeText(requireContext(), "Your order is on its way", Toast.LENGTH_SHORT).show()

            // Clear the cart
            databaseHelper.clearCart()

            // Reload cart items to update the UI
            loadCartItems()
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

            cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            cartRecyclerView.adapter = CartAdapter(cartItems, databaseHelper) { updatedCartItems ->
                // Callback invoked when the cart changes
                recalculateTotalPrice(updatedCartItems)
            }

            // Initial calculation of the total price
            calculateTotalPrice(cartItems) { totalPrice ->
                requireActivity().runOnUiThread { // Ensure UI updates happen on the main thread
                    totalPriceTextView.text = "Total: Rs %.2f".format(totalPrice) // Show total price
                }
            }
        }
    }

    private fun recalculateTotalPrice(cartItems: List<Pair<String, Int>>) {
        calculateTotalPrice(cartItems) { totalPrice ->
            requireActivity().runOnUiThread {
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
