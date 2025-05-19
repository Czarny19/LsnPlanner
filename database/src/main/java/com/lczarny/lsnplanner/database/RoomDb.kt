package com.lczarny.lsnplanner.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lczarny.lsnplanner.database.coverters.EnumConverter
import com.lczarny.lsnplanner.database.dao.ClassInfoDao
import com.lczarny.lsnplanner.database.dao.ClassScheduleDao
import com.lczarny.lsnplanner.database.dao.ExamDao
import com.lczarny.lsnplanner.database.dao.HomeworkDao
import com.lczarny.lsnplanner.database.dao.LessonPlanDao
import com.lczarny.lsnplanner.database.dao.NoteDao
import com.lczarny.lsnplanner.database.dao.ProfileDao
import com.lczarny.lsnplanner.database.entity.ClassInfoEntity
import com.lczarny.lsnplanner.database.entity.ClassScheduleEntity
import com.lczarny.lsnplanner.database.entity.ExamEntity
import com.lczarny.lsnplanner.database.entity.HomeworkEntity
import com.lczarny.lsnplanner.database.entity.LessonPlanEntity
import com.lczarny.lsnplanner.database.entity.NoteEntity
import com.lczarny.lsnplanner.database.entity.ProfileEntity

@Database(
    version = 8,
    exportSchema = false,
    entities = [
        ClassInfoEntity::class,
        ClassScheduleEntity::class,
        ExamEntity::class,
        HomeworkEntity::class,
        LessonPlanEntity::class,
        NoteEntity::class,
        ProfileEntity::class
    ]
)
@TypeConverters(EnumConverter::class)
abstract class RoomDb : RoomDatabase() {
    abstract fun classInfoDao(): ClassInfoDao
    abstract fun classScheduleDao(): ClassScheduleDao
    abstract fun examDao(): ExamDao
    abstract fun homeworkDao(): HomeworkDao
    abstract fun lessonPlanDao(): LessonPlanDao
    abstract fun noteDao(): NoteDao
    abstract fun profileDao(): ProfileDao
}