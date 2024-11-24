package com.lczarny.lsnplanner.di

import android.content.Context
import androidx.room.Room
import com.lczarny.lsnplanner.data.local.RoomDb
import com.lczarny.lsnplanner.data.local.dao.LessonPlanDao
import com.lczarny.lsnplanner.data.local.dao.PlanClassDao
import com.lczarny.lsnplanner.data.local.dao.ToDoDao
import com.lczarny.lsnplanner.data.local.repository.LessonPlanRepository
import com.lczarny.lsnplanner.data.local.repository.PlanClassRepository
import com.lczarny.lsnplanner.data.local.repository.ToDoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): RoomDb =
        Room.databaseBuilder(context, RoomDb::class.java, "local_db").fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideLessonPlanDao(db: RoomDb): LessonPlanDao = db.lessonPlanDao()

    @Provides
    @Singleton
    fun provideLessonPlanRepository(lessonPlanDao: LessonPlanDao): LessonPlanRepository = LessonPlanRepository(lessonPlanDao)

    @Provides
    @Singleton
    fun providePlanClassDao(db: RoomDb): PlanClassDao = db.planClassDao()

    @Provides
    @Singleton
    fun providePlanClassRepository(planClassDao: PlanClassDao): PlanClassRepository = PlanClassRepository(planClassDao)

    @Provides
    @Singleton
    fun provideToDoDao(db: RoomDb): ToDoDao = db.toDoDao()

    @Provides
    @Singleton
    fun provideToDoRepository(toDoDao: ToDoDao): ToDoRepository = ToDoRepository(toDoDao)
}