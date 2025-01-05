package com.sagara.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(
    private val productList: List<Product>,
    private val databaseHelper: DatabaseHelper // Pass DatabaseHelper instance
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val addToCartButton: Button = itemView.findViewById(R.id.addToCartButton7)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.productName.text = product.name
        holder.productPrice.text = "Price: RS ${product.price}"

        // Use Glide to load the product image from a URL
        Glide.with(holder.itemView.context)
            .load(product.image)
            .into(holder.productImage)

        // Handle click on the product image to navigate to ProductActivity
        holder.productImage.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ProductActivity::class.java)
            intent.putExtra("PRODUCT_ID", product.id) // Pass the product ID
            context.startActivity(intent)
        }

        // Handle add to cart button click
        holder.addToCartButton.setOnClickListener {
            val currentCount = databaseHelper.getAllProducts().find { it.first == product.id }?.second ?: 0
            val success = if (currentCount > 0) {
                databaseHelper.updateProductCount(product.id, currentCount + 1)
            } else {
                databaseHelper.insertProduct(product.id, 1)
            }

            if (success) {
                Toast.makeText(
                    holder.itemView.context,
                    "${product.name} added to cart",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    holder.itemView.context,
                    "Failed to add ${product.name} to cart",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getItemCount(): Int = productList.size
}
