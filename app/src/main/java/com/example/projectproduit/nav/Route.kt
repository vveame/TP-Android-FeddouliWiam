package com.example.projectproduit.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.projectproduit.ui.product.ProductViewModel
import com.example.projectproduit.ui.product.component.ProductDetails
import com.example.projectproduit.ui.product.screen.ProductHomeScreen

object Routes {
    const val Home = "Home Screen"
    const val ProductDetails = "Product Details"
}

@Composable
fun AppNavigation(viewModel: ProductViewModel) {
    val navController = rememberNavController()

    Scaffold { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = Routes.Home
            ) {
                composable(Routes.Home) {
                    ProductHomeScreen(
                        viewModel = viewModel,
                        onNavigateToDetails = { productId ->
                            navController.navigate("${Routes.ProductDetails}/$productId")
                        }
                    )
                }

                composable(
                    "${Routes.ProductDetails}/{productId}",
                    arguments = listOf(navArgument("productId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    ProductDetails(
                        productId = productId,
                        onNavigateToHome = { navController.navigate(Routes.Home) }
                    )
                }
            }
        }
    }
}