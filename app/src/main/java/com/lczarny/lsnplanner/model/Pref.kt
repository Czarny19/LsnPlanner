package com.lczarny.lsnplanner.model

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

enum class PrefType {
    User,
    Tutorial
}

sealed class Pref(val key: Preferences.Key<String>, val type: PrefType) {
    class HomeClassesView : Pref(stringPreferencesKey("home_classes_view"), PrefType.User)
    class TutorialNoteListSwipe : Pref(stringPreferencesKey("note_list_swipe"), PrefType.Tutorial)
    class TutorialNoteImportance : Pref(stringPreferencesKey("note_importance"), PrefType.Tutorial)
}