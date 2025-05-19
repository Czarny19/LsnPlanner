package com.lczarny.lsnplanner.database.repository

import com.lczarny.lsnplanner.database.dao.ProfileDao
import com.lczarny.lsnplanner.database.model.Profile

class ProfileRepository(private val dao: ProfileDao) {

    suspend fun loadActiveProfile(email: String): Profile? = dao.getByEmail(email)

    suspend fun insert(profile: Profile): String {
        dao.insert(profile)
        return dao.getByEmail(profile.email)!!.id
    }
}