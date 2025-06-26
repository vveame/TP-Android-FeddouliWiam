package com.example.projectproduit.ui.user

import com.example.projectproduit.data.entities.User

sealed class UserIntent {
    data class FetchUser(val userId: String) : UserIntent()
    object FetchAllUsers : UserIntent()
    data class UpdateUser(val user: User) : UserIntent()
    data class SignIn(val email: String, val password: String) : UserIntent()
    data class SignUp(val user: User, val password: String) : UserIntent()
    object SignOut : UserIntent()
    data class CheckIfAdmin(val userId: String) : UserIntent()
}
