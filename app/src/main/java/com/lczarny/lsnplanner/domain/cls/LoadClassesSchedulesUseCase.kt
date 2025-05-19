package com.lczarny.lsnplanner.domain.cls

import com.lczarny.lsnplanner.database.model.ClassScheduleWithInfo
import com.lczarny.lsnplanner.database.repository.ClassInfoRepository
import com.lczarny.lsnplanner.database.repository.ClassScheduleRepository
import com.lczarny.lsnplanner.model.SessionInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

class LoadClassesSchedulesUseCase @Inject constructor(
    private val sessionInfo: SessionInfo,
    private val classInfoRepository: ClassInfoRepository,
    private val classScheduleRepository: ClassScheduleRepository
) {

    fun invoke(): Flow<List<ClassScheduleWithInfo>> = classInfoRepository.watchAll(sessionInfo.activeLessonPlan.id!!)
        .zip(classScheduleRepository.watchAll(sessionInfo.activeLessonPlan.id!!)) { infoList, scheduleList ->
            scheduleList.map { schedule ->
                ClassScheduleWithInfo(schedule, infoList.first { info -> info.id == schedule.classInfoId })
            }
        }
        .takeWhile { it.any { scheduleWithInfo -> scheduleWithInfo.info.lessonPlanId == sessionInfo.activeLessonPlan.id!! } }
}