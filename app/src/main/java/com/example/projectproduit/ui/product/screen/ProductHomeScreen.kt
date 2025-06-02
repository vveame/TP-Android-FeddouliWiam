package com.example.projectproduit.ui.product.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.projectproduit.ui.product.ProductIntent
import com.example.projectproduit.ui.product.ProductViewModel
import com.example.projectproduit.ui.product.component.ProductList
import androidx.compose.runtime.getValue

@Composable
fun ProductHomeScreen(viewModel: ProductViewModel, onNavigateToDetails: (String) -> Unit) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.handleIntent(ProductIntent.LoadProducts)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        when {
            state.isLoading -> {
                // Display a Circular loader
                CircularProgressIndicator(modifier = Modifier.align(CenterHorizontally))
            }

            state.error != null -> {
                // Display an error message
                Text(text = "Error: ${state.error}", color = Color.Red)
            }

            else -> {
                // Display products when fetch is success
                ProductList(products = state.products, onNavigateToDetails)
            }
        }
    }
}