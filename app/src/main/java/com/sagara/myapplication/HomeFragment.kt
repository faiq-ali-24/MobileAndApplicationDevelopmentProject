package com.sagara.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment(R.layout.activity_home) {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseHelper = DatabaseHelper(requireContext())

        // Define a reusable function to set up the click listener
        fun setIconClickListener(iconId: Int, categoryName: String) {
            val icon = view.findViewById<ImageView>(iconId)
            icon.setOnClickListener {
                val intent = Intent(activity, CategoryProductActivity::class.java).apply {
                    putExtra("CATEGORY_NAME", categoryName)
                }
                startActivity(intent)
            }
        }

        // Set click listeners for each icon
        setIconClickListener(R.id.watch, "Watches")
        setIconClickListener(R.id.earbuds, "Earbuds")
        setIconClickListener(R.id.chargers, "Chargers")
        setIconClickListener(R.id.gadgets, "Gadgets")
        setIconClickListener(R.id.defense, "Defense")
        setIconClickListener(R.id.speakers, "Speakers")

        // Existing listeners for other icons
        val shoppingCartIcon = view.findViewById<ImageView>(R.id.shoppingCartIcon)
        shoppingCartIcon.setOnClickListener {
            val intent = Intent(activity, CartActivity::class.java)
            startActivity(intent)
        }

        val bellIcon = view.findViewById<ImageView>(R.id.bellIcon)
        bellIcon.setOnClickListener {
            val intent = Intent(activity, NotificationActivity::class.java)
            startActivity(intent)
        }

        // Fetch and display products
        fetchProducts(view)
        fetchProductsSelling(view)
        fetchTechGadgets(view)
    }

    private fun fetchProducts(view: View) {
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
                populateProducts(view, productList)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchProductsSelling(view: View) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Products")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<Product>()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    if (product != null && product.tag == "BestSelling") {
                        productList.add(product)
                    }
                }
                populateHorizontalScrollView(view, productList)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchTechGadgets(view: View) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Products")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<Product>()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    if (product != null && product.tag == "TechGadgets") {
                        productList.add(product)
                    }
                }
                populateTechGadgets(view, productList)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun populateHorizontalScrollView(view: View, productList: List<Product>) {
        val horizontalContainer = view.findViewById<LinearLayout>(R.id.horizontalCardContainer)
        horizontalContainer.removeAllViews()

        val layoutInflater = LayoutInflater.from(requireContext())
        for (product in productList) {
            val cardView = layoutInflater.inflate(R.layout.card_product_nobutton, horizontalContainer, false) as CardView
            val productName = cardView.findViewById<TextView>(R.id.productName2)
            val productPrice = cardView.findViewById<TextView>(R.id.productPrice2)
            val productImage = cardView.findViewById<ImageView>(R.id.productImage2)

            productName.text = product.name
            productPrice.text = "Price: RS ${product.price}"
            Glide.with(requireContext()).load(product.image).into(productImage)

            productImage.setOnClickListener {
                val intent = Intent(view.context, ProductActivity::class.java)
                intent.putExtra("PRODUCT_ID", product.id)
                view.context.startActivity(intent)
            }

            horizontalContainer.addView(cardView)
        }
    }

    private fun populateTechGadgets(view: View, productList: List<Product>) {
        val linearLayout = view.findViewById<LinearLayout>(R.id.dynamicLinearLayout)
        linearLayout.removeAllViews()

        val inflater = LayoutInflater.from(view.context)
        for (product in productList) {
            val cardView = inflater.inflate(R.layout.item_tech_gadget_card, linearLayout, false) as CardView
            val imageView = cardView.findViewById<ImageView>(R.id.productImage3)
            val productNameTextView = cardView.findViewById<TextView>(R.id.productName3)
            val productPriceTextView = cardView.findViewById<TextView>(R.id.productPrice3)

            productNameTextView.text = product.name
            productPriceTextView.text = "Price: RS ${product.price}"
            Glide.with(view.context).load(product.image).into(imageView)

            imageView.setOnClickListener {
                val intent = Intent(view.context, ProductActivity::class.java)
                intent.putExtra("PRODUCT_ID", product.id)
                view.context.startActivity(intent)
            }

            linearLayout.addView(cardView)
        }
    }

    private fun populateProducts(view: View, productList: List<Product>) {
        val cardContainer = view.findViewById<LinearLayout>(R.id.cardContainer)
        cardContainer.removeAllViews()

        val layoutInflater = LayoutInflater.from(requireContext())
        for (product in productList) {
            val cardView = layoutInflater.inflate(R.layout.card_product, cardContainer, false) as CardView
            val productName = cardView.findViewById<TextView>(R.id.productName1)
            val productPrice = cardView.findViewById<TextView>(R.id.productPrice1)
            val productImage = cardView.findViewById<ImageView>(R.id.productImage1)
            val addToCartButton = cardView.findViewById<Button>(R.id.addToCartButton1)

            productName.text = product.name
            productPrice.text = "Price: RS ${product.price}"
            Glide.with(requireContext()).load(product.image).into(productImage)

            productImage.setOnClickListener {
                val intent = Intent(view.context, ProductActivity::class.java)
                intent.putExtra("PRODUCT_ID", product.id)
                view.context.startActivity(intent)
            }

            addToCartButton.setOnClickListener {
                addToCart(product)
            }

            cardContainer.addView(cardView)
        }
    }

    private fun addToCart(product: Product) {
        val currentCount = databaseHelper.getAllProducts().find { it.first == product.id }?.second ?: 0
        val success = if (currentCount > 0) {
            databaseHelper.updateProductCount(product.id, currentCount + 1)
        } else {
            databaseHelper.insertProduct(product.id, 1)
        }

        if (success) {
            Toast.makeText(requireContext(), "${product.name} added to cart", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Failed to add ${product.name} to cart", Toast.LENGTH_SHORT).show()
        }
    }
}
