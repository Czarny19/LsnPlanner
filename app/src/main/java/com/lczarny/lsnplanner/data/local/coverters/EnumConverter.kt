package com.lczarny.lsnplanner.data.local.coverters

import androidx.room.TypeConverter
import com.lczarny.lsnplanner.data.local.model.LessonPlanType
import com.lczarny.lsnplanner.data.local.model.ToDoImportance

class EnumConverter {

    @TypeConverter
    fun toLessonPlanType(value: String): LessonPlanType = LessonPlanType.from(value)

    @TypeConverter
    fun fromLessonPlanType(value: LessonPlanType): String = value.raw

    @TypeConverter
    fun toToDoImportance(value: Int): ToDoImportance = ToDoImportance.from(value)

    @TypeConverter
    fun fromToDoImportance(value: ToDoImportance): Int = value.raw
}