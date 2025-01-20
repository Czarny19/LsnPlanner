package com.lczarny.lsnplanner.presentation.ui.home.tab

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.lczarny.lsnplanner.utils.mapAppDayOfWeekToCalendarDayOfWeek
import com.lczarny.lsnplanner.utils.toDayOfWeekString
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClassesTab(padding: PaddingValues, viewModel: HomeViewModel, pagerState: PagerState) {
    val classes by viewModel.planClasses.collectAsState()
    val currentDate by viewModel.planClassesCurrentDate.collectAsState()

    val classesPerWeekDay = mutableMapOf<Int, List<PlanClassModel>>()

    for (i in 1..7) {
        classesPerWeekDay[i] = classes.filter { it.weekDay == i }
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

    val classTime = if (item.weekDay != null) {
        val startTime = formatTime(context, item.startHour, item.startMinute)
        val endTime = formatTime(context, item.startHour + (item.durationMinutes / 60), item.startMinute + (item.durationMinutes % 60))
        "$startTime - $endTime (${item.durationMinutes} ${stringResource(R.string.minutes)})"
    } else {
        "aa"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = AppSizes.cardElevation),
        content = {
            Column(
                modifier = Modifier.padding(AppPadding.screenPadding),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
            ) {
                Text(item.name, modifier = Modifier.padding(bottom = AppPadding.smPadding), style = MaterialTheme.typography.titleSmall)
                Text(classTime, style = MaterialTheme.typography.labelLarge)
                Text(item.classroom ?: "")
            }
        }
    )
}