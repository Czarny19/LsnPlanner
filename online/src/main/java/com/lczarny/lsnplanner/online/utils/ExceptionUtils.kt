package com.lczarny.lsnplanner.online.utils

import com.lczarny.lsnplanner.online.model.AuthError
import io.github.jan.supabase.auth.exception.AuthRestException

fun Exception.getErrorType(): AuthError {
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