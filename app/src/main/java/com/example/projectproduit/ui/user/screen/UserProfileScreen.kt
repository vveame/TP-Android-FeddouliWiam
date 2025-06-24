package com.example.projectproduit.ui.user.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectproduit.ui.user.UserIntent
import com.example.projectproduit.ui.user.UserViewModel
import java.text.DateFormat
import java.util.Date
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.projectproduit.nav.Routes

@Composable
fun UserProfileScreen(
    userId: String,
    viewModel: UserViewModel,
    navController: NavHostController
) {
    val state by viewModel.state.collectAsState()

    // Redirect to SignIn if not authenticated
    LaunchedEffect(state.isAuthenticated) {
        if (!state.isAuthenticated) {
            navController.navigate(Routes.SignIn) {
                popUpTo(Routes.Home) { inclusive = true }
            }
        } else {
            viewModel.handleIntent(UserIntent.FetchUser(userId))
        }
    }

    val user = state.currentUser

    if (state.isLoading) {
        CircularProgressIndicator()
    } else if (user != null) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("User Profile", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

            Text("Full Name: ${user.fullName}", style = MaterialTheme.typography.bodyLarge)
            Text("Email: ${user.email}")
            Text("Phone: ${user.phoneNumber ?: "Non-specified"}")
            Text("Address: ${user.address?.street ?: "Non-specified"}")
            Text("Verified: ${if (user.isVerified) "Oui" else "Non"}")
            Text("Created at: ${DateFormat.getDateTimeInstance().format(Date(user.createdAt))}")

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.handleIntent(UserIntent.SignOut)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Logout", color = MaterialTheme.colorScheme.onError)
            }
        }
    } else {
        Text("User not found or error: ${state.error ?: "unknown"}")
    }
}
