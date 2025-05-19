package com.lczarny.lsnplanner.di.domain

import com.lczarny.lsnplanner.database.repository.ProfileRepository
import com.lczarny.lsnplanner.domain.auth.LoadUserProfileUseCase
import com.lczarny.lsnplanner.domain.auth.ResetPasswordUseCase
import com.lczarny.lsnplanner.domain.auth.SignInUseCase
import com.lczarny.lsnplanner.domain.auth.SignOutUseCase
import com.lczarny.lsnplanner.domain.auth.SignUpUseCase
import com.lczarny.lsnplanner.model.SessionInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.auth.Auth

@Module
@InstallIn(SingletonComponent::class)
object AuthDomainModule {

    @Provides
    fun provideLoadUserProfileUseCase(sessionInfo: SessionInfo, profileRepository: ProfileRepository): LoadUserProfileUseCase =
        LoadUserProfileUseCase(sessionInfo, profileRepository)

    @Provides
    fun provideSignInUseCase(auth: Auth): SignInUseCase = SignInUseCase(auth)

    @Provides
    fun provideSignUpUseCase(auth: Auth): SignUpUseCase = SignUpUseCase(auth)

    @Provides
    fun provideSignOutUseCase(auth: Auth): SignOutUseCase = SignOutUseCase(auth)

    @Provides
    fun provideResetPasswordUseCase(auth: Auth): ResetPasswordUseCase = ResetPasswordUseCase(auth)
}