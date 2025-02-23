package com.lczarny.lsnplanner.presentation.ui.classlist

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
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
import com.lczarny.lsnplanner.data.local.model.ClassInfoModel
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
import com.lczarny.lsnplanner.presentation.model.ListScreenState
import com.lczarny.lsnplanner.presentation.navigation.ClassDetailsRoute
import com.lczarny.lsnplanner.presentation.theme.AppTheme
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

enum class ClassListScreenSnackbar {
    Deleted
}

@Composable
fun ClassListScreen(navController: NavController, lessonPlanId: Long, viewModel: ClassListViewModel = hiltViewModel()) {
    viewModel.initializeLessonPlanAndClasses(lessonPlanId)

    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            content = {
                val screenState by viewModel.screenState.collectAsStateWithLifecycle()

                when (screenState) {
                    ListScreenState.Loading -> FullScreenLoading(label = stringResource(R.string.please_wait))
                    ListScreenState.List -> LessonPlanList(navController, viewModel)
                }
            }
        )
    }
}

@Composable
fun LessonPlanList(navController: NavController, viewModel: ClassListViewModel) {
    val context = LocalContext.current

    val lessonPlan by viewModel.lessonPlan.collectAsStateWithLifecycle()
    val classes by viewModel.classes.collectAsStateWithLifecycle()
    val selectedClassName = remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarChannel = remember { Channel<ClassListScreenSnackbar>(Channel.CONFLATED) }

    LaunchedEffect(snackbarChannel) {
        snackbarChannel.receiveAsFlow().collect {
            when (it) {
                ClassListScreenSnackbar.Deleted -> snackbarHostState.showSnackbar(
                    message = context.getString(R.string.snackbar_class_deleted, selectedClassName.value),
                    withDismissAction = true
                )
            }

            selectedClassName.value = ""
        }
    }

    lessonPlan?.let { lessonPlanData ->
        Scaffold(
            topBar = {
                AppNavBar(
                    title = stringResource(R.string.route_classes, lessonPlanData.name),
                    navIcon = { AppBarBackIconButton(onClick = { navController.popBackStack() }) },
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) { SuccessSnackbar(it) } },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(ClassDetailsRoute(lessonPlanId = lessonPlanData.id!!, lessonPlanType = lessonPlanData.type)) },
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
                items(items = classes) { item -> ClassListItem(viewModel, navController, snackbarChannel, item, lessonPlanData, selectedClassName) }
                item { FabListBottomSpacer() }
            }
        }
    }
}

@Composable
fun ClassListItem(
    viewModel: ClassListViewModel,
    navController: NavController,
    snackbarChannel: Channel<ClassListScreenSnackbar>,
    classInfo: ClassInfoModel,
    lessonPlan: LessonPlanModel,
    selectedClassName: MutableState<String>,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(
                    ClassDetailsRoute(
                        lessonPlanId = lessonPlan.id!!,
                        lessonPlanType = lessonPlan.type,
                        classInfoId = classInfo.id!!
                    )
                )
            }
            .padding(vertical = AppPadding.XSM_PADDING, horizontal = AppPadding.MD_PADDING),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = Icons.Filled.Circle,
            contentDescription = stringResource(R.string.class_color),
            tint = Color(classInfo.color.raw)
        )
        ListItemTitle(modifier = Modifier.padding(horizontal = AppPadding.MD_PADDING), text = classInfo.name)
        Spacer(modifier = Modifier.weight(1.0f))
        ClassListItemMenu(viewModel, snackbarChannel, classInfo, selectedClassName)
    }
    HorizontalDivider()
}

@Composable
fun ClassListItemMenu(
    viewModel: ClassListViewModel,
    snackbarChannel: Channel<ClassListScreenSnackbar>,
    classInfo: ClassInfoModel,
    selectedClassName: MutableState<String>,
) {
    var dropDownExpanded by remember { mutableStateOf(false) }
    var deleteConfirmationDialogOpen by remember { mutableStateOf(false) }

    DeleteItemDialog(
        deleteConfirmationDialogOpen,
        BasicDialogState(
            title = stringResource(R.string.class_delete),
            text = stringResource(R.string.class_delete_question),
            onConfirm = {
                deleteConfirmationDialogOpen = false
                viewModel.deleteClass(classInfo.id!!) {
                    selectedClassName.value = classInfo.name
                    snackbarChannel.trySend(ClassListScreenSnackbar.Deleted)
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
            DropdownMenuItem(
                text = { Text(stringResource(R.string.class_delete)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.class_delete)
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