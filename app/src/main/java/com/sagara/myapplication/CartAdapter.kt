package com.sagara.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartAdapter(
    private val cartItems: MutableList<Pair<String, Int>>, // Use MutableList for dynamic updates
    private val databaseHelper: DatabaseHelper,
    private val onCartUpdated: (List<Pair<String, Int>>) -> Unit // Callback to notify cart updates
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cartItemImage: ImageView = itemView.findViewById(R.id.cartItemImage)
        val cartItemName: TextView = itemView.findViewById(R.id.cartItemName)
        val cartItemPrice: TextView = itemView.findViewById(R.id.cartItemPrice)
        val cartItemQuantity: TextView = itemView.findViewById(R.id.cartItemQuantity)
        val removeButton: Button = itemView.findViewById(R.id.removeCartItemButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart_product, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val (productId, quantity) = cartItems[position]

        // Fetch product details from Firebase
        getProductById(productId) { product ->
            if (product != null) {
                holder.cartItemName.text = product.name
                holder.cartItemPrice.text = "Price: Rs ${product.price}"
                holder.cartItemQuantity.text = "Quantity: $quantity"
                Glide.with(holder.itemView.context).load(product.image).into(holder.cartItemImage)
            } else {
                holder.cartItemName.text = "Product not found"
                holder.cartItemPrice.text = ""
                holder.cartItemQuantity.text = ""
            }
        }

        holder.removeButton.setOnClickListener {
            // Update SQLite database
            databaseHelper.deleteProduct(productId)

            // Remove the item from the cartItems list
            cartItems.removeAt(position)

            // Notify adapter of item removal
            notifyItemRemoved(position)

            // Notify adapter of changes in the list to refresh subsequent items
            notifyItemRangeChanged(position, cartItems.size)

            // Invoke the callback to notify the activity of the updated cart
            onCartUpdated(cartItems)
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

    override fun getItemCount(): Int = cartItems.size
}
