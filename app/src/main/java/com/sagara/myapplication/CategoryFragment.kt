package com.sagara.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoryFragment : Fragment(R.layout.activity_category) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private val categories = mutableListOf<Category>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewCategories)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        categoryAdapter = CategoryAdapter(categories)
        recyclerView.adapter = categoryAdapter

        fetchCategories()
    }

    private fun fetchCategories() {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Categories")

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("FirebaseDB", "Snapshot: ${snapshot.value}")
                categories.clear()
                for (categorySnapshot in snapshot.children) {
                    val category = categorySnapshot.getValue(Category::class.java)
                    Log.d("FirebaseDB", "Category fetched: $category")
                    if (category != null) {
                        categories.add(category)
                    }
                }
                Log.d("FirebaseDB", "Categories list size: ${categories.size}")
                categoryAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseDB", "Error fetching data: ${error.message}")
            }
        })
    }

}
