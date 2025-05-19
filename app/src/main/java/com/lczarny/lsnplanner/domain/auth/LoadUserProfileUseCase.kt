package com.lczarny.lsnplanner.domain.auth

import com.lczarny.lsnplanner.database.repository.ProfileRepository
import com.lczarny.lsnplanner.model.SessionInfo
import com.lczarny.lsnplanner.model.mapper.toProfile
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import javax.inject.Inject

class LoadUserProfileUseCase @Inject constructor(
    private val sessionInfo: SessionInfo,
    private val profileRepository: ProfileRepository
) {

    suspend fun invoke(onSignIn: () -> Unit, onNoSession: () -> Unit) {
        return sessionInfo.status.collect { status ->
            val currentUser = sessionInfo.user()

            if (status is SessionStatus.Initializing) {
                return@collect
            }

            if (status is SessionStatus.Authenticated && currentUser != null) {
                profileRepository.loadActiveProfile(currentUser.email!!)?.let {
                    sessionInfo.activeProfile = it
                    onSignIn.invoke()
                } ?: run {
                    createNewProfile(currentUser, onSignIn)
                }
            } else {
                onNoSession.invoke()
            }
        }
    }

    private suspend fun createNewProfile(user: UserInfo, onSignIn: () -> Unit) {
        return profileRepository.insert(user.toProfile()).let { newProfileId ->
            profileRepository.loadActiveProfile(user.email!!)?.let {
                sessionInfo.activeProfile = it
                onSignIn.invoke()
            }
        }
    }
}