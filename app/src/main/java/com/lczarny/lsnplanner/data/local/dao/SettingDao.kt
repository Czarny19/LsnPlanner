package com.lczarny.lsnplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.lczarny.lsnplanner.data.local.entity.Setting
import com.lczarny.lsnplanner.data.local.model.SettingModel
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingDao {

    @Transaction
    @Query("SELECT value FROM setting WHERE name = :settingName")
    fun getSettingValue(settingName: String): Flow<String?>

    @Insert(entity = Setting::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetting(setting: SettingModel)
}