package com.lczarny.lsnplanner.di.domain

import com.lczarny.lsnplanner.database.repository.LessonPlanRepository
import com.lczarny.lsnplanner.domain.plan.DeleteLessonPlanUseCase
import com.lczarny.lsnplanner.domain.plan.LoadActiveLessonPlanUseCase
import com.lczarny.lsnplanner.domain.plan.LoadLessonPlanListUseCase
import com.lczarny.lsnplanner.domain.plan.LoadLessonPlanUseCase
import com.lczarny.lsnplanner.domain.plan.SaveLessonPlanUseCase
import com.lczarny.lsnplanner.domain.plan.SetLessonPlanActiveUseCase
import com.lczarny.lsnplanner.model.SessionInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PlanDomainModule {

    @Provides
    fun provideLoadLessonPlanListUseCase(
        sessionRepository: SessionInfo,
        lessonPlanRepository: LessonPlanRepository
    ): LoadLessonPlanListUseCase = LoadLessonPlanListUseCase(sessionRepository, lessonPlanRepository)

    @Provides
    fun provideLoadLessonPlanUseCase(
        sessionRepository: SessionInfo,
        lessonPlanRepository: LessonPlanRepository
    ): LoadLessonPlanUseCase = LoadLessonPlanUseCase(sessionRepository, lessonPlanRepository)

    @Provides
    fun provideLoadActiveLessonPlanUseCase(
        sessionRepository: SessionInfo,
        lessonPlanRepository: LessonPlanRepository
    ): LoadActiveLessonPlanUseCase = LoadActiveLessonPlanUseCase(sessionRepository, lessonPlanRepository)

    @Provides
    fun provideSetLessonPlanActiveUseCase(lessonPlanRepository: LessonPlanRepository): SetLessonPlanActiveUseCase =
        SetLessonPlanActiveUseCase(lessonPlanRepository)

    @Provides
    fun provideSaveLessonPlanUseCase(lessonPlanRepository: LessonPlanRepository): SaveLessonPlanUseCase =
        SaveLessonPlanUseCase(lessonPlanRepository)

    @Provides
    fun provideDeleteLessonPlanUseCase(lessonPlanRepository: LessonPlanRepository): DeleteLessonPlanUseCase =
        DeleteLessonPlanUseCase(lessonPlanRepository)
}