package com.lczarny.lsnplanner.domain.cls

import com.lczarny.lsnplanner.database.model.ClassInfo
import com.lczarny.lsnplanner.database.repository.ClassInfoRepository
import com.lczarny.lsnplanner.model.SessionInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadClassListUseCase @Inject constructor(
    private val sessionInfo: SessionInfo,
    private val classInfoRepository: ClassInfoRepository,
) {

    fun invoke(): Flow<List<ClassInfo>> = classInfoRepository.watchAll(sessionInfo.activeLessonPlan.id!!)
}