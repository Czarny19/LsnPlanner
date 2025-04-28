package com.lczarny.lsnplanner.data.common.repository

import com.lczarny.lsnplanner.data.common.model.ProfileModel
import com.lczarny.lsnplanner.data.common.model.toModel
import com.lczarny.lsnplanner.data.local.dao.ProfileDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileRepository(private val dao: ProfileDao) {

    private var _activeProfile = MutableStateFlow(ProfileModel(id = -1L))

    fun getActiveProfile(): StateFlow<ProfileModel> = _activeProfile.asStateFlow()

    suspend fun getByEmail(email: String): ProfileModel = dao.getByEmail(email).toModel().let {
        _activeProfile.update { it }
        it
    }

    suspend fun insert(profile: ProfileModel) {
        dao.insert(profile)
    }
}