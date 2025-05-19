package com.lczarny.lsnplanner.domain.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.lczarny.lsnplanner.di.TutorialDataStore
import com.lczarny.lsnplanner.model.Pref
import javax.inject.Inject

class ResetTutorialsUseCase @Inject constructor(@TutorialDataStore private val tutorialDataStore: DataStore<Preferences>) {

    suspend fun invoke() {
        tutorialDataStore.edit {
            it[Pref.TutorialNoteListSwipe().key] = "false"
            it[Pref.TutorialNoteImportance().key] = "false"
        }
    }
}