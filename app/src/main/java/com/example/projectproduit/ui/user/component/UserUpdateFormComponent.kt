package com.example.projectproduit.ui.user.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectproduit.data.entities.User
import com.example.projectproduit.ui.user.UserIntent
import com.example.projectproduit.ui.user.UserViewModel
import java.util.UUID
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

@Composable
fun UserUpdateForm(
    userId: String?,
    viewModel: UserViewModel,
    onSaveSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    var fullName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(userId) {
        if (!userId.isNullOrEmpty()) {
            viewModel.handleIntent(UserIntent.FetchUser(userId))
        }
    }

    LaunchedEffect(state.currentUser) {
        state.currentUser?.let {
            fullName = it.fullName
            email = it.email
            phone = it.phoneNumber ?: ""
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val user = User(
                    userId = userId ?: UUID.randomUUID().toString(),
                    fullName = fullName,
                    email = email,
                    phoneNumber = phone
                )

                if (userId.isNullOrEmpty()) {
                    viewModel.handleIntent(UserIntent.AddUser(user))
                } else {
                    viewModel.handleIntent(UserIntent.UpdateUser(user))
                }

                onSaveSuccess()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save")
        }
    }
}
