package com.lczarny.lsnplanner.di

import android.content.Context
import androidx.room.Room
import com.lczarny.lsnplanner.data.local.RoomDb
import com.lczarny.lsnplanner.data.local.dao.ClassInfoDao
import com.lczarny.lsnplanner.data.local.dao.ClassScheduleDao
import com.lczarny.lsnplanner.data.local.dao.ExamDao
import com.lczarny.lsnplanner.data.local.dao.HomeworkDao
import com.lczarny.lsnplanner.data.local.dao.LessonPlanDao
import com.lczarny.lsnplanner.data.local.dao.NoteDao
import com.lczarny.lsnplanner.data.local.dao.SettingDao
import com.lczarny.lsnplanner.data.common.repository.ClassInfoRepository
import com.lczarny.lsnplanner.data.common.repository.ClassScheduleRepository
import com.lczarny.lsnplanner.data.common.repository.ExamRepository
import com.lczarny.lsnplanner.data.common.repository.HomeworkRepository
import com.lczarny.lsnplanner.data.common.repository.LessonPlanRepository
import com.lczarny.lsnplanner.data.common.repository.NoteRepository
import com.lczarny.lsnplanner.data.common.repository.SettingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): RoomDb =
        Room.databaseBuilder(context, RoomDb::class.java, "local_db").fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideClassInfoDao(db: RoomDb): ClassInfoDao = db.classInfoDao()

    @Provides
    @Singleton
    fun provideClassInfoRepository(classInfoDao: ClassInfoDao): ClassInfoRepository = ClassInfoRepository(classInfoDao)

    @Provides
    @Singleton
    fun provideClassScheduleDao(db: RoomDb): ClassScheduleDao = db.classScheduleDao()

    @Provides
    @Singleton
    fun provideClassScheduleRepository(classScheduleDao: ClassScheduleDao): ClassScheduleRepository = ClassScheduleRepository(classScheduleDao)

    @Provides
    @Singleton
    fun provideExamDao(db: RoomDb): ExamDao = db.examDao()

    @Provides
    @Singleton
    fun provideExamRepository(examDao: ExamDao): ExamRepository = ExamRepository(examDao)

    @Provides
    @Singleton
    fun provideHomeworkDao(db: RoomDb): HomeworkDao = db.homeworkDao()

    @Provides
    @Singleton
    fun provideHomeworkRepository(homeworkDao: HomeworkDao): HomeworkRepository = HomeworkRepository(homeworkDao)

    @Provides
    @Singleton
    fun provideLessonPlanDao(db: RoomDb): LessonPlanDao = db.lessonPlanDao()

    @Provides
    @Singleton
    fun provideLessonPlanRepository(lessonPlanDao: LessonPlanDao): LessonPlanRepository = LessonPlanRepository(lessonPlanDao)

    @Provides
    @Singleton
    fun provideNoteDao(db: RoomDb): NoteDao = db.noteDao()

    @Provides
    @Singleton
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository = NoteRepository(noteDao)

    @Provides
    @Singleton
    fun provideSettingDao(db: RoomDb): SettingDao = db.settingDao()

    @Provides
    @Singleton
    fun provideSettingRepository(settingDao: SettingDao): SettingRepository = SettingRepository(settingDao)
}