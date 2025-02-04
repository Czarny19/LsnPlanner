package com.lczarny.lsnplanner.presentation.ui.home.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Room
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.local.model.PlanClassModel
import com.lczarny.lsnplanner.presentation.components.EmptyList
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes
import com.lczarny.lsnplanner.presentation.ui.home.HomeViewModel
import com.lczarny.lsnplanner.utils.formatTime
import com.lczarny.lsnplanner.utils.isSameDate
import com.lczarny.lsnplanner.utils.mapAppDayOfWeekToCalendarDayOfWeek
import com.lczarny.lsnplanner.utils.toDayOfWeekString
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun ClassesTab(padding: PaddingValues, viewModel: HomeViewModel, pagerState: PagerState) {
    val classes by viewModel.planClasses.collectAsState()
    val currentDate by viewModel.planClassesCurrentDate.collectAsState()

    val classesPerWeekDay = mutableMapOf<Int, List<PlanClassModel>>()

    for (i in 1..7) {
        val currentDayDate = Calendar.getInstance().apply {
            timeInMillis = currentDate.timeInMillis
            set(Calendar.DAY_OF_WEEK, i.mapAppDayOfWeekToCalendarDayOfWeek())
        }

        classesPerWeekDay[i] = classes.filter {
            it.weekDay == i || (it.startDate != null && currentDayDate.isSameDate(
                Calendar.getInstance().apply { timeInMillis = it.startDate!! }
            ))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppPadding.mdPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (i in 1..7) {
                    Calendar.getInstance().apply {
                        timeInMillis = currentDate.timeInMillis
                        set(Calendar.DAY_OF_WEEK, i.mapAppDayOfWeekToCalendarDayOfWeek())
                    }.let {
                        ClassesWeekDayTopNavItem(i, viewModel, it, pagerState)
                    }
                }
            }
        }

        HorizontalPager(modifier = Modifier.fillMaxSize(), state = pagerState, beyondViewportPageCount = 6) { page ->
            if (classesPerWeekDay[page + 1]!!.isEmpty()) {
                EmptyList(stringResource(R.string.class_list_empty_hint), showIcon = false)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(AppPadding.screenPadding),
                    verticalArrangement = Arrangement.spacedBy(AppPadding.listItemPadding),
                    horizontalAlignment = Alignment.Start
                ) {
                    items(items = classesPerWeekDay[page + 1]!!) { planClass ->
                        ClassesTabItem(planClass)
                    }
                }
            }
        }
    }
}

@Composable
fun ClassesWeekDayTopNavItem(index: Int, viewModel: HomeViewModel, date: Calendar, pagerState: PagerState) {
    val contex = LocalContext.current
    val animationScope = rememberCoroutineScope()

    val isActive = pagerState.currentPage + 1 == index

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(40.dp)
            .background(
                color = if (isActive) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary,
                shape = CircleShape
            )
    ) {
        Column(
            modifier = Modifier.clickable {
                animationScope.launch {
                    pagerState.animateScrollToPage(index - 1)
                    viewModel.changeCurrentClassesDate(date)
                }
            },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                date.get(Calendar.DAY_OF_MONTH).toString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiary
            )
            Text(
                index.toDayOfWeekString(contex).substring(0, 2),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}

@Composable
fun ClassesTabItem(item: PlanClassModel) {
    val context = LocalContext.current

    val startTime: String
    val classTime: String

    if (item.weekDay != null) {
        val hour = item.startHour
        val minute = item.startMinute

        startTime = formatTime(context, hour, minute)
        val endTime = formatTime(context, hour + (item.durationMinutes / 60), minute + (item.durationMinutes % 60))

        classTime = "$startTime - $endTime"
    } else {
        val startDate = Calendar.getInstance().apply { timeInMillis = item.startDate!! }
        val hour = startDate.get(Calendar.HOUR_OF_DAY)
        val minute = startDate.get(Calendar.MINUTE)

        startTime = formatTime(context, hour, minute)
        val endTime = formatTime(context, hour + (item.durationMinutes / 60), minute + (item.durationMinutes % 60))

        classTime = "$startTime - $endTime"
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = AppPadding.smPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(startTime, style = MaterialTheme.typography.labelMedium)
            HorizontalDivider(modifier = Modifier.padding(start = AppPadding.smPadding))
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = AppPadding.timelineStartPadding),
            elevation = CardDefaults.cardElevation(defaultElevation = AppSizes.cardElevation),
            colors = CardDefaults.cardColors(containerColor = Color(item.color)),
            content = {
                Column(
                    modifier = Modifier.padding(AppPadding.screenPadding),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top,
                ) {
                    Text(
                        item.name,
                        modifier = Modifier.padding(bottom = AppPadding.xsmPadding),
                        style = MaterialTheme.typography.titleSmall
                    )
                    ClassesTabItemField(classTime, Icons.Filled.Schedule)
                    ClassesTabItemField("${item.durationMinutes} ${stringResource(R.string.minutes)}", Icons.Filled.Timer)
                    if (item.classroom != null) {
                        ClassesTabItemField("${stringResource(R.string.class_classroom)}: ${item.classroom}", Icons.Filled.Room)
                    }
                }
            }
        )
    }
}

@Composable
fun ClassesTabItemField(value: String, img: ImageVector) {
    Row(modifier = Modifier.padding(top = AppPadding.xsmPadding), verticalAlignment = Alignment.CenterVertically) {
        Icon(modifier = Modifier.size(AppSizes.smIcon), imageVector = img, contentDescription = value)
        Text(value, modifier = Modifier.padding(start = AppPadding.xsmPadding), style = MaterialTheme.typography.labelLarge)
    }
}