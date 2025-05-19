package com.lczarny.lsnplanner.di

import com.lczarny.lsnplanner.model.SessionInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SessionModule {

    @Provides
    @Singleton
    fun provideSessionInfo(client: SupabaseClient): SessionInfo = SessionInfo(client.auth)
}