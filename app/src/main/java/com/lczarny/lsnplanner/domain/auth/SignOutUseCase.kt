package com.lczarny.lsnplanner.domain.auth

import io.github.jan.supabase.auth.Auth
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val auth: Auth) {

    suspend fun invoke() {
        try {
            auth.signOut()
            auth.clearSession()
        } catch (_: Exception) {
        }
    }
}