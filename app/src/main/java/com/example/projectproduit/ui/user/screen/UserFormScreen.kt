package com.example.projectproduit.ui.user.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.projectproduit.ui.user.UserViewModel
import com.example.projectproduit.data.entities.UserFormMode
import com.example.projectproduit.ui.user.component.SignInForm
import com.example.projectproduit.ui.user.component.SignUpForm
import com.example.projectproduit.ui.user.component.UserUpdateForm
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

@Composable
fun UserFormScreen(
    mode: UserFormMode,
    userId: String? = null,
    viewModel: UserViewModel,
    onBack: () -> Unit = {},
    onSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var currentMode by rememberSaveable { mutableStateOf(mode) }

    when (currentMode) {
        UserFormMode.SIGNUP -> SignUpForm(
            viewModel = viewModel,
            onSuccess = onSuccess,
            state = state,
            onSwitchToSignIn = { currentMode = UserFormMode.SIGNIN }
        )
        UserFormMode.SIGNIN -> SignInForm(
            viewModel = viewModel,
            onSuccess = onSuccess,
            state = state,
            onSwitchToSignUp = { currentMode = UserFormMode.SIGNUP }
        )
        UserFormMode.EDIT -> UserUpdateForm(
            userId = userId,
            viewModel = viewModel,
            onBack = onBack,
            onSaveSuccess = onSuccess
        )
    }
}

