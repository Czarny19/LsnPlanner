package com.lczarny.lsnplanner.data.common.repository

import com.lczarny.lsnplanner.data.local.dao.SettingDao
import com.lczarny.lsnplanner.data.common.model.AppSetting
import com.lczarny.lsnplanner.data.common.model.SettingModel

class SettingRepository(private val dao: SettingDao) {

    fun getSettingValue(setting: AppSetting): String? = dao.getByName(setting.raw)

    suspend fun setUserName(userName: String) {
        dao.insert(SettingModel(name = AppSetting.UserName.raw, value = userName))
    }

    suspend fun setNoteListSwipeTutorialDone() {
        dao.insert(SettingModel(name = AppSetting.NoteListSwipeTutorialDone.raw, value = "true"))
    }

    suspend fun setNoteImportanceTutorialDone() {
        dao.insert(SettingModel(name = AppSetting.NoteImportanceTutorialDone.raw, value = "true"))
    }

    suspend fun resetTutorialSettings() {
        dao.insert(SettingModel(name = AppSetting.NoteListSwipeTutorialDone.raw, value = "false"))
    }

    suspend fun setHomeClassesViewType(viewType: String) {
        dao.insert(SettingModel(name = AppSetting.HomeClassesViewType.raw, value = viewType))
    }
}