package com.lczarny.lsnplanner.data.common.repository

import com.lczarny.lsnplanner.data.common.model.ProfileModel
import com.lczarny.lsnplanner.data.common.model.toModel
import com.lczarny.lsnplanner.data.local.dao.ProfileDao

class ProfileRepository(private val dao: ProfileDao, private val sessionRepository: SessionRepository) {

    suspend fun loadActiveProfile(email: String): Boolean = dao.getByEmail(email)?.let { profile ->
        profile.toModel().let { profileModel -> sessionRepository.activeProfile = profileModel }
        true
    } ?: run {
        false
    }

    suspend fun insert(profile: ProfileModel): String {
        dao.insert(profile)
        return dao.getByEmail(profile.email)!!.id
    }
}