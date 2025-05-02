package com.lczarny.lsnplanner.presentation.ui.classlist.components

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
import com.lczarny.lsnplanner.presentation.components.EmptyList
import com.lczarny.lsnplanner.presentation.components.FabListBottomSpacer
import com.lczarny.lsnplanner.presentation.components.SuccessSnackbar
import com.lczarny.lsnplanner.presentation.navigation.ClassDetailsRoute
import com.lczarny.lsnplanner.presentation.ui.classlist.ClassListViewModel
import kotlinx.coroutines.channels.Channel

@Composable
fun ClassList(navController: NavController, viewModel: ClassListViewModel) {
    val lessonPlanName by viewModel.lessonPlanName.collectAsStateWithLifecycle()
    val classes by viewModel.classes.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarChannel = remember { Channel<ClassListScreenSnackbar>(Channel.CONFLATED) }

    ClassListSnackbar(snackbarHostState, snackbarChannel, viewModel)

    Scaffold(
        topBar = { AppNavBar(title = stringResource(R.string.route_classes, lessonPlanName), onNavIconClick = { navController.popBackStack() }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) { SuccessSnackbar(it) } },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(ClassDetailsRoute()) }) {
                AddIcon(contentDescription = stringResource(R.string.class_add))
            }
        },
    ) { padding ->
        if (classes.isEmpty()) {
            EmptyList(stringResource(R.string.class_list_add_empty_hint))
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            state = listState
        ) {
            items(items = classes, key = { it.id!! }) { item -> ClassListItem(navController, viewModel, snackbarChannel, item) }
            item { FabListBottomSpacer() }
        }
    }
}