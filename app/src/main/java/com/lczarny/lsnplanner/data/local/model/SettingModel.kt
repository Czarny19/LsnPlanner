package com.lczarny.lsnplanner.data.local.model

import androidx.room.ColumnInfo

data class SettingModel(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "value") var value: String,
)

enum class AppSetting(val raw: String) {
    TodoListSwipeTutorialDone("todoListSwipeTutorialDone");
}