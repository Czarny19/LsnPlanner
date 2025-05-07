package com.lczarny.lsnplanner.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.common.model.ClassScheduleType
import com.lczarny.lsnplanner.data.common.model.ClassScheduleWithInfoModel
import com.lczarny.lsnplanner.data.common.model.LessonPlanModel
import com.lczarny.lsnplanner.data.common.model.NoteModel
import com.lczarny.lsnplanner.data.common.model.ProfileModel
import com.lczarny.lsnplanner.data.common.repository.AuthRepository
import com.lczarny.lsnplanner.data.common.repository.ClassInfoRepository
import com.lczarny.lsnplanner.data.common.repository.ClassScheduleRepository
import com.lczarny.lsnplanner.data.common.repository.DataStoreRepository
import com.lczarny.lsnplanner.data.common.repository.LessonPlanRepository
import com.lczarny.lsnplanner.data.common.repository.NoteRepository
import com.lczarny.lsnplanner.data.common.repository.SessionRepository
import com.lczarny.lsnplanner.di.IoDispatcher
import com.lczarny.lsnplanner.presentation.model.BasicScreenState
import com.lczarny.lsnplanner.presentation.model.mapper.ClassViewType
import com.lczarny.lsnplanner.utils.dateFromEpochMilis
import com.lczarny.lsnplanner.utils.getWeekNumber
import com.lczarny.lsnplanner.utils.isBetweenDates
import com.lczarny.lsnplanner.utils.isSameDate
import com.lczarny.lsnplanner.utils.weekStartDate
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val sessionRepository: SessionRepository,
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val lessonPlanRepository: LessonPlanRepository,
    private val classInfoRepository: ClassInfoRepository,
    private val classScheduleRepository: ClassScheduleRepository,
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(BasicScreenState.Loading)

    private val _sessionActive = MutableStateFlow(true)
    private val _userProfile = MutableStateFlow<ProfileModel?>(null)

    private val _lessonPlan = MutableStateFlow<LessonPlanModel?>(null)

    private val _classesLoading = MutableStateFlow(true)
    private val _classesCurrentDate = MutableStateFlow(LocalDate.now())
    private val _classesDisplayType = MutableStateFlow(ClassViewType.List)
    private val _classesSchedulesPerDay = MutableStateFlow<Map<DayOfWeek, List<ClassScheduleWithInfoModel>>>(emptyMap())

    private val _notes = MutableStateFlow(emptyList<NoteModel>())
    private val _noteListSwipeTutorialDone = MutableStateFlow(false)

    private var _classesSchedulesWithInfo = emptyList<ClassScheduleWithInfoModel>()
    private var _classesCurrentWeek = LocalDate.now().getWeekNumber()

    val screenState = _screenState.asStateFlow()

    val sessionActive = _sessionActive.asStateFlow()
    val userProfile = _userProfile.asStateFlow()

    val lessonPlan = _lessonPlan.asStateFlow()

    val classesLoading = _classesLoading.asStateFlow()
    val classesCurrentDate = _classesCurrentDate.asStateFlow()
    val classesDisplayType = _classesDisplayType.asStateFlow()
    val classesSchedulesPerDay = _classesSchedulesPerDay.asStateFlow()

    val notes = _notes.asStateFlow()
    val noteListSwipeTutorialDone = _noteListSwipeTutorialDone.asStateFlow()

    init {
        watchUserSession()
        watchLessonPlan()
        watchSettings()
        watchClassesCurrentDate()
    }

    fun changeCurrentClassesDate(date: LocalDate) {
        _classesCurrentDate.update { date }
    }

    fun changeCurrentClassesWeek(goForward: Boolean) {
        _classesCurrentDate.update { _classesCurrentDate.value.plusWeeks(if (goForward) 1 else -1) }
    }

    private fun watchLessonPlan() {
        viewModelScope.launch(ioDispatcher) {
            lessonPlanRepository.watchActive(sessionRepository.activeProfile.id).flowOn(ioDispatcher).collect { lessonPlan ->
                if (lessonPlan != null) {
                    sessionRepository.activeLessonPlan = lessonPlan
                    _lessonPlan.update { lessonPlan }

                    watchClasses()
                    watchNotes()
                }

                _screenState.update { BasicScreenState.Ready }
            }
        }
    }

    private fun watchUserSession() {
        viewModelScope.launch(ioDispatcher) {
            sessionRepository.status.collect { status ->
                if (status is SessionStatus.Authenticated) {
                    _sessionActive.update { true }
                    _userProfile.update { sessionRepository.activeProfile }
                } else {
                    _sessionActive.update { false }
                }
            }
        }
    }

    private fun watchSettings() {
        viewModelScope.launch(ioDispatcher) {
            dataStoreRepository.getAppSettings().flowOn(ioDispatcher).collect { appSettings ->
                _classesDisplayType.update { ClassViewType.from(appSettings.homeClassesViewType ?: "") }

                _noteListSwipeTutorialDone.update { appSettings.tutorials.noteListSwipeDone }
            }
        }
    }

    private fun watchClasses() {
        val activePlanId = sessionRepository.activeLessonPlan.id!!

        viewModelScope.launch(ioDispatcher) {
            classInfoRepository.watchAll(activePlanId)
                .zip(classScheduleRepository.watchAll(activePlanId)) { infoList, scheduleList ->
                    scheduleList.map { schedule ->
                        ClassScheduleWithInfoModel(schedule, infoList.first { info -> info.id == schedule.classInfoId })
                    }
                }
                .takeWhile { it.any { scheduleWithInfo -> scheduleWithInfo.info.lessonPlanId == activePlanId } }
                .flowOn(ioDispatcher)
                .collect { schedulesWithInfo ->
                    _classesSchedulesWithInfo = schedulesWithInfo
                    // todo nie odswieza na save classy
                    createScheduleForThisWeek()
                }
        }
    }

    private fun watchClassesCurrentDate() {
        viewModelScope.launch(ioDispatcher) {
            _classesCurrentDate.collect { date ->
                if (_classesCurrentWeek != date.getWeekNumber()) {
                    _classesCurrentWeek = date.getWeekNumber()
                    createScheduleForThisWeek()
                }
            }
        }
    }

    private fun createScheduleForThisWeek() {
        _classesLoading.update { true }

        val currDate = _classesCurrentDate.value
        val weekStart = currDate.weekStartDate()
        val schedulesPerDay = mutableMapOf<DayOfWeek, List<ClassScheduleWithInfoModel>>()

        for (i in 1..7) {
            schedulesPerDay[DayOfWeek(i)] = _classesSchedulesWithInfo.filter {
                val start = dateFromEpochMilis(it.schedule.startDate ?: 0)
                val end = dateFromEpochMilis(it.schedule.endDate ?: 0)

                when (it.schedule.type) {
                    ClassScheduleType.Weekly -> it.schedule.weekDay == i
                    ClassScheduleType.Single -> start isSameDate weekStart.plusDays(i.toLong() - 1)
                    ClassScheduleType.Period -> it.schedule.weekDay == i && currDate.isBetweenDates(start, end)
                }
            }
        }

        _classesSchedulesPerDay.update { schedulesPerDay }
        _classesLoading.update { false }
    }

    private fun watchNotes() {
        viewModelScope.launch(ioDispatcher) {
            noteRepository.watchAll(sessionRepository.activeLessonPlan.id!!).flowOn(ioDispatcher).collect { notes -> _notes.update { notes } }
        }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch(ioDispatcher) {
            noteRepository.delete(noteId)
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

    fun signOut() {
        viewModelScope.launch(ioDispatcher) {
            authRepository.signOut()
        }
    }

    fun resetTutorials(onComplete: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            dataStoreRepository.resetTutorials()
        }.invokeOnCompletion {
            onComplete.invoke()
        }
    }
}