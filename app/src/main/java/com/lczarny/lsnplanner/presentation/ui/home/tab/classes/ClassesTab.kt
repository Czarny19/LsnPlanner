package com.lczarny.lsnplanner.presentation.ui.home.tab.classes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lczarny.lsnplanner.data.common.model.ClassInfoModel
import com.lczarny.lsnplanner.data.common.model.ClassScheduleModel
import com.lczarny.lsnplanner.presentation.model.mapper.ClassViewType
import com.lczarny.lsnplanner.presentation.ui.home.HomeViewModel

@Composable
fun ClassesTab(padding: PaddingValues, viewModel: HomeViewModel, pagerState: PagerState) {
    val classesCurrentDate by viewModel.classesCurrentDate.collectAsState()
    val weekStartDate = classesCurrentDate.minusDays(classesCurrentDate.dayOfWeek.value.toLong())

    val classesDisplayType by viewModel.classesDisplayType.collectAsStateWithLifecycle()
    val classesWithSchedules by viewModel.classesWithSchedules.collectAsState()
    val classScheduleByHourMap: MutableList<Pair<ClassScheduleModel, ClassInfoModel>> = mutableListOf()

    classesWithSchedules.forEach {
        classScheduleByHourMap.addAll(it.value.map { classTime -> Pair(classTime, it.key) })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        ClassesTabTopNav(viewModel, pagerState)

        when (classesDisplayType) {
            ClassViewType.List -> ClassesTabList(viewModel, pagerState, weekStartDate, classScheduleByHourMap)
            ClassViewType.Timeline -> ClassesTabTimeline()
        }
    }
}