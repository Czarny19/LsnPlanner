package com.lczarny.lsnplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lczarny.lsnplanner.data.common.model.ProfileModel
import com.lczarny.lsnplanner.data.local.entity.Profile

@Dao
interface ProfileDao {

    @Query("SELECT * FROM profile WHERE email = :email")
    suspend fun getByEmail(email: String): Profile?

    @Insert(entity = Profile::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: ProfileModel)
}