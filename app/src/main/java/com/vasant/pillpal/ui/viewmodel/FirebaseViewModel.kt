package com.PillPal.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.PillPal.repository.Auth
import com.PillPal.utils.AuthState
import com.PillPal.utils.FirebaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseViewModel @Inject constructor(
    private val auth: Auth
) : ViewModel() {
    var firebaseState by mutableStateOf<FirebaseState?>(null)
    //    var authState by mutableStateOf<AuthResult>(AuthResult.Loading)
    var authState by mutableStateOf(AuthState(Error = null, Success = false))

    fun login(email: String, password: String) {
        viewModelScope.launch {
            firebaseState = FirebaseState.Loading
            authState = auth.SingIn(email, password)
            firebaseState = if (authState.Success) {
                FirebaseState.Done
            } else FirebaseState.IsIdle
        }
    }

    fun singUp(email: String, password: String) {
        viewModelScope.launch {
            firebaseState = FirebaseState.Loading
            authState = auth.SingUp(email, password)
            firebaseState = if (authState.Success) {
                FirebaseState.Done
            } else FirebaseState.IsIdle
        }
    }
}
