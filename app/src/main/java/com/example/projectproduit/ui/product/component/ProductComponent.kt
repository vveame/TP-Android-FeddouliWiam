package com.example.projectproduit.ui.product.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectproduit.data.entities.Product
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import com.example.projectproduit.ui.cart.CartViewModel

@Composable
fun ProductList(products: List<Product>, onNavigateToDetails: (String) -> Unit, cartViewModel: CartViewModel){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                ProductItem(product,
                    onNavigateToDetails = onNavigateToDetails,
                    cartViewModel = cartViewModel
                )
            }
        }
    }
}
