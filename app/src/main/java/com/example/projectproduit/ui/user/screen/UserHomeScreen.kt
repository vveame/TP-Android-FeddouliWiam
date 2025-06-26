package com.example.projectproduit.ui.user.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.projectproduit.ui.user.UserIntent
import com.example.projectproduit.ui.user.UserViewModel
import com.example.projectproduit.ui.user.component.UserList
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.projectproduit.data.entities.UserRole
import com.example.projectproduit.nav.Routes

@Composable
fun UsersHomeScreen(
    viewModel: UserViewModel,
    navController: NavHostController
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.loggedInUser) {
        if (state.loggedInUser?.role == UserRole.ADMIN) {
            viewModel.handleIntent(UserIntent.FetchAllUsers)
        } else {
            navController.navigate(Routes.SignIn) {
                popUpTo(Routes.Home) { inclusive = true }
            }
        }
    }

    when {
        state.isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        !state.error.isNullOrEmpty() -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)
            }
        }

        else -> {
            UserList(
                users = state.users,
                onUserClick = { user ->
                    navController.navigate("${Routes.Profile}/${user.userId}")
                }
            )
        }
    }
}
