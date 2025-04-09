package com.lczarny.lsnplanner.data.common.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.lczarny.lsnplanner.data.common.model.AppSettings
import com.lczarny.lsnplanner.data.common.model.Tutorials
import com.lczarny.lsnplanner.di.TutorialDataStore
import com.lczarny.lsnplanner.di.UserDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class DataStoreRepository(
    @UserDataStore private val userDataStore: DataStore<Preferences>,
    @TutorialDataStore private val tutorialDataStore: DataStore<Preferences>
) {
    private var _appSettings = MutableStateFlow<AppSettings>(AppSettings())

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAppSettings(): Flow<AppSettings> {
        if (_appSettings.value.initialized) {
            return _appSettings.asStateFlow()
        }

        return userDataStore.data.combine(tutorialDataStore.data) { userData, tutorials ->
            val appSettings = AppSettings(
                userName = userData[USER_NAME_KEY],
                homeClassesViewType = userData[HOME_CLASSES_VIEW],
                tutorials = Tutorials(
                    noteListSwipeDone = tutorials[TUTORIAL_NOTE_LIST_SWIPE] == "true",
                    noteImportanceDone = tutorials[TUTORIAL_NOTE_IMPORTANCE] == "true"
                )
            )

            _appSettings.update { appSettings }

            appSettings
        }
    }

    fun getUserName(): Flow<String?> = _appSettings.map { it.userName }

    suspend fun setUserName(userName: String) {
        userDataStore.edit { it[USER_NAME_KEY] = userName }
        _appSettings.update { _appSettings.value.copy(userName = userName) }
    }

    fun getHomeClassesViewType(): Flow<String?> = _appSettings.map { it.homeClassesViewType }

    suspend fun setHomeClassesViewType(viewType: String) {
        userDataStore.edit { it[HOME_CLASSES_VIEW] = viewType }
        _appSettings.update { _appSettings.value.copy(homeClassesViewType = viewType) }
    }

    fun getTutorialNoteListSwipeDone(): Flow<Boolean> = _appSettings.map { it.tutorials.noteListSwipeDone }

    suspend fun setTutorialNoteListSwipeDone() {
        userDataStore.edit { it[TUTORIAL_NOTE_LIST_SWIPE] = "true" }
        _appSettings.update { _appSettings.value.copy(tutorials = _appSettings.value.tutorials.copy(noteListSwipeDone = true)) }
    }

    fun getTutorialNoteImportanceDone(): Flow<Boolean> = _appSettings.map { it.tutorials.noteImportanceDone }

    suspend fun setTutorialNoteImportanceDone() {
        userDataStore.edit { it[TUTORIAL_NOTE_IMPORTANCE] = "true" }
        _appSettings.update { _appSettings.value.copy(tutorials = _appSettings.value.tutorials.copy(noteImportanceDone = true)) }
    }

    suspend fun resetTutorials() {
        userDataStore.edit {
            it[TUTORIAL_NOTE_LIST_SWIPE] = "false"
            it[TUTORIAL_NOTE_IMPORTANCE] = "false"
        }

        _appSettings.update {
            _appSettings.value.copy(
                tutorials = _appSettings.value.tutorials.copy(
                    noteImportanceDone = false,
                    noteListSwipeDone = false
                )
            )
        }
    }

    companion object {
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val HOME_CLASSES_VIEW = stringPreferencesKey("home_classes_view")

        private val TUTORIAL_NOTE_LIST_SWIPE = stringPreferencesKey("note_list_swipe")
        private val TUTORIAL_NOTE_IMPORTANCE = stringPreferencesKey("note_importance")
    }
}