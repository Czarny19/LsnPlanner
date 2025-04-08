package com.lczarny.lsnplanner.presentation.ui.classdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lczarny.lsnplanner.data.common.model.ClassInfoModel
import com.lczarny.lsnplanner.data.common.model.ClassScheduleModel
import com.lczarny.lsnplanner.data.common.model.ClassScheduleType
import com.lczarny.lsnplanner.data.common.model.ClassType
import com.lczarny.lsnplanner.data.common.model.ExamModel
import com.lczarny.lsnplanner.data.common.model.HomeworkModel
import com.lczarny.lsnplanner.data.common.model.ItemState
import com.lczarny.lsnplanner.data.common.model.LessonPlanModel
import com.lczarny.lsnplanner.data.common.model.defaultClassType
import com.lczarny.lsnplanner.data.common.repository.ClassInfoRepository
import com.lczarny.lsnplanner.data.common.repository.ClassScheduleRepository
import com.lczarny.lsnplanner.data.common.repository.LessonPlanRepository
import com.lczarny.lsnplanner.di.IoDispatcher
import com.lczarny.lsnplanner.presentation.model.DetailsScreenState
import com.lczarny.lsnplanner.utils.isDurationOverMidnight
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassDetailsViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val lessonPlanRepository: LessonPlanRepository,
    private val classInfoRepository: ClassInfoRepository,
    private val classTimeRepository: ClassScheduleRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(DetailsScreenState.Loading)

    private val _lessonPlan = MutableStateFlow<LessonPlanModel?>(null)
    private val _info = MutableStateFlow<ClassInfoModel?>(null)
    private val _schedules = MutableStateFlow<List<ClassScheduleModel>>(emptyList())
    private val _exams = MutableStateFlow<List<ExamModel>>(emptyList())
    private val _homeworks = MutableStateFlow<List<HomeworkModel>>(emptyList())

    private val _dataChanged = MutableStateFlow(false)
    private val _saveEnabled = MutableStateFlow(false)

    private lateinit var _initialData: ClassInfoModel
    private var _anyListChange = false

    private var _defaultWeekDay = 0

    private var _classTimeTempId = 0L

    val screenState = _screenState.asStateFlow()

    val lessonPlan = _lessonPlan.asStateFlow()
    val info = _info.asStateFlow()
    val schedules = _schedules.asStateFlow()
    val exams = _exams.asStateFlow()
    val homeworks = _homeworks.asStateFlow()

    val dataChanged = _dataChanged.asStateFlow()
    val saveEnabled = _saveEnabled.asStateFlow()

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

    fun initializeClass(lessonPlanId: Long, defaultWeekDay: Int, classInfoId: Long?) {
        if (_info.value != null) {
            return
        }

        _screenState.update { DetailsScreenState.Loading }
        _defaultWeekDay = defaultWeekDay

        viewModelScope.launch(Dispatchers.IO) {
            lessonPlanRepository.getActivePlan().collect { plan ->
                _lessonPlan.update { plan }

                classInfoId?.let { id ->
                    classInfoRepository.getFullDataById(id).let { fullClass ->
                        _info.update { fullClass.info }
                        _initialData = fullClass.info

                        _schedules.update { fullClass.schedules }
                        _exams.update { fullClass.exams }
                        _homeworks.update { fullClass.homeworks }

                        _screenState.update { DetailsScreenState.Edit }
                    }
                } ?: run {
                    ClassInfoModel(lessonPlanId = lessonPlanId, type = plan.type.defaultClassType()).let { classInfo ->
                        _info.update { classInfo }
                        _initialData = classInfo
                    }

                    _schedules.update { emptyList() }
                    _exams.update { emptyList() }
                    _homeworks.update { emptyList() }

                    _screenState.update { DetailsScreenState.Create }
                }
            }
        }.invokeOnCompletion {
            _screenState.update { if (classInfoId != null) DetailsScreenState.Edit else DetailsScreenState.Create }
        }
    }

    fun addClassSchedule() {
        ClassScheduleModel(localTempId = _classTimeTempId++, state = ItemState.New, classInfoId = 0L, weekDay = _defaultWeekDay).let { schedule ->
            _schedules.update { _schedules.value.toMutableList() + schedule }
            _saveEnabled.update { false }
            _anyListChange = true
            checkDataChanged()
        }
    }

    fun deleteClassSchedule(classSchedule: ClassScheduleModel) {
        _schedules.value.indexOf(classSchedule).let {
            if (it >= 0) {
                val newList = _schedules.value.toMutableList()

                newList[it].let { item ->
                    if (item.state == ItemState.New) newList.removeAt(it)
                    else item.apply { state = ItemState.ToBeDeleted }
                }

                _schedules.update { newList }
                _anyListChange = true
                checkDataChanged()
            }
        }
    }

    private fun updateClassSchedule(oldVal: ClassScheduleModel, newVal: ClassScheduleModel) {
        _schedules.value.indexOf(oldVal).let {
            if (it >= 0) {
                val newList = _schedules.value.toMutableList()
                newList[it] = newVal

                _schedules.update { newList }
                _anyListChange = true
                checkDataChanged()
            }
        }
    }

    fun updateClassScheduleType(classSchedule: ClassScheduleModel, value: ClassScheduleType) {
        updateClassSchedule(
            classSchedule,
            classSchedule.copy(
                type = value,
                weekDay = _defaultWeekDay,
                startDate = null,
                endDate = null
            )
        )
    }

    fun updateClassScheduleAddress(classSchedule: ClassScheduleModel, value: String) {
        updateClassSchedule(classSchedule, classSchedule.copy(address = value))
    }

    fun updateClassScheduleClassroom(classSchedule: ClassScheduleModel, value: String) {
        updateClassSchedule(classSchedule, classSchedule.copy(classroom = value))
    }

    fun updateClassScheduleDuration(classSchedule: ClassScheduleModel, value: Int) {
        updateClassSchedule(classSchedule, classSchedule.copy(durationMinutes = value))
    }

    fun updateClassScheduleWeekday(classSchedule: ClassScheduleModel, value: Int) {
        updateClassSchedule(classSchedule, classSchedule.copy(weekDay = value))
    }

    fun updateClassScheduleStartTime(classSchedule: ClassScheduleModel, hour: Int, minute: Int) {
        updateClassSchedule(classSchedule, classSchedule.copy(startHour = hour, startMinute = minute))
    }

    fun updateClassScheduleStartDate(classSchedule: ClassScheduleModel, value: Long) {
        updateClassSchedule(classSchedule, classSchedule.copy(startDate = value, endDate = null))
    }

    fun updateClassScheduleEndDate(classSchedule: ClassScheduleModel, value: Long) {
        updateClassSchedule(classSchedule, classSchedule.copy(endDate = value))
    }

    fun isClassScheduleNotValid(classTime: ClassScheduleModel, classroomChanged: Boolean, addressChanged: Boolean): Boolean {
        classTime.let {
            val classroomError = classroomChanged && it.classroom.isEmpty()
            val addressError = _lessonPlan.value?.addressEnabled == true && addressChanged && (it.address?.isEmpty() == true)
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
        _info.value?.let { classInfo ->
            _screenState.update { DetailsScreenState.Saving }

            viewModelScope.launch(ioDispatcher) {
                classInfo.id?.let { classInfoId ->
                    classInfoRepository.update(classInfo)
                    saveClassSchedule(classInfoId)
                } ?: run {
                    saveClassSchedule(classInfoRepository.insert(classInfo))
                }
            }.invokeOnCompletion {
                _screenState.update { DetailsScreenState.Finished }
            }
        }
    }

    private suspend fun saveClassSchedule(classInfoId: Long) {
        _schedules.value.forEach {
            when (it.state) {
                ItemState.New -> classTimeRepository.insert(it.apply { this.classInfoId = classInfoId })
                ItemState.Existing -> classTimeRepository.update(it)
                ItemState.ToBeDeleted -> classTimeRepository.delete(it.id!!)
            }
        }
    }

    private fun checkDataChanged() {
        _dataChanged.update { _initialData != _info.value || _anyListChange }

        if (_info.value?.name?.isEmpty() != false) {
            _saveEnabled.update { false }
            return
        }

        if (_schedules.value.firstOrNull { isClassScheduleNotValid(it, true, true) } != null) {
            _saveEnabled.update { false }
            return
        }

        _saveEnabled.update { true }
    }
}