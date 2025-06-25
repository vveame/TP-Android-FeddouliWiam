package com.example.projectproduit.ui.user.component

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectproduit.data.entities.User
import com.example.projectproduit.ui.user.UserIntent
import com.example.projectproduit.ui.user.UserViewModel
import java.util.UUID
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.projectproduit.data.entities.UserRole
import com.example.projectproduit.ui.user.UserViewState

@Composable
fun SignUpForm(
    viewModel: UserViewModel,
    onSuccess: () -> Unit,
    state: UserViewState,
    onSwitchToSignIn: () -> Unit
) {
    val context = LocalContext.current
    var fullName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    // Show local form validation error
    LaunchedEffect(localError) {
        localError?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    // Show Firebase signup error
    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT).show()
        }
    }

    // On success
    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) {
            Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
            onSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create an account", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                localError = null
            },
            label = { Text("Email") },
            isError = localError != null,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                val trimmedEmail = email.trim()
                val emailPattern = Patterns.EMAIL_ADDRESS

                if (fullName.isBlank() || trimmedEmail.isBlank() || password.isBlank()) {
                    localError = "Please fill in all fields."
                    return@Button
                }

                if (!emailPattern.matcher(trimmedEmail).matches()) {
                    localError = "Invalid email address."
                    return@Button
                }

                val user = User(
                    userId = UUID.randomUUID().toString(),
                    fullName = fullName,
                    email = trimmedEmail,
                    role = UserRole.CUSTOMER
                )

                viewModel.handleIntent(UserIntent.SignUp(user, password))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }

        Spacer(modifier = Modifier.height(32.dp))
        TextButton(onClick = onSwitchToSignIn) {
            Text("Already have an account? Sign In")
        }
    }
}
