package com.lczarny.lsnplanner.domain.cls

import com.lczarny.lsnplanner.database.repository.ClassInfoRepository
import javax.inject.Inject

class DeleteClassUseCase @Inject constructor(private val classInfoRepository: ClassInfoRepository) {

    suspend fun invoke(id: Long) {
        classInfoRepository.delete(id)
    }
}