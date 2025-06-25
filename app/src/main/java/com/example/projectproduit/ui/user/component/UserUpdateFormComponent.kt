package com.example.projectproduit.ui.user.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.KeyboardType
import com.example.projectproduit.data.entities.Address
import com.example.projectproduit.data.entities.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserUpdateForm(
    userId: String?,
    viewModel: UserViewModel,
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    var fullName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }

    var street by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }
    var postalCode by rememberSaveable { mutableStateOf("") }
    var country by rememberSaveable { mutableStateOf("") }

    var role by rememberSaveable { mutableStateOf(UserRole.CUSTOMER) }
    val currentUser = state.currentUser

    LaunchedEffect(userId) {
        if (!userId.isNullOrEmpty()) {
            viewModel.handleIntent(UserIntent.FetchUser(userId))
        }
    }

    LaunchedEffect(state.currentUser) {
        state.currentUser?.let { user ->
            fullName = user.fullName
            email = user.email
            phone = user.phoneNumber ?: ""
            user.address?.let { address ->
                street = address.street
                city = address.city
                postalCode = address.postalCode ?: ""
                country = address.country
            }
            role = user.role
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Personal Info", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Address", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = street,
            onValueChange = { street = it },
            label = { Text("Street") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = postalCode,
            onValueChange = { postalCode = it },
            label = { Text("Postal Code") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = country,
            onValueChange = { country = it },
            label = { Text("Country") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (currentUser?.role == UserRole.ADMIN) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("User Role", style = MaterialTheme.typography.titleMedium)

            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = role.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Role") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    UserRole.entries.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.name) },
                            onClick = {
                                role = option
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                onClick = onBack,
            ) {
                Text("Back")
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = {
                    val user = User(
                        userId = userId ?: UUID.randomUUID().toString(),
                        fullName = fullName,
                        email = if (currentUser?.role == UserRole.ADMIN) email else currentUser?.email ?: "",
                        phoneNumber = phone,
                        address = Address(
                            street = street,
                            city = city,
                            postalCode = postalCode,
                            country = country
                        ),
                        role = if (currentUser?.role == UserRole.ADMIN) role else currentUser?.role ?: UserRole.CUSTOMER
                    )

                    if (userId.isNullOrEmpty()) {
                        viewModel.handleIntent(UserIntent.AddUser(user))
                    } else {
                        viewModel.handleIntent(UserIntent.UpdateUser(user))
                    }

                    onSaveSuccess()
                }
            ) {
                Text("Save")
            }
        }
    }
}
