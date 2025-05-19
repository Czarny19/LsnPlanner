package com.lczarny.lsnplanner.domain.plan

import com.lczarny.lsnplanner.database.model.LessonPlan
import com.lczarny.lsnplanner.database.repository.LessonPlanRepository
import javax.inject.Inject

class SaveLessonPlanUseCase @Inject constructor(private val lessonPlanRepository: LessonPlanRepository) {

    suspend fun invoke(lessonPlan: LessonPlan) {
        lessonPlan.id?.let {
            lessonPlanRepository.update(lessonPlan)
            updateOtherPlans(lessonPlan)
        } ?: run {
            val newId = lessonPlanRepository.insert(lessonPlan.apply { isActive = true })
            updateOtherPlans(lessonPlanRepository.getById(newId))
        }
    }

    private suspend fun updateOtherPlans(lessonPlan: LessonPlan) {
        if (lessonPlan.isActive) {
            lessonPlanRepository.makeOtherPlansNotActive(lessonPlan)
        }
    }
}