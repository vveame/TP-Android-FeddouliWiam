package com.example.projectproduit.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectproduit.data.entities.User
import com.example.projectproduit.data.entities.UserRole
import com.example.projectproduit.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UserViewState())
    val state: StateFlow<UserViewState> = _state.asStateFlow()

    fun handleIntent(intent: UserIntent) {
        viewModelScope.launch {
            when (intent) {
                is UserIntent.FetchUser -> fetchUser(intent.userId)
                is UserIntent.FetchAllUsers -> fetchAllUsers()
                is UserIntent.UpdateUser -> updateUser(intent.user)
                is UserIntent.SignIn -> signIn(intent.email, intent.password)
                is UserIntent.SignUp -> signUp(intent.user, intent.password)
                is UserIntent.SignOut -> signOut()
                is UserIntent.CheckIfAdmin -> checkIfAdmin(intent.userId)
            }
        }
    }

    private suspend fun fetchUser(userId: String) {
        _state.update { it.copy(isLoading = true) }
        try {
            val user = userRepository.getUserById(userId)
            _state.update { it.copy(viewedUser = user, isLoading = false) }
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message, isLoading = false) }
        }
    }

    private suspend fun fetchAllUsers() {
        _state.update { it.copy(isLoading = true) }
        try {
            val users = userRepository.getAllUsers()
            _state.update { it.copy(users = users, isLoading = false) }
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message, isLoading = false) }
        }
    }

    private suspend fun updateUser(user: User) {
        try {
            userRepository.updateUser(user)
            fetchAllUsers()
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message) }
        }
    }

    private suspend fun signIn(email: String, password: String) {
        _state.update { it.copy(isLoading = true) }
        try {
            val user = userRepository.signIn(email, password)
            _state.update { it.copy(loggedInUser = user, isAuthenticated = true, isLoading = false) }
        } catch (e: Exception) {
            val errorMessage = if (e.message?.contains("already in use") == true) {
                "This email address is already registered. Please sign in."
            } else {
                e.message ?: "Unknown error occurred"
            }
            _state.update { it.copy(error = errorMessage, isLoading = false) }
        }
    }

    private suspend fun signUp(user: User, password: String) {
        _state.update { it.copy(isLoading = true) }
        try {
            val createdUser = userRepository.signUp(user, password)
            _state.update { it.copy(loggedInUser = createdUser, isAuthenticated = true, isLoading = false) }
        } catch (e: Exception) {
            val msg = when {
                e.message?.contains("badly formatted") == true -> "Invalid email address format."
                e.message?.contains("password is invalid") == true -> "Incorrect password."
                else -> e.message ?: "Unknown error occurred"
            }
            _state.update { it.copy(error = msg, isLoading = false) }
        }
    }

    private fun signOut() {
        _state.update { it.copy(loggedInUser = null, isAuthenticated = false) }
        userRepository.signOutUser()
    }

    private fun checkIfAdmin(userId: String) {
        val user = _state.value.users.find { it.userId == userId }
            ?: _state.value.loggedInUser?.takeIf { it.userId == userId }

        val isAdmin = user?.role == UserRole.ADMIN
        _state.update { it.copy(isAdmin = isAdmin) }
    }
}
