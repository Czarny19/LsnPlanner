package com.lczarny.lsnplanner.presentation.ui.classdetails.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.components.AddIcon
import com.lczarny.lsnplanner.presentation.components.AppNavBar
import com.lczarny.lsnplanner.presentation.components.DiscardChangesDialog
import com.lczarny.lsnplanner.presentation.components.SaveIcon
import com.lczarny.lsnplanner.model.TabBarItem
import com.lczarny.lsnplanner.presentation.ui.classdetails.ClassDetailsViewModel
import com.lczarny.lsnplanner.presentation.ui.classdetails.tab.ClassHomeworkTab
import com.lczarny.lsnplanner.presentation.ui.classdetails.tab.ClassInfoTab
import com.lczarny.lsnplanner.presentation.ui.classdetails.tab.ClassSchedulesTab
import com.lczarny.lsnplanner.presentation.ui.classdetails.tab.ClassTestsTab
import kotlinx.coroutines.launch

@Composable
fun ClassDetailsPager(navController: NavController, viewModel: ClassDetailsViewModel, newClass: Boolean) {
    val classInfo by viewModel.info.collectAsStateWithLifecycle()
    val dataChanged by viewModel.dataChanged.collectAsStateWithLifecycle()
    val saveEnabled by viewModel.saveEnabled.collectAsStateWithLifecycle()

    val classInfoPagerState = rememberPagerState(initialPage = 0) { if (newClass) 2 else 4 }
    val animationScope = rememberCoroutineScope()

    val infoTab = TabBarItem(id = "Info", label = stringResource(R.string.class_basic_info))
    val scheduleTab = TabBarItem(id = "Schedule", label = stringResource(R.string.class_schedule_info))
    val testsTab = TabBarItem(id = "Exams", label = stringResource(R.string.class_exams_info))
    val homeworkTab = TabBarItem(id = "Homework", label = stringResource(R.string.class_homework_info))

    val topBarItems by lazy { if (newClass) listOf(infoTab, scheduleTab) else listOf(infoTab, scheduleTab, testsTab, homeworkTab) }

    var discardChangesDialogOpen by remember { mutableStateOf(false) }

    DiscardChangesDialog(
        visible = discardChangesDialogOpen,
        onConfirm = {
            discardChangesDialogOpen = false
            navController.popBackStack()
        },
        onDismiss = { discardChangesDialogOpen = false }
    )

    BackHandler(dataChanged) { discardChangesDialogOpen = true }

    classInfo?.let { classInfo ->
        Scaffold(
            topBar = {
                Column {
                    AppNavBar(
                        title = if (newClass) stringResource(R.string.route_new_plan_class) else classInfo.name,
                        onNavIconClick = {
                            if (dataChanged) {
                                discardChangesDialogOpen = true
                            } else {
                                navController.popBackStack()
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = { viewModel.saveClass() },
                                enabled = saveEnabled,
                            ) { SaveIcon() }
                        }
                    )

                    classInfoPagerState.currentPage.let { page ->
                        TabRow(selectedTabIndex = page) {
                            topBarItems.forEachIndexed { index, tabBarItem ->
                                Tab(
                                    text = { Text(tabBarItem.label, style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Center) },
                                    selected = index == page,
                                    onClick = { animationScope.launch { classInfoPagerState.animateScrollToPage(index) } }
                                )
                            }
                        }
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                if (classInfoPagerState.currentPage == 1) {
                    FloatingActionButton(
                        onClick = { viewModel.addClassSchedule() },
                        content = { AddIcon(contentDescription = stringResource(R.string.class_time_add)) },
                    )
                }
            },
        ) { padding ->
            HorizontalPager(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                state = classInfoPagerState,
                beyondViewportPageCount = if (newClass) 2 else 4,
                verticalAlignment = Alignment.Top
            ) { page ->
                when (page) {
                    0 -> ClassInfoTab(viewModel, newClass)
                    1 -> ClassSchedulesTab(viewModel)
                    2 -> ClassTestsTab(viewModel)
                    3 -> ClassHomeworkTab(viewModel)
                }
            }
        }
    }
}