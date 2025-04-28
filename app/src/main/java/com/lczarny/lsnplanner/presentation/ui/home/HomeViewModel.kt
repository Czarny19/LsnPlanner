package com.lczarny.lsnplanner.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.common.model.ClassInfoModel
import com.lczarny.lsnplanner.data.common.model.ClassScheduleModel
import com.lczarny.lsnplanner.data.common.model.LessonPlanModel
import com.lczarny.lsnplanner.data.common.model.NoteModel
import com.lczarny.lsnplanner.data.common.repository.ClassInfoRepository
import com.lczarny.lsnplanner.data.common.repository.ClassScheduleRepository
import com.lczarny.lsnplanner.data.common.repository.DataStoreRepository
import com.lczarny.lsnplanner.data.common.repository.LessonPlanRepository
import com.lczarny.lsnplanner.data.common.repository.NoteRepository
import com.lczarny.lsnplanner.di.IoDispatcher
import com.lczarny.lsnplanner.presentation.model.BasicScreenState
import com.lczarny.lsnplanner.presentation.model.mapper.ClassViewType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val dataStoreRepository: DataStoreRepository,
    private val lessonPlanRepository: LessonPlanRepository,
    private val classInfoRepository: ClassInfoRepository,
    private val classTimeRepository: ClassScheduleRepository,
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(BasicScreenState.Loading)
    private val _firstLaunchDone = MutableStateFlow(false)

    private val _userName = MutableStateFlow("")

    private val _lessonPlan = MutableStateFlow<LessonPlanModel?>(null)

    private val _classesWithSchedules = MutableStateFlow(mapOf<ClassInfoModel, List<ClassScheduleModel>>())
    private val _classesCurrentDate = MutableStateFlow(LocalDate.now())
    private val _classesDisplayType = MutableStateFlow(ClassViewType.List)

    private val _notes = MutableStateFlow(emptyList<NoteModel>())
    private val _noteListSwipeTutorialDone = MutableStateFlow(false)

    val screenState = _screenState.asStateFlow()
    val firstLaunchDone = _firstLaunchDone.asStateFlow()

    // TODO remove
    val userName = _userName.asStateFlow()

    val lessonPlan = _lessonPlan.asStateFlow()

    val classesWithSchedules = _classesWithSchedules.asStateFlow()
    val classesCurrentDate = _classesCurrentDate.asStateFlow()
    val classesDisplayType = _classesDisplayType.asStateFlow()

    val notes = _notes.asStateFlow()
    val noteListSwipeTutorialDone = _noteListSwipeTutorialDone.asStateFlow()

    init {
        getActiveLessonPlan()
        getSettings()
    }

    fun setFirstLaunchDone() {
        _firstLaunchDone.update { true }
    }

    fun changeCurrentClassesDate(date: LocalDate) {
        _classesCurrentDate.update { date }
    }

    fun changeCurrentClassesWeek(goForward: Boolean) {
        _classesCurrentDate.update { _classesCurrentDate.value.plusWeeks(if (goForward) 1 else -1) }
    }

    private fun getActiveLessonPlan() {
        viewModelScope.launch(ioDispatcher) {
            lessonPlanRepository.getActivePlan().collect { plan ->
                _lessonPlan.update { plan }

                plan.id?.let { id ->
                    getClasses(id)
                    getNotes(id)
                }

                delay(500)

                _screenState.update { BasicScreenState.Ready }
            }
        }
    }

    private fun getSettings() {
        viewModelScope.launch(ioDispatcher) {
            dataStoreRepository.getAppSettings().flowOn(ioDispatcher).collect { appSettings ->
                _classesDisplayType.update { ClassViewType.from(appSettings.homeClassesViewType ?: "") }

                _noteListSwipeTutorialDone.update { appSettings.tutorials.noteListSwipeDone }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getClasses(lessonPlanId: Long) {
        viewModelScope.launch(ioDispatcher) {
            classInfoRepository.getAllForLessonPlan(lessonPlanId)
                .flowOn(ioDispatcher)
                .collect { classInfoList ->
                    flowOf(*classInfoList.toTypedArray())
                        .flowOn(ioDispatcher)
                        .flatMapMerge { classInfo ->
                            classTimeRepository.getAllForClassInfo(classInfo.id!!)
                                .onEach { }
                                .flowOn(ioDispatcher)
                                .map { classTimes -> classInfo to classTimes }
                        }
                        .collect { classInfoWithTimes ->
                            // TODO does not refresh on add schedule
                            _classesWithSchedules.update {
                                _classesWithSchedules.value.toMutableMap().plus(classInfoWithTimes)
                            }
                        }
                }
        }
    }

    private fun getNotes(lessonPlanId: Long) {
        viewModelScope.launch(ioDispatcher) {
            noteRepository.getAllForLessonPlan(lessonPlanId).flowOn(ioDispatcher).collect { notes -> _notes.update { notes } }
        }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch(ioDispatcher) {
            noteRepository.delete(noteId)
        }.invokeOnCompletion {
            getNotes(_lessonPlan.value?.id!!)
        }
    }

    fun changeClassesViewType(viewType: ClassViewType) {
        viewModelScope.launch(ioDispatcher) {
            delay(200)
            _classesDisplayType.update { viewType }
            dataStoreRepository.setHomeClassesViewType(viewType.raw)
        }
    }

    fun markNoteListSwipeTutorialDone() {
        viewModelScope.launch(ioDispatcher) {
            _noteListSwipeTutorialDone.update { true }
            dataStoreRepository.setTutorialNoteListSwipeDone()
        }
    }

    fun resetTutorials(onComplete: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            dataStoreRepository.resetTutorials()
            getSettings()
        }.invokeOnCompletion {
            onComplete.invoke()
        }
    }
}