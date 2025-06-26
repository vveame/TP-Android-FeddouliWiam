package com.example.projectproduit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.example.projectproduit.ui.theme.ProjectProduitTheme
import com.example.projectproduit.nav.AppNavigation
import com.example.projectproduit.ui.cart.CartViewModel
import com.example.projectproduit.ui.order.OrderViewModel
import com.example.projectproduit.ui.product.ProductViewModel
import com.example.projectproduit.ui.user.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val productViewModel : ProductViewModel by viewModels<ProductViewModel>()
    val cartViewModel : CartViewModel by viewModels<CartViewModel>()
    val orderViewModel : OrderViewModel by viewModels< OrderViewModel>()
    val userViewModel: UserViewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectProduitTheme {
                val userId = userViewModel.state.collectAsState().value.loggedInUser?.userId
                AppNavigation(productViewModel, cartViewModel, orderViewModel, userViewModel, userId)
            }
        }
    }
}
