package com.example.projectproduit.ui.user

import com.example.projectproduit.data.entities.User

data class UserViewState(
    val users: List<User> = emptyList(),
    val currentUser: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false
)