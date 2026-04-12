package com.PillPal.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.PillPal.utils.AuthState
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "Firebase"

class AuthImplementation @Inject constructor(
    private val _auth: FirebaseAuth
) : Auth {

    override suspend fun SingIn(email: String, password: String): AuthState {

        return try {
            val result = _auth.signInWithEmailAndPassword(email, password).await()
            Log.d(TAG, "User successfully Login in.")

            AuthState(
                Error = null,
                Success = true
            )
        } catch (e: Exception) {
            AuthState(
                Error = e.message.toString(),
                Success = false
            )
        }
    }

    override suspend fun SingUp(email: String, password: String): AuthState {
        return try {
            val result = _auth.createUserWithEmailAndPassword(email, password).await()
            AuthState(Error = null, Success = true)
        } catch (e: Exception) {
            AuthState(Error = e.message.toString(), Success = false)
        }
    }

    override fun LogOut() {
        _auth.signOut()
        Log.d(TAG, "User signed out.")
    }
}
