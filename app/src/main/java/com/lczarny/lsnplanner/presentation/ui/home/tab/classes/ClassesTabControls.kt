package com.lczarny.lsnplanner.presentation.ui.home.tab.classes

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.components.AppDatePickerDialog
import com.lczarny.lsnplanner.presentation.components.AppIcons
import com.lczarny.lsnplanner.presentation.components.BackIcon
import com.lczarny.lsnplanner.presentation.components.FabMenu
import com.lczarny.lsnplanner.presentation.components.FabMenuItem
import com.lczarny.lsnplanner.presentation.components.NextIcon
import com.lczarny.lsnplanner.presentation.model.mapper.ClassViewType
import com.lczarny.lsnplanner.presentation.navigation.ClassDetailsRoute
import com.lczarny.lsnplanner.presentation.ui.home.HomeViewModel
import com.lczarny.lsnplanner.utils.dateTimeFromEpochMilis
import com.lczarny.lsnplanner.utils.getDayOfWeekNum
import com.lczarny.lsnplanner.utils.getTimestamp
import kotlinx.coroutines.launch

@Composable
fun ClassesTabActions(viewModel: HomeViewModel) {
    Row {
        IconButton(onClick = { viewModel.changeCurrentClassesWeek(false) }) {
            BackIcon(contentDescription = stringResource(R.string.class_prev_week))
        }

        IconButton(onClick = { viewModel.changeCurrentClassesWeek(true) }) {
            NextIcon(contentDescription = stringResource(R.string.class_next_week))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassesTabFab(
    viewModel: HomeViewModel,
    navController: NavController,
    classesPagerState: PagerState
) {
    val lessonPlan by viewModel.lessonPlan.collectAsStateWithLifecycle()
    val classesCurrentDate by viewModel.classesCurrentDate.collectAsStateWithLifecycle()
    val classesDisplayType by viewModel.classesDisplayType.collectAsStateWithLifecycle()

    val animationScope = rememberCoroutineScope()
    var showClassesDateDialog by remember { mutableStateOf(false) }

    if (showClassesDateDialog) {
        AppDatePickerDialog(
            initialValue = classesCurrentDate.getTimestamp(),
            onDismiss = { showClassesDateDialog = false },
            onConfirm = { selectedDateMillis ->
                animationScope.launch {
                    dateTimeFromEpochMilis(selectedDateMillis!!).let {
                        viewModel.changeCurrentClassesDate(it.toLocalDate())
                        classesPagerState.animateScrollToPage(it.getDayOfWeekNum() - 1)
                        showClassesDateDialog = false
                    }
                }
            }
        )
    }

    lessonPlan?.let {
        FabMenu(
            items = listOf(
                FabMenuItem(
                    imageVector = AppIcons.DATE,
                    label = stringResource(R.string.class_select_date),
                    onClick = { showClassesDateDialog = true }
                ),
                FabMenuItem(
                    visible = classesDisplayType != ClassViewType.List,
                    imageVector = AppIcons.LIST,
                    label = stringResource(R.string.home_tab_classes_list_view),
                    onClick = { viewModel.changeClassesViewType(ClassViewType.List) }
                ),
                FabMenuItem(
                    visible = classesDisplayType != ClassViewType.Timeline,
                    imageVector = AppIcons.TIMELINE,
                    label = stringResource(R.string.home_tab_classes_timeline_view),
                    onClick = { viewModel.changeClassesViewType(ClassViewType.Timeline) }
                ),
                FabMenuItem(
                    imageVector = AppIcons.ADD,
                    label = stringResource(R.string.class_new),
                    onClick = { navController.navigate(ClassDetailsRoute(defaultWeekDay = classesPagerState.currentPage + 1)) },
                )
            )
        )
    }
}