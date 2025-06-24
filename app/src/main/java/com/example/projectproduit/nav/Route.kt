package com.example.projectproduit.nav

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projectproduit.R
import com.example.projectproduit.data.entities.UserFormMode
import com.example.projectproduit.ui.cart.CartViewModel
import com.example.projectproduit.ui.cart.screen.CartScreen
import com.example.projectproduit.ui.order.OrderViewModel
import com.example.projectproduit.ui.order.screen.CheckoutScreen
import com.example.projectproduit.ui.product.ProductViewModel
import com.example.projectproduit.ui.product.component.ProductDetails
import com.example.projectproduit.ui.product.screen.ProductHomeScreen
import com.example.projectproduit.ui.user.UserViewModel
import com.example.projectproduit.ui.user.screen.UserFormScreen
import com.example.projectproduit.ui.user.screen.UserProfileScreen
import kotlinx.coroutines.launch

object Routes {
    const val Home = "home"
    const val ProductDetails = "product_details"
    const val Cart = "cart"
    const val Checkout = "checkout"
    const val Profile = "profile"
    const val SignIn = "signIn"
    const val SignUp = "signUp"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(viewModel: ProductViewModel,
                  cartViewModel: CartViewModel,
                  orderViewModel: OrderViewModel,
                  userViewModel: UserViewModel,
                  userId: String?) {

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val state by viewModel.state.collectAsState()
    val categories = state.products.map { it.category }.distinct().sorted()
    val brands = state.products.map { it.brand }.distinct().sorted()

    var selectedCategory by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedBrand by rememberSaveable { mutableStateOf<String?>(null) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var brandExpanded by remember { mutableStateOf(false) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Glamora Market", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))
                NavigationDrawerItem(label = { Text("Home") }, selected = currentRoute == Routes.Home, onClick = {
                    navController.navigate(Routes.Home) {
                        popUpTo(0); launchSingleTop = true
                    }
                    scope.launch { drawerState.close() }
                })

                // Category Filter
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded },
                    modifier = Modifier.padding(16.dp).fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedCategory ?: "All",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        DropdownMenuItem(text = { Text("All") }, onClick = {
                            selectedCategory = null; categoryExpanded = false
                        })
                        categories.forEach { category ->
                            DropdownMenuItem(text = { Text(category) }, onClick = {
                                selectedCategory = category; categoryExpanded = false
                                scope.launch { drawerState.close() }
                            })
                        }
                    }
                }

                // Brand Filter
                ExposedDropdownMenuBox(
                    expanded = brandExpanded,
                    onExpandedChange = { brandExpanded = !brandExpanded },
                    modifier = Modifier.padding(16.dp).fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedBrand ?: "All",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Brand") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = brandExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = brandExpanded,
                        onDismissRequest = { brandExpanded = false }
                    ) {
                        DropdownMenuItem(text = { Text("All") }, onClick = {
                            selectedBrand = null; brandExpanded = false
                        })
                        brands.forEach { brand ->
                            DropdownMenuItem(text = { Text(brand) }, onClick = {
                                selectedBrand = brand; brandExpanded = false
                                scope.launch { drawerState.close() }
                            })
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Glamora Market", color = MaterialTheme.colorScheme.onPrimary) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    NavigationBarItem(
                        selected = currentRoute == Routes.Cart,
                        onClick = {
                            navController.navigate(Routes.Cart) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
                        label = { Text("Cart") }
                    )

                    NavigationBarItem(
                        selected = currentRoute == Routes.Home,
                        onClick = {
                            navController.navigate(Routes.Home) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(painterResource(id = R.drawable.logo), contentDescription = "Home", modifier = Modifier.size(32.dp)) },
                        label = { Text("Home") }
                    )

                    NavigationBarItem(
                        selected = currentRoute == Routes.Profile,
                        onClick = {
                            navController.navigate(Routes.Profile) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text("Profile") }
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Routes.Home,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                composable(Routes.Home) {
                    ProductHomeScreen(
                        viewModel = viewModel,
                        selectedCategory = selectedCategory,
                        selectedBrand = selectedBrand,
                        cartViewModel = cartViewModel,
                        onNavigateToDetails = { productId ->
                            navController.navigate("${Routes.ProductDetails}/$productId")
                        },
                        modifier = Modifier.padding(paddingValues)
                    )
                }
                composable("${Routes.ProductDetails}/{productId}", arguments = listOf(navArgument("productId") { type = NavType.StringType })) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    ProductDetails(
                        productId = productId,
                        viewModel = viewModel,
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
                composable(Routes.Checkout) {
                    if (userId != null) {
                        CheckoutScreen(
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
                composable(Routes.Profile) {
                    if (userId != null) {
                        UserProfileScreen(userId = userId, viewModel = userViewModel, navController = navController)
                    } else {
                        LaunchedEffect(Unit) {
                            navController.navigate(Routes.SignIn)
                        }
                    }
                }
                composable(Routes.SignIn) {
                    UserFormScreen(mode = UserFormMode.SIGNIN, viewModel = userViewModel) {
                        navController.navigate(Routes.Profile) {
                            popUpTo(Routes.Home) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }

                composable(Routes.SignUp) {
                    UserFormScreen(mode = UserFormMode.SIGNUP, viewModel = userViewModel) {
                        navController.navigate(Routes.Profile) {
                            popUpTo(Routes.Home) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
    }
}
