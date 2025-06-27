package com.example.projectproduit.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.projectproduit.data.entities.UserFormMode
import com.example.projectproduit.ui.AdminScreen
import com.example.projectproduit.ui.cart.CartViewModel
import com.example.projectproduit.ui.cart.screen.CartScreen
import com.example.projectproduit.ui.order.OrderViewModel
import com.example.projectproduit.ui.order.screen.CheckoutScreen
import com.example.projectproduit.ui.order.screen.OrderScreen
import com.example.projectproduit.ui.product.ProductViewModel
import com.example.projectproduit.ui.product.component.ProductDetails
import com.example.projectproduit.ui.product.screen.ProductHomeScreen
import com.example.projectproduit.ui.product.screen.StockManagementScreen
import com.example.projectproduit.ui.user.UserViewModel
import com.example.projectproduit.ui.user.screen.UserFormScreen
import com.example.projectproduit.ui.user.screen.UserProfileScreen
import com.example.projectproduit.ui.user.screen.UsersHomeScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    orderViewModel: OrderViewModel,
    userViewModel: UserViewModel,
    userId: String?,
    selectedCategory: String?,
    selectedBrand: String?,
    paddingValues: PaddingValues
) {
    val userState by userViewModel.state.collectAsState()
    val isAdmin = userState.isAdmin == true

    NavHost(
        navController = navController,
        startDestination = Routes.Home,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        composable(Routes.Home) {
            ProductHomeScreen(
                viewModel = productViewModel,
                selectedCategory = selectedCategory,
                selectedBrand = selectedBrand,
                cartViewModel = cartViewModel,
                onNavigateToDetails = { productId ->
                    navController.navigate("${Routes.ProductDetails}/$productId")
                },
                modifier = Modifier.padding(paddingValues)
            )
        }
        composable(Routes.StockManagement) {
            if (userId != null && isAdmin) {
                StockManagementScreen(
                    viewModel = productViewModel,
                    selectedCategory = selectedCategory,
                    selectedBrand = selectedBrand
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Routes.SignIn)
                }
            }
        }
        composable("${Routes.ProductDetails}/{productId}", arguments = listOf(navArgument("productId") { type = NavType.StringType })) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetails(
                productId = productId,
                viewModel = productViewModel,
                cartViewModel = cartViewModel
            )
        }
        composable(Routes.Cart) {
            CartScreen(
                viewModel = cartViewModel,
                navToCheckout = {
                    if (userId != null) {
                        navController.navigate(Routes.Checkout)
                    } else {
                        navController.navigate(Routes.SignIn)
                    }
                }
            )
        }
        composable(Routes.Orders) {
            if (userId != null) {
                OrderScreen(viewModel = orderViewModel, userId = userId)
            } else {
                // Redirect unauthenticated users
                LaunchedEffect(Unit) {
                    navController.navigate(Routes.SignIn)
                }
            }
        }
        composable(Routes.Checkout) {
            if (userId != null) {
                CheckoutScreen(
                    productViewModel = productViewModel,
                    cartViewModel = cartViewModel,
                    orderViewModel = orderViewModel,
                    userId = userId,
                    onOrderPlaced = {
                        navController.popBackStack(Routes.Home, inclusive = false)
                    },
                    onCancel = { navController.popBackStack(Routes.Cart, inclusive = false) }
                )
            } else {
                // Fallback just in case someone reaches the route manually
                LaunchedEffect(Unit) {
                    navController.navigate(Routes.SignIn)
                }
            }
        }
        composable(
            route = "${Routes.Profile}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userIdArg = backStackEntry.arguments?.getString("userId")

            if (userIdArg == null) {
                // If userId is null, redirect to SignIn
                LaunchedEffect(Unit) {
                    navController.navigate(Routes.SignIn) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            } else {
                UserProfileScreen(
                    userId = userIdArg,
                    viewModel = userViewModel,
                    navController = navController
                )
            }
        }
        composable("${Routes.ProfileEdit}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("userId")
            UserFormScreen(
                mode = UserFormMode.EDIT,
                userId = id,
                viewModel = userViewModel,
                onBack = { navController.popBackStack() },
                onSuccess = {
                    userId?.let {
                        navController.navigate("${Routes.Profile}/$it") {
                            popUpTo(Routes.Home) { inclusive = false }
                            launchSingleTop = true
                        }
                    }

                }
            )
        }
        composable(Routes.SignIn) {
            UserFormScreen(mode = UserFormMode.SIGNIN, viewModel = userViewModel) {
                val id = userViewModel.state.value.loggedInUser?.userId
                id?.let {
                    navController.navigate("${Routes.Profile}/$it") {
                        popUpTo(Routes.Home) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            }
        }
        composable(Routes.SignUp) {
            UserFormScreen(mode = UserFormMode.SIGNUP, viewModel = userViewModel) {
                val id = userViewModel.state.value.loggedInUser?.userId
                id?.let {
                    navController.navigate("${Routes.Profile}/$it") {
                        popUpTo(Routes.Home) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            }
        }
        composable(Routes.Admin) {
            if (isAdmin) {
                AdminScreen(navController)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Routes.SignIn)
                }
            }
        }

        composable(Routes.UserList) {
            if (isAdmin) {
                UsersHomeScreen(viewModel = userViewModel, navController = navController)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Routes.SignIn)
                }
            }
        }
    }

}