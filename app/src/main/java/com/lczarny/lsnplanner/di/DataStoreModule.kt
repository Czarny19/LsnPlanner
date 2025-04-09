package com.lczarny.lsnplanner.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.lczarny.lsnplanner.data.common.repository.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Qualifier
import javax.inject.Singleton

private const val USER_PREFERENCES = "user"
private const val TUTORIAL_PREFERENCES = "tutorial"

@Retention()
@Qualifier
annotation class UserDataStore

@Retention()
@Qualifier
annotation class TutorialDataStore

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    @UserDataStore
    fun provideUserDataStore(@ApplicationContext context: Context, @IoDispatcher ioDispatcher: CoroutineDispatcher): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(produceNewData = { emptyPreferences() }),
            migrations = listOf(SharedPreferencesMigration(context, USER_PREFERENCES)),
            scope = CoroutineScope(ioDispatcher),
            produceFile = { context.preferencesDataStoreFile(USER_PREFERENCES) }
        )

    @Provides
    @Singleton
    @TutorialDataStore
    fun provideTutorialDataStore(@ApplicationContext context: Context, @IoDispatcher ioDispatcher: CoroutineDispatcher): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(produceNewData = { emptyPreferences() }),
            migrations = listOf(SharedPreferencesMigration(context, TUTORIAL_PREFERENCES)),
            scope = CoroutineScope(ioDispatcher),
            produceFile = { context.preferencesDataStoreFile(TUTORIAL_PREFERENCES) }
        )

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        @UserDataStore userDataStore: DataStore<Preferences>,
        @TutorialDataStore tutorialDataStore: DataStore<Preferences>
    ): DataStoreRepository = DataStoreRepository(userDataStore, tutorialDataStore)
}