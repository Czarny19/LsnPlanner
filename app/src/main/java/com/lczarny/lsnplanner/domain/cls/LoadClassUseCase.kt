package com.lczarny.lsnplanner.domain.cls

import com.lczarny.lsnplanner.database.model.ClassInfo
import com.lczarny.lsnplanner.database.model.FullClass
import com.lczarny.lsnplanner.database.model.defaultClassType
import com.lczarny.lsnplanner.database.repository.ClassInfoRepository
import com.lczarny.lsnplanner.model.SessionInfo
import javax.inject.Inject

class LoadClassUseCase @Inject constructor(
    private val sessionInfo: SessionInfo,
    private val classInfoRepository: ClassInfoRepository,
) {

    suspend fun invoke(classInfoId: Long?): FullClass =
        classInfoId?.let { id ->
            classInfoRepository.getFullDataById(id)
        } ?: run {
            sessionInfo.activeLessonPlan.let {
                FullClass(
                    info = ClassInfo(lessonPlanId = it.id!!, type = it.type.defaultClassType()),
                    schedules = listOf(),
                    exams = listOf(),
                    homeworks = listOf()
                )
            }
        }
}