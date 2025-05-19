package com.lczarny.lsnplanner.presentation.ui.classdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.database.model.ClassInfo
import com.lczarny.lsnplanner.database.model.ClassSchedule
import com.lczarny.lsnplanner.database.model.ClassScheduleType
import com.lczarny.lsnplanner.database.model.ClassType
import com.lczarny.lsnplanner.database.model.Exam
import com.lczarny.lsnplanner.database.model.Homework
import com.lczarny.lsnplanner.database.model.ItemState
import com.lczarny.lsnplanner.database.model.LessonPlan
import com.lczarny.lsnplanner.di.IoDispatcher
import com.lczarny.lsnplanner.model.SessionInfo
import com.lczarny.lsnplanner.domain.cls.LoadClassUseCase
import com.lczarny.lsnplanner.domain.cls.SaveClassUseCase
import com.lczarny.lsnplanner.model.DetailsScreenState
import com.lczarny.lsnplanner.utils.deleteItem
import com.lczarny.lsnplanner.utils.isDurationOverMidnight
import com.lczarny.lsnplanner.utils.updateIfChanged
import com.lczarny.lsnplanner.utils.updateItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassDetailsViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val loadClassUseCase: LoadClassUseCase,
    private val saveClassUseCase: SaveClassUseCase,
    private val sessionInfo: SessionInfo,
) : ViewModel() {

    private val _screenState = MutableStateFlow(DetailsScreenState.Loading)

    private val _lessonPlan = MutableStateFlow<LessonPlan?>(null)
    private val _info = MutableStateFlow<ClassInfo?>(null)
    private val _schedules = MutableStateFlow<List<ClassSchedule>>(emptyList())
    private val _exams = MutableStateFlow<List<Exam>>(emptyList())
    private val _homeworks = MutableStateFlow<List<Homework>>(emptyList())

    private val _dataChanged = MutableStateFlow(false)
    private val _saveEnabled = MutableStateFlow(false)

    private lateinit var _initialData: ClassInfo
    private var _anyListChange = false

    private var _defaultWeekDay = 0

    private var _classScheduleTempId = 0L

    val screenState = _screenState.asStateFlow()

    val lessonPlan = _lessonPlan.asStateFlow()
    val info = _info.asStateFlow()
    val schedules = _schedules.asStateFlow()
    val exams = _exams.asStateFlow()
    val homeworks = _homeworks.asStateFlow()

    val dataChanged = _dataChanged.asStateFlow()
    val saveEnabled = _saveEnabled.asStateFlow()

    init {
        _lessonPlan.update { sessionInfo.activeLessonPlan }
    }

    fun initializeClass(defaultWeekDay: Int, classInfoId: Long?) {
        if (_info.value != null) {
            return
        }

        _screenState.update { DetailsScreenState.Loading }
        _defaultWeekDay = defaultWeekDay

        viewModelScope.launch(ioDispatcher) {
            loadClassUseCase.invoke(classInfoId).let { fullClassDataModel ->
                _initialData = fullClassDataModel.info

                _info.update { fullClassDataModel.info }
                _schedules.update { fullClassDataModel.schedules }
                _exams.update { fullClassDataModel.exams }
                _homeworks.update { fullClassDataModel.homeworks }
            }
        }.invokeOnCompletion {
            _screenState.update { if (classInfoId == null) DetailsScreenState.Create else DetailsScreenState.Edit }
        }
    }

    fun updateName(value: String) {
        _info.update { _info.value?.copy(name = value) }
        checkDataChanged()
    }

    fun updateClassType(value: ClassType) {
        _info.update { _info.value?.copy(type = value) }
        checkDataChanged()
    }

    fun updateClassColor(value: Long) {
        _info.update { _info.value?.copy(color = value) }
        checkDataChanged()
    }

    fun updateNote(value: String) {
        _info.update { _info.value?.copy(note = value) }
        checkDataChanged()
    }

    fun addClassSchedule() {
        ClassSchedule(localTempId = _classScheduleTempId++, state = ItemState.New, weekDay = _defaultWeekDay).let { schedule ->
            _schedules.update { _schedules.value.toMutableList() + schedule }
            _anyListChange = true
            checkDataChanged()
        }
    }

    private fun updateClassSchedule(oldVal: ClassSchedule, newVal: ClassSchedule) {
        _schedules.updateItem(oldVal, newVal) {
            _anyListChange = true
            checkDataChanged()
        }
    }

    fun deleteClassSchedule(classSchedule: ClassSchedule) {
        if (classSchedule.state == ItemState.New) {
            _schedules.deleteItem(classSchedule) {
                _anyListChange = true
            }
        } else {
            updateClassSchedule(classSchedule, classSchedule.copy(state = ItemState.ToBeDeleted))
        }
    }

    fun updateClassScheduleType(classSchedule: ClassSchedule, value: ClassScheduleType) {
        updateClassSchedule(
            classSchedule,
            classSchedule.copy(type = value, weekDay = _defaultWeekDay, startDate = null, endDate = null)
        )
    }

    fun updateClassScheduleAddress(classSchedule: ClassSchedule, value: String) {
        updateClassSchedule(classSchedule, classSchedule.copy(address = value))
    }

    fun updateClassScheduleClassroom(classSchedule: ClassSchedule, value: String) {
        updateClassSchedule(classSchedule, classSchedule.copy(classroom = value))
    }

    fun updateClassScheduleDuration(classSchedule: ClassSchedule, value: Int) {
        updateClassSchedule(classSchedule, classSchedule.copy(durationMinutes = value))
    }

    fun updateClassScheduleWeekday(classSchedule: ClassSchedule, value: Int) {
        updateClassSchedule(classSchedule, classSchedule.copy(weekDay = value))
    }

    fun updateClassScheduleStartTime(classSchedule: ClassSchedule, hour: Int, minute: Int) {
        updateClassSchedule(classSchedule, classSchedule.copy(startHour = hour, startMinute = minute))
    }

    fun updateClassScheduleStartDate(classSchedule: ClassSchedule, value: Long) {
        updateClassSchedule(classSchedule, classSchedule.copy(startDate = value, endDate = null))
    }

    fun updateClassScheduleEndDate(classSchedule: ClassSchedule, value: Long) {
        updateClassSchedule(classSchedule, classSchedule.copy(endDate = value))
    }

    fun isClassScheduleNotValid(classSchedule: ClassSchedule): Boolean {
        classSchedule.let {
            val classroomError = it.classroom.isEmpty()
            val addressError = _lessonPlan.value?.addressEnabled == true && (it.address?.isEmpty() == true)
            val durationOverMidnight = isDurationOverMidnight(it.durationMinutes, it.startHour, it.startMinute)
            val basicDataError = classroomError || addressError || durationOverMidnight || it.durationMinutes == 0

            return when (it.type) {
                ClassScheduleType.Weekly -> basicDataError
                ClassScheduleType.Period -> basicDataError || it.startDate == null || it.endDate == null
                ClassScheduleType.Single -> basicDataError || it.startDate == null
            }
        }
    }

    fun saveClass() {
        _screenState.update { DetailsScreenState.Saving }

        viewModelScope.launch(ioDispatcher) {
            saveClassUseCase.invoke(_info.value!!, _schedules.value)
        }.invokeOnCompletion {
            _screenState.update { DetailsScreenState.Finished }
        }
    }

    private fun checkDataChanged() {
        val dataChanged = _initialData != _info.value || _anyListChange
        val nameNotEmpty = _info.value?.name?.isNotEmpty() != false
        val schedulesValid = _schedules.value.firstOrNull { isClassScheduleNotValid(it) } == null

        _dataChanged.updateIfChanged(dataChanged)
        _saveEnabled.updateIfChanged(dataChanged && nameNotEmpty && schedulesValid)
    }
}