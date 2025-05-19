package com.lczarny.lsnplanner.di

import android.content.Context
import androidx.room.Room
import com.lczarny.lsnplanner.database.RoomDb
import com.lczarny.lsnplanner.database.dao.ClassInfoDao
import com.lczarny.lsnplanner.database.dao.ClassScheduleDao
import com.lczarny.lsnplanner.database.dao.ExamDao
import com.lczarny.lsnplanner.database.dao.HomeworkDao
import com.lczarny.lsnplanner.database.dao.LessonPlanDao
import com.lczarny.lsnplanner.database.dao.NoteDao
import com.lczarny.lsnplanner.database.dao.ProfileDao
import com.lczarny.lsnplanner.database.repository.ClassInfoRepository
import com.lczarny.lsnplanner.database.repository.ClassScheduleRepository
import com.lczarny.lsnplanner.database.repository.ExamRepository
import com.lczarny.lsnplanner.database.repository.HomeworkRepository
import com.lczarny.lsnplanner.database.repository.LessonPlanRepository
import com.lczarny.lsnplanner.database.repository.NoteRepository
import com.lczarny.lsnplanner.database.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RoomModule {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): RoomDb =
        Room.databaseBuilder(context, RoomDb::class.java, "local_db").fallbackToDestructiveMigration(false).build()

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
    fun provideProfileDao(db: RoomDb): ProfileDao = db.profileDao()

    @Provides
    @Singleton
    fun provideProfileRepository(profileDao: ProfileDao): ProfileRepository = ProfileRepository(profileDao)
}