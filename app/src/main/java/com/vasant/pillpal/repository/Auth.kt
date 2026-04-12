package com.PillPal.repository

import com.PillPal.utils.AuthState

interface Auth {
    suspend fun SingIn(email: String, password: String): AuthState
    suspend fun SingUp(email: String, password: String): AuthState
    fun LogOut()
}
