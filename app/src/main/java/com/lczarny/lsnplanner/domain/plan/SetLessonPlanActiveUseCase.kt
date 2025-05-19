package com.lczarny.lsnplanner.domain.plan

import com.lczarny.lsnplanner.database.model.LessonPlan
import com.lczarny.lsnplanner.database.repository.LessonPlanRepository
import javax.inject.Inject

class SetLessonPlanActiveUseCase @Inject constructor(private val lessonPlanRepository: LessonPlanRepository) {

    suspend fun invoke(lessonPlan: LessonPlan) {
        lessonPlanRepository.update(lessonPlan.apply { isActive = true }).also {
            lessonPlanRepository.makeOtherPlansNotActive(lessonPlan)
        }
    }
}