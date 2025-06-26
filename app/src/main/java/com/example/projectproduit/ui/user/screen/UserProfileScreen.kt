package com.example.projectproduit.ui.user.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import com.example.projectproduit.nav.Routes
import com.example.projectproduit.ui.user.component.ProfileItem

@Composable
fun UserProfileScreen(
    userId: String,
    viewModel: UserViewModel,
    navController: NavHostController
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isAuthenticated) {
        if (!state.isAuthenticated) {
            navController.navigate(Routes.SignIn) {
                popUpTo(Routes.Home) { inclusive = true }
            }
        } else {
            viewModel.handleIntent(UserIntent.FetchUser(userId)) // fetch profile user only
        }
    }

    val loggedInUser = state.loggedInUser
    val viewedUser = state.viewedUser

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (viewedUser != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                "User Profile",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileItem(label = "Full Name", value = viewedUser.fullName, icon = Icons.Default.Person)
                    ProfileItem(label = "Email", value = viewedUser.email, icon = Icons.Default.Email)
                    ProfileItem(label = "Phone", value = viewedUser.phoneNumber ?: "Not specified", icon = Icons.Default.Phone)
                    ProfileItem(label = "Address", value = viewedUser.address?.let {
                        "${it.street}, ${it.city}, ${it.postalCode ?: ""}, ${it.country}"
                    } ?: "Not specified", icon = Icons.Default.Home)
                    ProfileItem(label = "Role", value = viewedUser.role.name, icon = Icons.Default.Lock)
                    ProfileItem(label = "Created at", value = DateFormat.getDateTimeInstance().format(Date(viewedUser.createdAt)), icon = Icons.Default.DateRange)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.navigate("${Routes.ProfileEdit}/$userId") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Modifier")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Modifier")
                }

                Spacer(modifier = Modifier.width(12.dp))

                if (loggedInUser?.userId == userId) {
                    Button(
                        onClick = { viewModel.handleIntent(UserIntent.SignOut) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Logout")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Logout", color = MaterialTheme.colorScheme.onError)
                    }
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error : ${state.error ?: "User not found"}", color = MaterialTheme.colorScheme.error)
        }
    }
}
