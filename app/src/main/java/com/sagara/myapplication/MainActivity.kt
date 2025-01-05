package com.sagara.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.View
import android.widget.ImageButton
import androidx.core.view.GravityCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            // Redirect to LoginActivity if the user is not logged in
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, HomeFragment())
                .commit()
        }

        val fabSearch = findViewById<FloatingActionButton>(R.id.fab_search)

        bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.navigation_home -> HomeFragment()
                R.id.navigation_category -> CategoryFragment()
                R.id.navigation_cart -> CartFragment()
                R.id.navigation_profile -> ProfileFragment()
                else -> HomeFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, selectedFragment)
                .commit()
            true
        }

        fabSearch.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, SearchFragment())
                .addToBackStack(null)
                .commit()
        }

        val openDrawerButton: ImageButton = findViewById(R.id.open_drawer_button)
        openDrawerButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            val selectedFragment: Fragment = when (menuItem.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_category -> CategoryFragment()
                R.id.nav_cart -> CartFragment()
                R.id.nav_profile -> ProfileFragment()
                else -> HomeFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, selectedFragment)
                .addToBackStack(null)
                .commit()

            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
