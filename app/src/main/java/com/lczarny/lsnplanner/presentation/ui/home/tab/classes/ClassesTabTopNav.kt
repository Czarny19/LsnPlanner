package com.lczarny.lsnplanner.presentation.ui.home.tab.classes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.ui.home.HomeViewModel
import com.lczarny.lsnplanner.utils.toDayOfWeekString
import com.lczarny.lsnplanner.utils.weekStartDate
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun ClassesTabTopNav(viewModel: HomeViewModel, pagerState: PagerState) {
    val classesCurrentDate by viewModel.classesCurrentDate.collectAsState()
    val weekStartDate = classesCurrentDate.weekStartDate()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppPadding.MD_PADDING),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 1..7) {
                ClassesTabTopNavItem(i, viewModel, weekStartDate.plusDays(i.toLong() - 1), pagerState)
            }
        }
    }
}

@Composable
fun ClassesTabTopNavItem(index: Int, viewModel: HomeViewModel, date: LocalDate, pagerState: PagerState) {
    val contex = LocalContext.current
    val animationScope = rememberCoroutineScope()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(40.dp)
            .background(
                color = if (pagerState.currentPage == index - 1) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary,
                shape = CircleShape
            )
            // todo poprawic efekt click
            .clickable {
                animationScope.launch {
                    pagerState.animateScrollToPage(index - 1)
                    viewModel.changeCurrentClassesDate(date)
                }
            },
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                date.dayOfMonth.toString(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiary
            )

            Text(
                index.toDayOfWeekString(contex).substring(0, 2),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}