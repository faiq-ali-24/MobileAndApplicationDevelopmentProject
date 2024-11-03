package com.sagara.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.ImageView

class HomeFragment : Fragment(R.layout.activity_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val icon1 = view.findViewById<ImageView>(R.id.icon1)
        icon1.setOnClickListener {
            val intent = Intent(activity, CategoryProductActivity::class.java)
            startActivity(intent)
        }


        val watchImage = view.findViewById<ImageView>(R.id.watchImage)
        watchImage.setOnClickListener {
            val intent = Intent(activity, ProductActivity::class.java)
            startActivity(intent)
        }


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
    }
}
