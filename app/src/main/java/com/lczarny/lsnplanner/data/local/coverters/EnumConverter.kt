package com.lczarny.lsnplanner.data.local.coverters

import androidx.room.TypeConverter
import com.lczarny.lsnplanner.data.local.model.LessonPlanType

class EnumConverter {

    @TypeConverter
    fun toLessonPlanType(value: String): LessonPlanType = LessonPlanType.from(value)

    @TypeConverter
    fun fromLessonPlanType(value: LessonPlanType): String = value.raw
}