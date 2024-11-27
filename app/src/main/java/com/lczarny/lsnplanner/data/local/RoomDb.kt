package com.lczarny.lsnplanner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lczarny.lsnplanner.data.local.coverters.EnumConverter
import com.lczarny.lsnplanner.data.local.dao.LessonPlanDao
import com.lczarny.lsnplanner.data.local.dao.PlanClassDao
import com.lczarny.lsnplanner.data.local.dao.ToDoDao
import com.lczarny.lsnplanner.data.local.entity.LessonPlan
import com.lczarny.lsnplanner.data.local.entity.PlanClass
import com.lczarny.lsnplanner.data.local.entity.ToDo

@Database(
    version = 12,
    exportSchema = false,
    entities = [
        LessonPlan::class,
        PlanClass::class,
        ToDo::class
    ]
)
@TypeConverters(EnumConverter::class)
abstract class RoomDb : RoomDatabase() {

    abstract fun lessonPlanDao(): LessonPlanDao
    abstract fun planClassDao(): PlanClassDao
    abstract fun toDoDao(): ToDoDao
}