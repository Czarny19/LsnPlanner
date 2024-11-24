package com.lczarny.lsnplanner.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.lczarny.lsnplanner.data.local.entity.PlanClass
import com.lczarny.lsnplanner.data.local.entity.ToDo

data class PlanClassWithToDos(
    @Embedded val planClass: PlanClass,
    @Relation(parentColumn = "id", entityColumn = "class_id") val toDos: List<ToDo>,
)