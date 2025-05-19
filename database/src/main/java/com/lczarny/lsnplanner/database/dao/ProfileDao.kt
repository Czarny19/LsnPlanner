package com.lczarny.lsnplanner.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lczarny.lsnplanner.database.entity.ProfileEntity
import com.lczarny.lsnplanner.database.model.Profile

@Dao
interface ProfileDao {

    @Query("SELECT * FROM profile WHERE email = :email")
    suspend fun getByEmail(email: String): Profile?

    @Insert(entity = ProfileEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Profile)
}