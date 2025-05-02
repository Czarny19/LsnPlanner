package com.lczarny.lsnplanner.data.common.repository

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email

enum class AuthError {
    InvalidCredentials,
    UserBanned,
    UserAlreadyExists,
    Other
}

class AuthRepository(private val auth: Auth) {
    val currentSessionStatus = auth.sessionStatus

    val currentUser = { auth.currentUserOrNull() }

    suspend fun signIn(email: String, password: String): AuthError? {
        return try {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            null
        } catch (e: Exception) {
            e.getErrorType()
        }
    }

    suspend fun signUp(email: String, password: String): AuthError? {
        return try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            null
        } catch (e: Exception) {
            e.getErrorType()
        }
    }

    suspend fun signOut() {
        try {
            auth.signOut()
            auth.clearSession()
        } catch (_: Exception) {
        }
    }

    suspend fun resetPassword(email: String): AuthError? {
        return try {
            auth.resetPasswordForEmail(email)
            null
        } catch (e: Exception) {
            e.getErrorType()
        }
    }

    private fun Exception.getErrorType(): AuthError {
        if (this is AuthRestException) {
            return when (this.error) {
                "invalid_credentials" -> AuthError.InvalidCredentials
                "user_banned" -> AuthError.UserBanned
                "user_already_exists" -> AuthError.UserAlreadyExists
                else -> AuthError.Other
            }
        }

        return AuthError.Other
    }
}