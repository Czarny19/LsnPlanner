package com.lczarny.lsnplanner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lczarny.lsnplanner.data.local.coverters.EnumConverter
import com.lczarny.lsnplanner.data.local.dao.ExamDao
import com.lczarny.lsnplanner.data.local.dao.ClassInfoDao
import com.lczarny.lsnplanner.data.local.dao.ClassScheduleDao
import com.lczarny.lsnplanner.data.local.dao.HomeworkDao
import com.lczarny.lsnplanner.data.local.dao.LessonPlanDao
import com.lczarny.lsnplanner.data.local.dao.NoteDao
import com.lczarny.lsnplanner.data.local.dao.SettingDao
import com.lczarny.lsnplanner.data.local.entity.Exam
import com.lczarny.lsnplanner.data.local.entity.ClassInfo
import com.lczarny.lsnplanner.data.local.entity.ClassSchedule
import com.lczarny.lsnplanner.data.local.entity.Homework
import com.lczarny.lsnplanner.data.local.entity.LessonPlan
import com.lczarny.lsnplanner.data.local.entity.Note
import com.lczarny.lsnplanner.data.local.entity.Setting

@Database(
    version = 4,
    exportSchema = false,
    entities = [
        ClassInfo::class,
        ClassSchedule::class,
        Exam::class,
        Homework::class,
        LessonPlan::class,
        Note::class,
        Setting::class
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
    abstract fun settingDao(): SettingDao
}