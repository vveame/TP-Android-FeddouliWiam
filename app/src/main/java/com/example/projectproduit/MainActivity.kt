package com.example.projectproduit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.projectproduit.ui.theme.ProjectProduitTheme
import com.example.projectproduit.nav.AppNavigation
import com.example.projectproduit.ui.cart.CartViewModel
import com.example.projectproduit.ui.product.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val productViewModel : ProductViewModel by viewModels<ProductViewModel>()
    val cartViewModel : CartViewModel by viewModels<CartViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectProduitTheme {
                AppNavigation(productViewModel, cartViewModel)
            }
        }
    }
}
