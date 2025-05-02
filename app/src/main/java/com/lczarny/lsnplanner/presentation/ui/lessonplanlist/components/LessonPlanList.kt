package com.lczarny.lsnplanner.presentation.ui.lessonplanlist.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.components.AddIcon
import com.lczarny.lsnplanner.presentation.components.AppNavBar
import com.lczarny.lsnplanner.presentation.components.FabListBottomSpacer
import com.lczarny.lsnplanner.presentation.components.SuccessSnackbar
import com.lczarny.lsnplanner.presentation.navigation.LessonPlanRoute
import com.lczarny.lsnplanner.presentation.ui.lessonplanlist.LessonPlanListViewModel
import kotlinx.coroutines.channels.Channel

@Composable
fun LessonPlanList(navController: NavController, viewModel: LessonPlanListViewModel) {
    val lessonPlans by viewModel.lessonPlans.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarChannel = remember { Channel<ListPickerScreenSnackbar>(Channel.CONFLATED) }

    LessonPlanListSnackbar(snackbarHostState, snackbarChannel, viewModel)

    Scaffold(
        topBar = { AppNavBar(title = stringResource(R.string.route_lesson_plan_list), onNavIconClick = { navController.popBackStack() }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) { SuccessSnackbar(it) } },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(LessonPlanRoute()) }) {
                AddIcon(contentDescription = stringResource(R.string.plan_add))
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            state = listState
        ) {
            items(items = lessonPlans, key = { it.id!! }) { item -> LessonPlanListItem(navController, viewModel, snackbarChannel, item) }

            item { FabListBottomSpacer() }
        }
    }
}