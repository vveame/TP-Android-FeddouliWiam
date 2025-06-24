package com.example.projectproduit.ui.user.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectproduit.ui.user.UserIntent
import com.example.projectproduit.ui.user.UserViewModel
import com.example.projectproduit.ui.user.component.UserList
import androidx.compose.runtime.getValue

@Composable
fun UserHomeScreen(
    viewModel: UserViewModel,
    onNavigateToEditUser: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(UserIntent.FetchAllUsers)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToEditUser("") }) {
                Icon(Icons.Default.Add, contentDescription = "Add User")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                state.users.isNotEmpty() -> {
                    UserList(
                        users = state.users,
                        onUserClick = { user ->
                            onNavigateToEditUser(user.userId)
                        }
                    )
                }

                else -> {
                    Text("User not found", Modifier.padding(16.dp))
                }
            }
        }
    }
}
