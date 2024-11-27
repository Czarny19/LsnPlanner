package com.lczarny.lsnplanner.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import com.lczarny.lsnplanner.data.local.entity.PlanClass
import com.lczarny.lsnplanner.data.local.entity.ToDo

data class PlanClassModel(
    val id: Long? = null,
    val name: String,
    @ColumnInfo(name = "is_cyclical") val isCyclical: Boolean,
    val note: String? = null,
    @ColumnInfo(name = "week_day") val weekDay: Int? = null,
    @ColumnInfo(name = "start_time") val startTime: Long? = null,
    @ColumnInfo(name = "end_time") val endTime: Long? = null,
    @ColumnInfo(name = "single_date") val singleDate: Long? = null,
    @ColumnInfo(name = "class_address") val classAddress: String? = null,
    @ColumnInfo(name = "class_number") val classNumber: String? = null,
    @ColumnInfo(name = "lesson_plan_id") val lessonPlanId: Long,
)

fun PlanClass.mapToModel() = PlanClassModel(
    id = this.id,
    name = this.name,
    isCyclical = this.isCyclical,
    note = this.note,
    weekDay = this.weekDay,
    startTime = this.startTime,
    endTime = this.endTime,
    singleDate = this.singleDate,
    classAddress = this.classAddress,
    classNumber = this.classNumber,
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