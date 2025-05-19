package com.lczarny.lsnplanner.domain.plan

import com.lczarny.lsnplanner.database.model.LessonPlan
import com.lczarny.lsnplanner.database.repository.LessonPlanRepository
import com.lczarny.lsnplanner.model.SessionInfo
import com.lczarny.lsnplanner.utils.currentTimestamp
import javax.inject.Inject

class LoadLessonPlanUseCase @Inject constructor(private val sessionInfo: SessionInfo, private val lessonPlanRepository: LessonPlanRepository) {

    suspend fun invoke(planId: Long?): LessonPlan =
        planId?.let { id ->
            lessonPlanRepository.getById(id)
        } ?: run {
            LessonPlan(isActive = true, profileId = sessionInfo.activeProfile.id, createDate = currentTimestamp())
        }
}