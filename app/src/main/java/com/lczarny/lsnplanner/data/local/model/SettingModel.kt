package com.lczarny.lsnplanner.data.local.model

data class SettingModel(
    var name: String,
    var value: String,
)

enum class AppSetting(val raw: String) {
    TodoListSwipeTutorialDone("todoListSwipeTutorialDone");
}