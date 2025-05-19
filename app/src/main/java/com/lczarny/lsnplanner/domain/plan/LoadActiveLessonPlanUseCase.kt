package com.lczarny.lsnplanner.domain.plan

import com.lczarny.lsnplanner.database.model.LessonPlan
import com.lczarny.lsnplanner.database.repository.LessonPlanRepository
import com.lczarny.lsnplanner.model.SessionInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class LoadActiveLessonPlanUseCase @Inject constructor(private val sessionInfo: SessionInfo, private val lessonPlanRepository: LessonPlanRepository) {

    fun invoke(): Flow<LessonPlan?> = lessonPlanRepository.watchActive(sessionInfo.activeProfile.id).onEach {
        if (it != null) {
            sessionInfo.activeLessonPlan = it
        }
    }
}