package com.lczarny.lsnplanner.domain.plan

import com.lczarny.lsnplanner.database.model.LessonPlan
import com.lczarny.lsnplanner.database.repository.LessonPlanRepository
import com.lczarny.lsnplanner.model.SessionInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadLessonPlanListUseCase @Inject constructor(private val sessionInfo: SessionInfo, private val lessonPlanRepository: LessonPlanRepository) {

    fun invoke(): Flow<List<LessonPlan>> = lessonPlanRepository.watchAll(sessionInfo.activeProfile.id)
}