package com.lczarny.lsnplanner.data.common.repository

import com.lczarny.lsnplanner.data.common.model.ProfileModel
import com.lczarny.lsnplanner.data.common.model.toModel
import com.lczarny.lsnplanner.data.local.dao.ProfileDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileRepository(private val dao: ProfileDao) {

    private var _activeProfile = MutableStateFlow(ProfileModel(id = ""))

    fun getActiveProfile(): StateFlow<ProfileModel> = _activeProfile.asStateFlow()

    suspend fun loadActiveProfile(email: String): ProfileModel? = dao.getByEmail(email)?.let { profile ->
        profile.toModel().let { profileModel ->
            _activeProfile.update { profileModel }
            profileModel
        }
    } ?: run {
        null
    }

    suspend fun insert(profile: ProfileModel): String {
        dao.insert(profile)
        return dao.getByEmail(profile.email)!!.id
    }
}