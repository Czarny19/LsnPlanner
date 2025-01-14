package com.lczarny.lsnplanner.data.local.repository

import com.lczarny.lsnplanner.data.local.dao.SettingDao
import com.lczarny.lsnplanner.data.local.model.AppSetting
import com.lczarny.lsnplanner.data.local.model.SettingModel
import kotlinx.coroutines.flow.Flow

class SettingRepository(private val settingDao: SettingDao) {

    fun settingValue(setting: AppSetting): Flow<String?> = settingDao.getSettingValue(setting.raw)

    suspend fun insert(setting: SettingModel) {
        settingDao.insertSetting(setting)
    }

    suspend fun resetTutorialSettings() {
        settingDao.insertSetting(SettingModel(name = AppSetting.TodoListSwipeTutorialDone.raw, value = "false"))
    }
}