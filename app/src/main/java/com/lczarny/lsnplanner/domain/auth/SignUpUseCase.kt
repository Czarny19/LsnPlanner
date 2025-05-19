package com.lczarny.lsnplanner.domain.auth

import com.lczarny.lsnplanner.online.model.AuthError
import com.lczarny.lsnplanner.online.utils.getErrorType
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val auth: Auth) {

    suspend fun invoke(email: String, password: String): AuthError? {
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
}