package com.example.projectproduit.ui.user

import androidx.lifecycle.ViewModel
import com.example.projectproduit.data.entities.User
import com.example.projectproduit.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UserViewState())
    val state: StateFlow<UserViewState> = _state.asStateFlow()

    fun handleIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.FetchUser -> fetchUser(intent.userId)
            is UserIntent.FetchAllUsers -> fetchAllUsers()
            is UserIntent.AddUser -> addUser(intent.user)
            is UserIntent.UpdateUser -> updateUser(intent.user)
            is UserIntent.DeleteUser -> deleteUser(intent.userId)
            is UserIntent.SignIn -> signIn(intent.email, intent.password)
            is UserIntent.SignUp -> signUp(intent.user, intent.password)
            is UserIntent.SignOut -> signOut()
        }
    }

    private fun fetchUser(userId: String) {
        _state.update { it.copy(isLoading = true) }
        userRepository.getUserById(userId,
            onResult = { user ->
                _state.update { it.copy(currentUser = user, isLoading = false) }
            },
            onError = { error ->
                _state.update { it.copy(error = error.message, isLoading = false) }
            }
        )
    }

    private fun fetchAllUsers() {
        _state.update { it.copy(isLoading = true) }
        userRepository.getAllUsers(
            onResult = { users ->
                _state.update { it.copy(users = users, isLoading = false) }
            },
            onError = { error ->
                _state.update { it.copy(error = error.message, isLoading = false) }
            }
        )
    }

    private fun addUser(user: User) {
        userRepository.addUser(user,
            onSuccess = { fetchAllUsers() },
            onFailure = { error -> _state.update { it.copy(error = error.message) } }
        )
    }

    private fun updateUser(user: User) {
        userRepository.updateUser(user,
            onSuccess = { fetchAllUsers() },
            onFailure = { error -> _state.update { it.copy(error = error.message) } }
        )
    }

    private fun deleteUser(userId: String) {
        userRepository.deleteUser(userId,
            onSuccess = { fetchAllUsers() },
            onFailure = { error -> _state.update { it.copy(error = error.message) } }
        )
    }

    private fun signIn(email: String, password: String) {
        _state.update { it.copy(isLoading = true) }
        userRepository.signIn(email, password,
            onSuccess = { user ->
                _state.update { it.copy(currentUser = user, isLoading = false, isAuthenticated = true) }
            },
            onFailure = { error ->
                // If error.message contains "already in use", set a friendly message
                val errorMessage = if (error.message?.contains("already in use") == true) {
                    "This email address is already registered. Please sign in."
                } else {
                    error.message ?: "Unknown error occurred"
                }
                _state.update { it.copy(error = errorMessage, isLoading = false) }
            }
        )
    }

    private fun signUp(user: User, password: String) {
        _state.update { it.copy(isLoading = true) }
        userRepository.signUp(user, password,
            onSuccess = {
                _state.update { it.copy(currentUser = user, isLoading = false, isAuthenticated = true) }
            },
            onFailure = { error ->
                when {
                    error.message?.contains("badly formatted") == true ->
                        "Invalid email address format."

                    error.message?.contains("password is invalid") == true ->
                        "Incorrect password."

                    else -> error.message ?: "Unknown error occurred"
                }
            }
        )
    }

    private fun signOut() {
        userRepository.signOutUser()
        _state.update { it.copy(currentUser = null, isAuthenticated = false) }
    }
}
