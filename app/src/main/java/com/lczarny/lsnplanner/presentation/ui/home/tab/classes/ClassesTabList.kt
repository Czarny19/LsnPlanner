package com.lczarny.lsnplanner.presentation.ui.home.tab.classes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.common.model.ClassInfoModel
import com.lczarny.lsnplanner.data.common.model.ClassScheduleModel
import com.lczarny.lsnplanner.data.common.model.ClassScheduleType
import com.lczarny.lsnplanner.presentation.components.AppIcons
import com.lczarny.lsnplanner.presentation.components.EmptyList
import com.lczarny.lsnplanner.presentation.components.FabListBottomSpacer
import com.lczarny.lsnplanner.presentation.components.InfoChip
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes
import com.lczarny.lsnplanner.presentation.model.mapper.toLabel
import com.lczarny.lsnplanner.presentation.model.mapper.toPlanClassTypeIcon
import com.lczarny.lsnplanner.presentation.ui.home.HomeViewModel
import com.lczarny.lsnplanner.utils.dateFromEpochMilis
import com.lczarny.lsnplanner.utils.formatDuration
import com.lczarny.lsnplanner.utils.formatTime
import com.lczarny.lsnplanner.utils.isBetweenDates
import com.lczarny.lsnplanner.utils.isSameDate
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun ClassesTabList(
    viewModel: HomeViewModel,
    pagerState: PagerState,
    weekStartDate: LocalDate,
    classScheduleWithInfoList: List<Pair<ClassScheduleModel, ClassInfoModel>>
) {
    val classesCurrentDate by viewModel.classesCurrentDate.collectAsState()
    val currentHour = LocalDateTime.now().hour
    val isCurrentDay = pagerState.currentPage + 1 == LocalDate.now().dayOfWeek.value

    HorizontalPager(modifier = Modifier.fillMaxSize(), state = pagerState, beyondViewportPageCount = 0) { page ->
        // TODO load once for entire week, loads every tab change for days in between
        val classScheduleByHourMap = classScheduleWithInfoList
            .filter {
                when (it.first.type) {
                    ClassScheduleType.Weekly -> it.first.weekDay == page + 1
                    ClassScheduleType.Single -> it.first.startDate != null && dateFromEpochMilis(it.first.startDate!!) isSameDate weekStartDate.plusDays(
                        (page + 1).toLong()
                    )

                    ClassScheduleType.Period -> {
                        val startDate = dateFromEpochMilis(it.first.startDate!!)
                        val endDate = dateFromEpochMilis(it.first.endDate!!)
                        it.first.weekDay == page + 1 && classesCurrentDate.isBetweenDates(startDate, endDate)
                    }
                }
            }
            .sortedWith(compareBy<Pair<ClassScheduleModel, ClassInfoModel>> { it.first.startHour }.thenBy { it.first.startMinute })
            .groupBy { it.first.startHour }

        if (classScheduleByHourMap.isEmpty()) {
            EmptyList(stringResource(R.string.class_list_empty_hint), showIcon = false)
            return@HorizontalPager
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(AppPadding.MD_PADDING),
            verticalArrangement = Arrangement.spacedBy(AppPadding.LIST_ITEM_PADDING),
            horizontalAlignment = Alignment.Start
        ) {
            var prevHour = -1

            classScheduleByHourMap.forEach { (hour, schedule) ->
                for (emptyHour in (prevHour + 1)..hour) {
                    item { ClassesTabListHourDivider(emptyHour, isCurrentDay, currentHour) }
                }

                prevHour = hour

                items(items = schedule, key = { it.first.id!! }) { ClassesTabListItem(viewModel, it) }
            }

            for (emptyHour in (prevHour + 1)..<24) {
                item { ClassesTabListHourDivider(emptyHour, isCurrentDay, currentHour) }
            }

            item { FabListBottomSpacer() }
        }
    }
}

@Composable
private fun ClassesTabListHourDivider(hour: Int, isCurrentDay: Boolean, currentHour: Int) {
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            formatTime(context, hour, 0),
            style = MaterialTheme.typography.labelMedium,
            color = if (isCurrentDay && hour == currentHour) MaterialTheme.colorScheme.error else Color.Unspecified
        )
        HorizontalDivider(
            modifier = Modifier.padding(start = AppPadding.SM_PADDING),
            color = if (isCurrentDay && hour == currentHour) MaterialTheme.colorScheme.error else DividerDefaults.color
        )
    }
}

@Composable
private fun ClassesTabListItem(viewModel: HomeViewModel, classTimeWithInfo: Pair<ClassScheduleModel, ClassInfoModel>) {
    val context = LocalContext.current

    val schedule = classTimeWithInfo.first
    val classInfo = classTimeWithInfo.second


    val lessonPlan by viewModel.lessonPlan.collectAsStateWithLifecycle()
    val duration = formatDuration(context, schedule.startHour, schedule.startMinute, schedule.durationMinutes)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = AppSizes.CARD_ELEVATION),
        // TODO grey out if in past
        colors = CardDefaults.cardColors(containerColor = Color(classInfo.color), contentColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(AppPadding.MD_PADDING),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
        ) {
            ClassesTabListItemTitle(classInfo.name, classInfo.type.toLabel(context), classInfo.type.toPlanClassTypeIcon())
            ClassesTabListItemField(duration, AppIcons.TIME)
            ClassesTabListItemField("${schedule.durationMinutes} ${stringResource(R.string.minutes)}", AppIcons.DURATION)
            if (lessonPlan?.addressEnabled == true) {
                ClassesTabListItemField("${stringResource(R.string.class_address)}: ${schedule.address}", AppIcons.LOCATION)
            }
            ClassesTabListItemField("${stringResource(R.string.class_classroom)}: ${schedule.classroom}", AppIcons.CLASSROOM)
        }
    }
}

@Composable
private fun ClassesTabListItemTitle(title: String, type: String, typeImg: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = AppPadding.XSM_PADDING),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.labelLarge)
        InfoChip(label = type, imageVector = typeImg)
    }
}

@Composable
private fun ClassesTabListItemField(value: String, img: ImageVector) {
    Row(modifier = Modifier.padding(top = AppPadding.XSM_PADDING), verticalAlignment = Alignment.CenterVertically) {
        Icon(img, modifier = Modifier.size(AppSizes.SM_ICON), contentDescription = value)
        Text(value, modifier = Modifier.padding(start = AppPadding.SM_PADDING), style = MaterialTheme.typography.labelSmall)
    }
}