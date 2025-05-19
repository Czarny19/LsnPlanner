package com.lczarny.lsnplanner.domain.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.lczarny.lsnplanner.di.TutorialDataStore
import com.lczarny.lsnplanner.di.UserDataStore
import com.lczarny.lsnplanner.model.AppSettings
import com.lczarny.lsnplanner.model.Pref
import com.lczarny.lsnplanner.model.Tutorials
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetAppSettingsUseCase @Inject constructor(
    @UserDataStore private val userDataStore: DataStore<Preferences>,
    @TutorialDataStore private val tutorialDataStore: DataStore<Preferences>
) {

    fun invoke(): Flow<AppSettings> = userDataStore.data.combine(tutorialDataStore.data) { userData, tutorials ->
        val appSettings = AppSettings(
            homeClassesViewType = userData[Pref.HomeClassesView().key],
            tutorials = Tutorials(
                noteListSwipeDone = tutorials[Pref.TutorialNoteListSwipe().key].toString() == "true",
                noteImportanceDone = tutorials[Pref.TutorialNoteImportance().key].toString() == "true"
            )
        )

        appSettings
    }
}