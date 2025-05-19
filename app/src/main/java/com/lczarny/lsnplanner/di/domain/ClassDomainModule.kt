package com.lczarny.lsnplanner.di.domain

import com.lczarny.lsnplanner.database.repository.ClassInfoRepository
import com.lczarny.lsnplanner.database.repository.ClassScheduleRepository
import com.lczarny.lsnplanner.domain.cls.DeleteClassUseCase
import com.lczarny.lsnplanner.domain.cls.LoadClassListUseCase
import com.lczarny.lsnplanner.domain.cls.LoadClassUseCase
import com.lczarny.lsnplanner.domain.cls.LoadClassesSchedulesUseCase
import com.lczarny.lsnplanner.domain.cls.SaveClassUseCase
import com.lczarny.lsnplanner.model.SessionInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ClassDomainModule {

    @Provides
    fun LoadClassListUseCase(
        sessionRepository: SessionInfo,
        classInfoRepository: ClassInfoRepository
    ): LoadClassListUseCase = LoadClassListUseCase(sessionRepository, classInfoRepository)

    @Provides
    fun provideLoadClassUseCase(
        sessionRepository: SessionInfo,
        classInfoRepository: ClassInfoRepository
    ): LoadClassUseCase = LoadClassUseCase(sessionRepository, classInfoRepository)

    @Provides
    fun provideLoadClassesSchedulesUseCase(
        sessionRepository: SessionInfo,
        classInfoRepository: ClassInfoRepository,
        classScheduleRepository: ClassScheduleRepository
    ): LoadClassesSchedulesUseCase = LoadClassesSchedulesUseCase(sessionRepository, classInfoRepository, classScheduleRepository)

    @Provides
    fun provideSaveClassUseCase(
        classInfoRepository: ClassInfoRepository,
        classScheduleRepository: ClassScheduleRepository
    ): SaveClassUseCase = SaveClassUseCase(classInfoRepository, classScheduleRepository)

    @Provides
    fun provideDeleteClassUseCase(classInfoRepository: ClassInfoRepository): DeleteClassUseCase = DeleteClassUseCase(classInfoRepository)
}