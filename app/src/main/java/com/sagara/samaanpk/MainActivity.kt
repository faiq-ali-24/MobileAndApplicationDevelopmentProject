package com.sagara.samaanpk

import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sagara.samaanpk.ui.theme.SamaanPkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Alignment
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SamaanPkTheme {
                MainScreen()
            }
        }
    }

}


@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Box {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController)
            }
        ) { innerPadding ->
            NavigationHost(navController = navController, modifier = Modifier.padding(innerPadding))
        }


        FloatingActionButton(
            onClick = {
                navController.navigate("search")
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = -32.dp),
            shape = CircleShape,
            containerColor = Color.White
        ) {
            Icon(
                painter = painterResource(id = R.drawable.search_24px),
                contentDescription = "Search",
                tint = Color.Black
            )
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf("home", "category", "cart", "profile")
    val backStackEntry by navController.currentBackStackEntryAsState()

    val icons = listOf(
        R.drawable.home_24px,
        R.drawable.category_24px,
        R.drawable.shopping_cart_24px,
        R.drawable.person_24px
    )

    BottomNavigation(
        backgroundColor = Color.White
    ) {
        items.forEachIndexed { index, screen ->
            BottomNavigationItem(
                icon = {
                    Image(
                        painter = painterResource(id = icons[index]),
                        contentDescription = null
                    )
                },
                selected = backStackEntry?.destination?.route == screen,
                onClick = {
                    navController.navigate(screen) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Gray
            )
        }
    }
}



@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") { HomeScreen() }
        composable("category") { CategoryScreen() }
        composable("search") { SearchScreen() }
        composable("cart") { CartScreen() }
        composable("profile") { ProfileScreenMainActivity() }
    }
}


@Composable
fun CartScreen() {
    val context = LocalContext.current
    AndroidView(factory = { ctx ->
        LayoutInflater.from(ctx).inflate(R.layout.activity_cart, null).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    })
}



@Composable
fun HomeScreen() {
    val context = LocalContext.current
    AndroidView(factory = { ctx ->
        LayoutInflater.from(ctx).inflate(R.layout.activity_home, null).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            val bellIcon = findViewById<ImageView>(R.id.bellIcon)
            bellIcon.setOnClickListener {
                val intent = Intent(context, NotificationActivity::class.java)
                context.startActivity(intent)
            }
            val shoppingCartIcon = findViewById<ImageView>(R.id.shoppingCartIcon)
            shoppingCartIcon.setOnClickListener {
                val intent = Intent(context, CartActivity::class.java)
                context.startActivity(intent)
            }
        }
    })
}

@Composable
fun CategoryScreen() {
    val context = LocalContext.current
    AndroidView(factory = { ctx ->
        LayoutInflater.from(ctx).inflate(R.layout.activity_category, null).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    })
}

@Composable
fun SearchScreen() {
    androidx.compose.material3.Text(
        text = "Search Screen",
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
fun ProfileScreenMainActivity() {
    val context = LocalContext.current
    AndroidView(factory = { ctx ->
        LayoutInflater.from(ctx).inflate(R.layout.activity_profile, null).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    })
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SamaanPkTheme {
        MainScreen()
    }
}
