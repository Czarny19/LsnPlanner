package com.lczarny.lsnplanner.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.database.model.ClassScheduleType
import com.lczarny.lsnplanner.database.model.ClassScheduleWithInfo
import com.lczarny.lsnplanner.database.model.LessonPlan
import com.lczarny.lsnplanner.database.model.Note
import com.lczarny.lsnplanner.database.model.Profile
import com.lczarny.lsnplanner.di.IoDispatcher
import com.lczarny.lsnplanner.domain.auth.SignOutUseCase
import com.lczarny.lsnplanner.domain.cls.LoadClassesSchedulesUseCase
import com.lczarny.lsnplanner.domain.datastore.GetAppSettingsUseCase
import com.lczarny.lsnplanner.domain.datastore.ResetTutorialsUseCase
import com.lczarny.lsnplanner.domain.datastore.SetAppSettingUseCase
import com.lczarny.lsnplanner.domain.note.DeleteNoteUseCase
import com.lczarny.lsnplanner.domain.note.LoadNoteListUseCase
import com.lczarny.lsnplanner.domain.plan.LoadActiveLessonPlanUseCase
import com.lczarny.lsnplanner.model.BasicScreenState
import com.lczarny.lsnplanner.model.Pref
import com.lczarny.lsnplanner.model.SessionInfo
import com.lczarny.lsnplanner.model.mapper.ClassViewType
import com.lczarny.lsnplanner.utils.dateFromEpochMilis
import com.lczarny.lsnplanner.utils.getWeekNumber
import com.lczarny.lsnplanner.utils.isBetweenDates
import com.lczarny.lsnplanner.utils.isSameDate
import com.lczarny.lsnplanner.utils.updateIfChanged
import com.lczarny.lsnplanner.utils.weekStartDate
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val sessionInfo: SessionInfo,
    private val loadActiveLessonPlanUseCase: LoadActiveLessonPlanUseCase,
    private val loadClassesSchedulesUseCase: LoadClassesSchedulesUseCase,
    private val loadNoteListUseCase: LoadNoteListUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val resetTutorialsUseCase: ResetTutorialsUseCase,
    private val setAppSettingUseCase: SetAppSettingUseCase,
) : ViewModel() {

    private val _screenState = MutableStateFlow(BasicScreenState.Loading)

    private val _sessionActive = MutableStateFlow(true)
    private val _userProfile = MutableStateFlow<Profile?>(null)

    private val _lessonPlan = MutableStateFlow<LessonPlan?>(null)

    private val _classesLoading = MutableStateFlow(true)
    private val _classesCurrentDate = MutableStateFlow(LocalDate.now())
    private val _classesDisplayType = MutableStateFlow(ClassViewType.List)
    private val _classesSchedulesPerDay = MutableStateFlow<Map<DayOfWeek, List<ClassScheduleWithInfo>>>(emptyMap())

    private val _notes = MutableStateFlow(emptyList<Note>())
    private val _noteListSwipeTutorialDone = MutableStateFlow(false)

    private var _classesSchedulesWithInfo = emptyList<ClassScheduleWithInfo>()
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

    fun deleteNote(noteId: Long) {
        viewModelScope.launch(ioDispatcher) {
            deleteNoteUseCase.invoke(noteId)
        }
    }

    fun changeClassesViewType(viewType: ClassViewType) {
        viewModelScope.launch(ioDispatcher) {
            delay(200)
            _classesDisplayType.update { viewType }
            setAppSettingUseCase.invoke(Pref.HomeClassesView(), viewType.raw)
        }
    }

    fun markNoteListSwipeTutorialDone() {
        viewModelScope.launch(ioDispatcher) {
            _noteListSwipeTutorialDone.update { true }
            setAppSettingUseCase.invoke(Pref.TutorialNoteListSwipe(), "true")
        }
    }

    fun signOut() {
        viewModelScope.launch(ioDispatcher) {
            signOutUseCase.invoke()
        }
    }

    fun resetTutorials(onComplete: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            resetTutorialsUseCase.invoke()
        }.invokeOnCompletion {
            onComplete.invoke()
        }
    }

    private fun watchUserSession() {
        viewModelScope.launch(ioDispatcher) {
            sessionInfo.status.collect { status ->
                _sessionActive.updateIfChanged(status is SessionStatus.Authenticated)
                _userProfile.update { sessionInfo.activeProfile }
            }
        }
    }

    private fun watchSettings() {
        viewModelScope.launch(ioDispatcher) {
            getAppSettingsUseCase.invoke().flowOn(ioDispatcher).collect { appSettings ->
                _classesDisplayType.update { ClassViewType.from(appSettings.homeClassesViewType ?: "") }
                _noteListSwipeTutorialDone.update { appSettings.tutorials.noteListSwipeDone }
            }
        }
    }

    private fun watchLessonPlan() {
        viewModelScope.launch(ioDispatcher) {
            loadActiveLessonPlanUseCase.invoke().flowOn(ioDispatcher).collect { lessonPlan ->
                _lessonPlan.update { lessonPlan }

                watchClasses()
                watchNotes()

                _screenState.update { BasicScreenState.Ready }
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

    private fun watchClasses() {
        viewModelScope.launch(ioDispatcher) {
            loadClassesSchedulesUseCase.invoke().flowOn(ioDispatcher).collect { schedulesWithInfo ->
                _classesSchedulesWithInfo = schedulesWithInfo
                // todo nie odswieza na save classy
                createScheduleForThisWeek()
            }
        }
    }

    private fun createScheduleForThisWeek() {
        _classesLoading.update { true }

        val currDate = _classesCurrentDate.value
        val weekStart = currDate.weekStartDate()
        val schedulesPerDay = mutableMapOf<DayOfWeek, List<ClassScheduleWithInfo>>()

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
            loadNoteListUseCase.invoke().flowOn(ioDispatcher).collect { notes -> _notes.update { notes } }
        }
    }
}