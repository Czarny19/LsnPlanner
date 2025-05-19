package com.lczarny.lsnplanner.domain.plan

import com.lczarny.lsnplanner.database.repository.LessonPlanRepository
import javax.inject.Inject

class DeleteLessonPlanUseCase @Inject constructor(private val lessonPlanRepository: LessonPlanRepository) {

    suspend fun invoke(id: Long) {
        lessonPlanRepository.delete(id)
    }
}