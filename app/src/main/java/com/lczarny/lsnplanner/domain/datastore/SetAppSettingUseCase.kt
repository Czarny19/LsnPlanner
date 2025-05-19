package com.lczarny.lsnplanner.domain.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.lczarny.lsnplanner.di.TutorialDataStore
import com.lczarny.lsnplanner.di.UserDataStore
import com.lczarny.lsnplanner.model.Pref
import com.lczarny.lsnplanner.model.PrefType
import javax.inject.Inject

class SetAppSettingUseCase @Inject constructor(
    @UserDataStore private val userDataStore: DataStore<Preferences>,
    @TutorialDataStore private val tutorialDataStore: DataStore<Preferences>
) {

    suspend fun invoke(pref: Pref, value: String) {
        when (pref.type) {
            PrefType.User -> userDataStore.edit { it[pref.key] = value }
            PrefType.Tutorial -> tutorialDataStore.edit { it[pref.key] = value }
        }
    }
}