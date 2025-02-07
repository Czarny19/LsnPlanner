package com.lczarny.lsnplanner.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import com.lczarny.lsnplanner.data.local.entity.PlanClass
import com.lczarny.lsnplanner.data.local.entity.ToDo

data class PlanClassModel(
    var id: Long? = null,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "type") var type: PlanClassType,
    @ColumnInfo(name = "color") var color: Long = PlanClassColor.Default.raw,
    @ColumnInfo(name = "note") var note: String? = null,
    @ColumnInfo(name = "week_day") var weekDay: Int? = null,
    @ColumnInfo(name = "start_date") var startDate: Long? = null,
    @ColumnInfo(name = "start_hour") var startHour: Int = 8,
    @ColumnInfo(name = "start_minute") var startMinute: Int = 0,
    @ColumnInfo(name = "duration_minutes") var durationMinutes: Int = 45,
    @ColumnInfo(name = "classroom") var classroom: String? = null,
    @ColumnInfo(name = "lesson_plan_id") var lessonPlanId: Long,
)

enum class PlanClassColor(val raw: Long) {
    Default(0xFF394E85),
    Red(0xFFFF0000),
    Green(0xFF008000),
    Purple(0xFF800080);

    companion object {
        fun from(find: Long): PlanClassColor = PlanClassColor.entries.find { it.raw == find } ?: Default
    }
}

fun PlanClass.mapToModel() = PlanClassModel(
    id = this.id,
    name = this.name,
    type = this.type,
    color = this.color,
    note = this.note,
    weekDay = this.weekDay,
    startDate = this.startDate,
    startHour = this.startHour,
    startMinute = this.startMinute,
    durationMinutes = this.durationMinutes,
    classroom = this.classroom,
    lessonPlanId = this.lessonPlanId
)

data class PlanClassWithToDos(
    @Embedded val planClass: PlanClass,
    @Relation(parentColumn = "id", entityColumn = "class_id") val toDos: List<ToDo>,
)

data class PlanClassWithToDosModel(
    val planClass: PlanClassModel,
    val toDos: List<ToDoModel>,
)

fun PlanClassWithToDos.mapToModel() = PlanClassWithToDosModel(
    planClass = planClass.mapToModel(),
    toDos = toDos.map { it.mapToModel() },
)

enum class PlanClassType(val raw: String) {
    Class("Class"),
    PE("PE"),
    Lecture("Lecture"),
    Practical("Practical"),
    Laboratory("Laboratory"),
    Seminar("Seminar"),
    Workshop("Workshop"),
    Other("Other");

    companion object {
        fun from(find: String): PlanClassType = PlanClassType.entries.find { it.raw == find } ?: Other
    }
}

fun LessonPlanType.planClassTypes(): List<PlanClassType> = when (this) {
    LessonPlanType.School -> listOf(
        PlanClassType.Class,
        PlanClassType.PE,
        PlanClassType.Other
    )

    LessonPlanType.University -> listOf(
        PlanClassType.Lecture,
        PlanClassType.Practical,
        PlanClassType.Laboratory,
        PlanClassType.Seminar,
        PlanClassType.Workshop,
        PlanClassType.Other
    )
}

fun LessonPlanType.defaultClassType() = when (this) {
    LessonPlanType.School -> PlanClassType.Class
    LessonPlanType.University -> PlanClassType.Lecture
}
