package com.example.projectproduit.ui.user

import com.example.projectproduit.data.entities.User

data class UserViewState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false,
    val isAdmin: Boolean = false,
    val loggedInUser: User? = null,      // The current authenticated user
    val viewedUser: User? = null        // The user profile being viewed
)