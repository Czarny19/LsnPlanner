package com.lczarny.lsnplanner.presentation.ui.lessonplanlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.local.model.LessonPlanModel
import com.lczarny.lsnplanner.presentation.components.AppBarBackIconButton
import com.lczarny.lsnplanner.presentation.components.AppNavBar
import com.lczarny.lsnplanner.presentation.components.BasicDialogState
import com.lczarny.lsnplanner.presentation.components.DeleteItemDialog
import com.lczarny.lsnplanner.presentation.components.FabListBottomSpacer
import com.lczarny.lsnplanner.presentation.components.FullScreenLoading
import com.lczarny.lsnplanner.presentation.components.ListItemTitle
import com.lczarny.lsnplanner.presentation.components.SuccessSnackbar
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.navigation.LessonPlanRoute
import com.lczarny.lsnplanner.presentation.theme.AppTheme
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

enum class ListPickerScreenSnackbar {
    SetActive,
    Deleted
}

@Composable
fun ListPickerScreen(navController: NavController, viewModel: LessonPlanListViewModel = hiltViewModel()) {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            content = {
                val screenState by viewModel.screenState.collectAsStateWithLifecycle()

                when (screenState) {
                    LessonPlanListScreenState.Loading -> FullScreenLoading(label = stringResource(R.string.please_wait))
                    LessonPlanListScreenState.List -> LessonPlanList(navController, viewModel)
                    LessonPlanListScreenState.Finished -> navController.popBackStack()
                }
            }
        )
    }
}

@Composable
fun LessonPlanList(navController: NavController, viewModel: LessonPlanListViewModel) {
    val context = LocalContext.current

    val lessonPlans by viewModel.lessonPlans.collectAsStateWithLifecycle()
    val selectedPlanName = remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarChannel = remember { Channel<ListPickerScreenSnackbar>(Channel.CONFLATED) }

    LaunchedEffect(snackbarChannel) {
        snackbarChannel.receiveAsFlow().collect {
            when (it) {
                ListPickerScreenSnackbar.SetActive -> snackbarHostState.showSnackbar(
                    message = context.getString(R.string.snackbar_plan_set_active, selectedPlanName.value),
                    withDismissAction = true
                )

                ListPickerScreenSnackbar.Deleted -> snackbarHostState.showSnackbar(
                    message = context.getString(R.string.snackbar_plan_deleted, selectedPlanName.value),
                    withDismissAction = true
                )
            }

            selectedPlanName.value = ""
        }
    }

    Scaffold(
        topBar = {
            AppNavBar(
                title = stringResource(R.string.route_lesson_plan_list),
                navIcon = { AppBarBackIconButton(onClick = { navController.popBackStack() }) },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) { SuccessSnackbar(it) } },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(LessonPlanRoute()) },
                content = { Icon(Icons.Filled.Add, stringResource(R.string.plan_add)) },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            state = listState
        ) {
            items(items = lessonPlans) { item -> LessonPlanListItem(viewModel, navController, snackbarChannel, item, selectedPlanName) }
            item { FabListBottomSpacer() }
        }
    }
}

@Composable
fun LessonPlanListItem(
    viewModel: LessonPlanListViewModel,
    navController: NavController,
    snackbarChannel: Channel<ListPickerScreenSnackbar>,
    lessonPlan: LessonPlanModel,
    selectedPlanName: MutableState<String>,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppPadding.XSM_PADDING, horizontal = AppPadding.MD_PADDING),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = stringResource(R.string.plan_active),
            tint = if (lessonPlan.isActive) MaterialTheme.colorScheme.tertiary else Color.Transparent
        )
        ListItemTitle(modifier = Modifier.padding(horizontal = AppPadding.MD_PADDING), text = lessonPlan.name)
        Spacer(modifier = Modifier.weight(1.0f))
        LessonPlanListItemMenu(viewModel, navController, snackbarChannel, lessonPlan, selectedPlanName)
    }
    HorizontalDivider()
}

@Composable
fun LessonPlanListItemMenu(
    viewModel: LessonPlanListViewModel,
    navController: NavController,
    snackbarChannel: Channel<ListPickerScreenSnackbar>,
    lessonPlan: LessonPlanModel,
    selectedPlanName: MutableState<String>,
) {
    var dropDownExpanded by remember { mutableStateOf(false) }
    var deleteConfirmationDialogOpen by remember { mutableStateOf(false) }

    DeleteItemDialog(
        deleteConfirmationDialogOpen,
        BasicDialogState(
            title = stringResource(R.string.plan_delete),
            text = stringResource(R.string.plan_delete_question),
            onConfirm = {
                deleteConfirmationDialogOpen = false
                viewModel.deletePlan(lessonPlan.id!!) {
                    selectedPlanName.value = lessonPlan.name
                    snackbarChannel.trySend(ListPickerScreenSnackbar.Deleted)
                }
            },
            onDismiss = { deleteConfirmationDialogOpen = false }
        )
    )

    IconButton(onClick = { dropDownExpanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.options),
        )
        DropdownMenu(expanded = dropDownExpanded, onDismissRequest = { dropDownExpanded = false }) {
            if (lessonPlan.isActive.not()) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.plan_make_active)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = stringResource(R.string.plan_make_active)
                        )
                    },
                    onClick = {
                        dropDownExpanded = false
                        viewModel.makePlanActive(lessonPlan) {
                            selectedPlanName.value = lessonPlan.name
                            snackbarChannel.trySend(ListPickerScreenSnackbar.SetActive)
                        }
                    }
                )
                HorizontalDivider()
            }

            DropdownMenuItem(
                text = { Text(stringResource(R.string.plan_edit)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = stringResource(R.string.plan_edit)
                    )
                },
                onClick = {
                    dropDownExpanded = false
                    navController.navigate(LessonPlanRoute(lessonPlanId = lessonPlan.id!!))
                }
            )

            if (lessonPlan.isActive.not()) {
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.plan_delete)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = stringResource(R.string.plan_delete)
                        )
                    },
                    onClick = {
                        dropDownExpanded = false
                        deleteConfirmationDialogOpen = true
                    }
                )
            }
        }
    }
}