package com.lczarny.lsnplanner.database.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import com.lczarny.lsnplanner.database.entity.ClassInfoEntity
import com.lczarny.lsnplanner.database.entity.ClassScheduleEntity
import com.lczarny.lsnplanner.database.entity.ExamEntity
import com.lczarny.lsnplanner.database.entity.HomeworkEntity

data class ClassInfo(
    @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "lesson_plan_id") val lessonPlanId: Long,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "type") var type: ClassType,
    @ColumnInfo(name = "color") var color: Long = 0xFF394E85,
    @ColumnInfo(name = "address") var address: String? = null,
    @ColumnInfo(name = "note") var note: String? = null,
)

data class FullClass(
    @Embedded var info: ClassInfo,
    @Relation(parentColumn = "id", entityColumn = "class_info_id") var schedules: List<ClassSchedule>,
    @Relation(parentColumn = "id", entityColumn = "class_info_id") var exams: List<Exam>,
    @Relation(parentColumn = "id", entityColumn = "class_info_id") var homeworks: List<Homework>
)

@Keep
enum class ClassType(val raw: String) {
    Class("Class"),
    Extracurricular("Extracurricular"),
    Lecture("Lecture"),
    Practical("Practical"),
    Laboratory("Laboratory"),
    Seminar("Seminar"),
    Workshop("Workshop"),
    Other("Other");

    companion object {
        fun from(find: String): ClassType = ClassType.entries.find { it.raw == find } ?: Other
    }
}

class FullClassEntity internal constructor(
    @Embedded internal val info: ClassInfoEntity,
    @Relation(parentColumn = "id", entityColumn = "class_info_id") internal val schedules: List<ClassScheduleEntity>,
    @Relation(parentColumn = "id", entityColumn = "class_info_id") internal val exams: List<ExamEntity>,
    @Relation(parentColumn = "id", entityColumn = "class_info_id") internal val homeworks: List<HomeworkEntity>,
)

internal fun FullClassEntity.toModel() = FullClass(
    info = ClassInfo(
        id = info.id,
        lessonPlanId = info.lessonPlanId,
        name = info.name,
        type = info.type,
        color = info.color,
        address = info.address,
        note = info.note,
    ),
    schedules = schedules.map {
        ClassSchedule(
            id = it.id,
            classInfoId = it.classInfoId,
            type = it.type,
            durationMinutes = it.durationMinutes,
            address = it.address,
            classroom = it.classroom,
            startHour = it.startHour,
            startMinute = it.startMinute,
            weekDay = it.weekDay,
            startDate = it.startDate,
            endDate = it.endDate
        )
    },
    exams = exams.map {
        Exam(
            id = it.id,
            classInfoId = it.classInfoId,
            name = it.name,
            date = it.date,
            description = it.description,
            importance = it.importance,
        )
    },
    homeworks = homeworks.map {
        Homework(
            id = it.id,
            classInfoId = it.classInfoId,
            name = it.name,
            dueDate = it.dueDate,
            description = it.description,
            done = it.done,
            importance = it.importance,
        )
    }
)