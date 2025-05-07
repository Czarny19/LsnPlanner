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
import com.lczarny.lsnplanner.data.common.model.ClassScheduleWithInfoModel
import com.lczarny.lsnplanner.presentation.components.AppIcons
import com.lczarny.lsnplanner.presentation.components.EmptyList
import com.lczarny.lsnplanner.presentation.components.FabListBottomSpacer
import com.lczarny.lsnplanner.presentation.components.InfoChip
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes
import com.lczarny.lsnplanner.presentation.model.mapper.toLabel
import com.lczarny.lsnplanner.presentation.model.mapper.toPlanClassTypeIcon
import com.lczarny.lsnplanner.presentation.ui.home.HomeViewModel
import com.lczarny.lsnplanner.utils.formatDuration
import com.lczarny.lsnplanner.utils.formatTime
import kotlinx.datetime.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun ClassesTabList(viewModel: HomeViewModel, pagerState: PagerState) {
    val currentHour = LocalDateTime.now().hour
    val isCurrentDay = pagerState.currentPage + 1 == LocalDate.now().dayOfWeek.value

    val classesSchedulesPerDay by viewModel.classesSchedulesPerDay.collectAsStateWithLifecycle()

    HorizontalPager(modifier = Modifier.fillMaxSize(), state = pagerState, beyondViewportPageCount = 6) { page ->
        val todaysClassesPerHour = classesSchedulesPerDay[DayOfWeek(page + 1)]!!
            .sortedWith(compareBy<ClassScheduleWithInfoModel> { it.schedule.startHour }.thenBy { it.schedule.startMinute })
            .groupBy { it.schedule.startHour }

        if (todaysClassesPerHour.isEmpty()) {
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

            todaysClassesPerHour.forEach { (hour, scheduleWithInfoList) ->
                for (emptyHour in (prevHour + 1)..hour) {
                    item { ClassesTabListHourDivider(emptyHour, isCurrentDay, currentHour) }
                }

                prevHour = hour

                items(items = scheduleWithInfoList, key = { it.schedule.id!! }) { ClassesTabListItem(viewModel, it) }
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
private fun ClassesTabListItem(viewModel: HomeViewModel, scheduleWithInfo: ClassScheduleWithInfoModel) {
    val context = LocalContext.current

    val schedule = scheduleWithInfo.schedule
    val classInfo = scheduleWithInfo.info


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