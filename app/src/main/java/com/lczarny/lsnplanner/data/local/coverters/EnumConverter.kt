package com.lczarny.lsnplanner.data.local.coverters

import androidx.room.TypeConverter
import com.lczarny.lsnplanner.data.common.model.ClassScheduleType
import com.lczarny.lsnplanner.data.common.model.ClassType
import com.lczarny.lsnplanner.data.common.model.Importance
import com.lczarny.lsnplanner.data.common.model.LessonPlanType

class EnumConverter {

    @TypeConverter
    fun toClassType(value: String): ClassType = ClassType.from(value)

    @TypeConverter
    fun fromClassType(value: ClassType): String = value.raw

    @TypeConverter
    fun toClassTimeType(value: String): ClassScheduleType = ClassScheduleType.from(value)

    @TypeConverter
    fun fromClassTimeType(value: ClassScheduleType): String = value.raw

    @TypeConverter
    fun toLessonPlanType(value: String): LessonPlanType = LessonPlanType.from(value)

    @TypeConverter
    fun fromLessonPlanType(value: LessonPlanType): String = value.raw

    @TypeConverter
    fun toImportance(value: Int): Importance = Importance.from(value)

    @TypeConverter
    fun fromImportance(value: Importance): Int = value.raw
}