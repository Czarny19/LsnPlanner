package com.lczarny.lsnplanner.data.common.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import com.lczarny.lsnplanner.data.local.entity.ClassInfo
import com.lczarny.lsnplanner.data.local.entity.ClassSchedule
import com.lczarny.lsnplanner.data.local.entity.Exam
import com.lczarny.lsnplanner.data.local.entity.Homework

data class ClassInfoModel(
    val id: Long? = null,
    @ColumnInfo(name = "lesson_plan_id") val lessonPlanId: Long,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "teacher") var teacher: String = "",
    @ColumnInfo(name = "type") var type: ClassType,
    @ColumnInfo(name = "color") var color: Long = 0xFF394E85,
    @ColumnInfo(name = "address") var address: String? = null,
    @ColumnInfo(name = "note") var note: String? = null,
)

fun ClassInfo.toModel() = ClassInfoModel(
    id = this.id,
    lessonPlanId = this.lessonPlanId,
    name = this.name,
    teacher = this.teacher,
    type = this.type,
    color = this.color,
    address = this.address,
    note = this.note,
)

data class FullClassData(
    @Embedded val info: ClassInfo,
    @Relation(parentColumn = "id", entityColumn = "class_info_id") val schedules: List<ClassSchedule>,
    @Relation(parentColumn = "id", entityColumn = "class_info_id") val exams: List<Exam>,
    @Relation(parentColumn = "id", entityColumn = "class_info_id") val homeworks: List<Homework>,
)

data class FullClassDataModel(
    var info: ClassInfoModel,
    var schedules: List<ClassScheduleModel>,
    var exams: List<ExamModel>,
    var homeworks: List<HomeworkModel>
)

fun FullClassData.toModel() = FullClassDataModel(
    info = info.toModel(),
    schedules = schedules.map { it.toModel() },
    exams = exams.map { it.toModel() },
    homeworks = homeworks.map { it.toModel() }
)

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
