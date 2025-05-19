package com.lczarny.lsnplanner.domain.cls

import com.lczarny.lsnplanner.database.model.ClassInfo
import com.lczarny.lsnplanner.database.model.ClassSchedule
import com.lczarny.lsnplanner.database.model.ItemState
import com.lczarny.lsnplanner.database.repository.ClassInfoRepository
import com.lczarny.lsnplanner.database.repository.ClassScheduleRepository
import javax.inject.Inject

class SaveClassUseCase @Inject constructor(
    private val classInfoRepository: ClassInfoRepository,
    private val classScheduleRepository: ClassScheduleRepository
) {

    suspend fun invoke(classInfo: ClassInfo, classSchedules: List<ClassSchedule>) {
        val id = classInfo.id?.let { classInfoId ->
            classInfoRepository.update(classInfo)
            classInfo.id
        } ?: run {
            classInfoRepository.insert(classInfo)
        }

        classSchedules.forEach { saveClassSchedule(it, id) }
    }

    private suspend fun saveClassSchedule(classSchedule: ClassSchedule, classInfoId: Long) {
        when (classSchedule.state) {
            ItemState.New -> classScheduleRepository.insert(classSchedule.apply { this.classInfoId = classInfoId })
            ItemState.Existing -> classScheduleRepository.update(classSchedule)
            ItemState.ToBeDeleted -> classScheduleRepository.delete(classSchedule.id!!)
        }
    }
}