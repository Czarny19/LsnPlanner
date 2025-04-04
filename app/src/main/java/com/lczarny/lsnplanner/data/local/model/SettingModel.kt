package com.lczarny.lsnplanner.data.local.model

import androidx.room.ColumnInfo

data class SettingModel(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "value") var value: String,
)

enum class AppSetting(val raw: String) {
    UserName("userName"),
    NoteImportanceTutorialDone("noteImportanceTutorialDone"),
    NoteListSwipeTutorialDone("noteListSwipeTutorialDone"),
    HomeClassesViewType("homeClassesViewType");
}