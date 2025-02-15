package com.lczarny.lsnplanner.di

import android.content.Context
import androidx.room.Room
import com.lczarny.lsnplanner.data.local.RoomDb
import com.lczarny.lsnplanner.data.local.dao.ClassEventDao
import com.lczarny.lsnplanner.data.local.dao.ClassInfoDao
import com.lczarny.lsnplanner.data.local.dao.ClassTimeDao
import com.lczarny.lsnplanner.data.local.dao.LessonPlanDao
import com.lczarny.lsnplanner.data.local.dao.NoteDao
import com.lczarny.lsnplanner.data.local.dao.SettingDao
import com.lczarny.lsnplanner.data.local.repository.ClassEventRepository
import com.lczarny.lsnplanner.data.local.repository.ClassInfoRepository
import com.lczarny.lsnplanner.data.local.repository.ClassTimeRepository
import com.lczarny.lsnplanner.data.local.repository.LessonPlanRepository
import com.lczarny.lsnplanner.data.local.repository.NoteRepository
import com.lczarny.lsnplanner.data.local.repository.SettingRepository
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
    fun provideClassEventDao(db: RoomDb): ClassEventDao = db.classEventDao()

    @Provides
    @Singleton
    fun provideClassEventRepository(classEventDao: ClassEventDao): ClassEventRepository = ClassEventRepository(classEventDao)

    @Provides
    @Singleton
    fun provideClassInfoDao(db: RoomDb): ClassInfoDao = db.classInfoDao()

    @Provides
    @Singleton
    fun provideClassInfoRepository(classInfoDao: ClassInfoDao): ClassInfoRepository = ClassInfoRepository(classInfoDao)

    @Provides
    @Singleton
    fun provideClassTimeDao(db: RoomDb): ClassTimeDao = db.classTimeDao()

    @Provides
    @Singleton
    fun provideClassTimeRepository(classTimeDao: ClassTimeDao): ClassTimeRepository = ClassTimeRepository(classTimeDao)

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