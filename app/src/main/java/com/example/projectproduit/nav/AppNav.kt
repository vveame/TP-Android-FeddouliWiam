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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projectproduit.R
import com.example.projectproduit.ui.cart.CartViewModel
import com.example.projectproduit.ui.order.OrderViewModel
import com.example.projectproduit.ui.product.ProductViewModel
import com.example.projectproduit.ui.user.UserIntent
import com.example.projectproduit.ui.user.UserViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(productViewModel: ProductViewModel,
                  cartViewModel: CartViewModel,
                  orderViewModel: OrderViewModel,
                  userViewModel: UserViewModel,
                  userId: String?) {

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val productState by productViewModel.state.collectAsState()
    val categories = productState.products.map { it.category }.distinct().sorted()
    val brands = productState.products.map { it.brand }.distinct().sorted()

    var selectedCategory by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedBrand by rememberSaveable { mutableStateOf<String?>(null) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var brandExpanded by remember { mutableStateOf(false) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val userState by userViewModel.state.collectAsState()
    val isAdmin = userState.isAdmin == true

    LaunchedEffect(userId) {
        userId?.let { userViewModel.handleIntent(UserIntent.CheckIfAdmin(it)) }
    }

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

                    if (userId != null) {
                        NavigationBarItem(
                            selected = currentRoute == Routes.Orders,
                            onClick = {
                                navController.navigate(Routes.Orders) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(Icons.Default.Favorite, contentDescription = "My Orders") },
                            label = { Text("Orders") }
                        )
                    }

                    if (userId != null && isAdmin) {
                        NavigationBarItem(
                            selected = currentRoute == Routes.Admin,
                            onClick = {
                                navController.navigate(Routes.Admin) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(Icons.Default.Settings, contentDescription = "Admin") },
                            label = { Text("Admin") }
                        )
                    }

                    NavigationBarItem(
                        selected = currentRoute?.startsWith(Routes.Profile) == true,
                        onClick = {
                            if (userId != null) {
                                navController.navigate("${Routes.Profile}/$userId") {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            } else {
                                navController.navigate(Routes.SignIn) {
                                    popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                                }
                            }
                        },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text("Profile") }
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            AppNavGraph(
                navController = navController,
                productViewModel = productViewModel,
                cartViewModel = cartViewModel,
                orderViewModel = orderViewModel,
                userViewModel = userViewModel,
                userId = userId,
                selectedCategory = selectedCategory,
                selectedBrand = selectedBrand,
                paddingValues = paddingValues
            )
        }
    }
}
