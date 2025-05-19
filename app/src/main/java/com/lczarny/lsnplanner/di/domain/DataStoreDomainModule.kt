package com.lczarny.lsnplanner.di.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.lczarny.lsnplanner.di.TutorialDataStore
import com.lczarny.lsnplanner.di.UserDataStore
import com.lczarny.lsnplanner.domain.datastore.GetAppSettingsUseCase
import com.lczarny.lsnplanner.domain.datastore.ResetTutorialsUseCase
import com.lczarny.lsnplanner.domain.datastore.SetAppSettingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataStoreDomainModule {

    @Provides
    fun provideGetAppSettingsUseCase(
        @UserDataStore userDataStore: DataStore<Preferences>,
        @TutorialDataStore tutorialDataStore: DataStore<Preferences>
    ): GetAppSettingsUseCase = GetAppSettingsUseCase(userDataStore, tutorialDataStore)

    @Provides
    fun provideResetTutorialsUseCase(
        @TutorialDataStore tutorialDataStore: DataStore<Preferences>
    ): ResetTutorialsUseCase = ResetTutorialsUseCase(tutorialDataStore)

    @Provides
    fun provideSetAppSettingUseCase(
        @UserDataStore userDataStore: DataStore<Preferences>,
        @TutorialDataStore tutorialDataStore: DataStore<Preferences>
    ): SetAppSettingUseCase = SetAppSettingUseCase(userDataStore, tutorialDataStore)
}