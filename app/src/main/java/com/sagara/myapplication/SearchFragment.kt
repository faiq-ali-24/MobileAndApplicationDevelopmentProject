package com.sagara.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

class SearchFragment : Fragment(R.layout.activity_search) {

    private lateinit var searchInput: EditText
    private lateinit var searchButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchInput = view.findViewById(R.id.search_input)
        searchButton = view.findViewById(R.id.search_button)  // Assuming you have a button to trigger the search

        searchButton.setOnClickListener {
            val categoryName = searchInput.text.toString().trim()

            if (categoryName.isNotEmpty()) {
                val intent = Intent(activity, CategoryProductActivity::class.java)
                intent.putExtra("CATEGORY_NAME", categoryName)
                startActivity(intent)
            }
        }
    }
}
