package com.example.projectproduit.ui.product.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.projectproduit.ui.product.ProductIntent
import com.example.projectproduit.ui.product.ProductViewModel

@Composable
fun ProductDetails(
    productId: String,
    viewModel: ProductViewModel,
    onNavigateToHome: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    // Charger le produit par ID une seule fois
    LaunchedEffect(productId) {
        viewModel.handleIntent(ProductIntent.LoadProductById(productId))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            state.isLoading -> {
                androidx.compose.material3.CircularProgressIndicator(
                    modifier = Modifier.align(androidx.compose.ui.Alignment.CenterHorizontally)
                )
            }

            state.error != null -> {
                Text(
                    text = "Erreur : ${state.error}",
                    color = androidx.compose.ui.graphics.Color.Red
                )
            }

            state.selectedProduct != null -> {
                val product = state.selectedProduct!!

                // Image
                AsyncImage(
                    model = product.productImage,
                    contentDescription = product.productTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Détails du produit
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = product.productTitle,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${"%.2f".format(product.productPrice)} DH",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        Text(
                            text = "En stock : ",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = product.productQuantity.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = onNavigateToHome,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text("Retour aux produits")
                    }
                }
            }
        }
    }
}