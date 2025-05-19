package com.lczarny.lsnplanner.domain.auth

import com.lczarny.lsnplanner.online.model.AuthError
import com.lczarny.lsnplanner.online.utils.getErrorType
import io.github.jan.supabase.auth.Auth
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(private val auth: Auth) {

    suspend fun invoke(email: String): AuthError? {
        return try {
            auth.resetPasswordForEmail(email)
            null
        } catch (e: Exception) {
            e.getErrorType()
        }
    }
}