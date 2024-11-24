package com.lczarny.lsnplanner.presentation.ui.todo

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDownCircle
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.components.AppBarBackIconButton
import com.lczarny.lsnplanner.presentation.components.AppNavBar
import com.lczarny.lsnplanner.presentation.components.FullScreenLoading
import com.lczarny.lsnplanner.presentation.components.FullScreenTextArea
import com.lczarny.lsnplanner.presentation.components.InfoField
import com.lczarny.lsnplanner.presentation.components.OutlinedDateTimePicker
import com.lczarny.lsnplanner.presentation.components.SavingDialog
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.theme.AppTheme
import com.lczarny.lsnplanner.presentation.ui.home.HomeViewModel
import com.lczarny.lsnplanner.presentation.ui.todo.model.ToDoState

@Composable
fun ToDoScreen(
    navController: NavController,
    lessonPlanId: Long,
    toDoId: Long?,
    viewModel: ToDoViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val toDosList by homeViewModel.todos.collectAsState()
    viewModel.intializeToDo(toDosList.find { toDo -> toDo.id == toDoId })

    val screenState by viewModel.screenState.collectAsState()

    AppTheme(
        content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
                content = {
                    when (screenState) {
                        ToDoState.Loading -> FullScreenLoading(label = stringResource(R.string.please_wait))
                        ToDoState.Edit, ToDoState.Saving -> ToDoForm(
                            navController,
                            lessonPlanId,
                            screenState == ToDoState.Saving,
                            viewModel
                        )

                        ToDoState.Finished -> navController.popBackStack()
                    }
                }
            )
        }
    )
}

@Composable
fun ToDoForm(navController: NavController, lessonPlanId: Long, saving: Boolean, viewModel: ToDoViewModel) {
    val isEdit by remember { viewModel.isEdit }
    var content by remember { viewModel.content }

    var detailsExpanded by remember { mutableStateOf(false) }

    val rotationState by animateFloatAsState(
        targetValue = if (detailsExpanded) 180f else 0f, label = stringResource(R.string.card_animation)
    )

    Scaffold(
        topBar = {
            AppNavBar(
                title = stringResource(if (isEdit) R.string.route_edit_todo else R.string.route_new_todo),
                navIcon = { AppBarBackIconButton(navController) },
                actions = {
                    IconButton(
                        modifier = Modifier.rotate(rotationState),
                        onClick = { detailsExpanded = !detailsExpanded },
                        content = {
                            Icon(
                                imageVector = Icons.Outlined.ArrowDropDownCircle,
                                contentDescription = stringResource(R.string.drop_down_arrow),
                            )
                        }
                    )
                    IconButton(
                        onClick = { viewModel.saveToDo(lessonPlanId) },
                        content = {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = stringResource(R.string.todo_save),
                            )
                        }
                    )
                }
            )
        }
    ) { padding ->
        SavingDialog(saving)
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            ToDoDetails(viewModel, detailsExpanded)
            FullScreenTextArea(
                placeholder = stringResource(R.string.write_here),
                value = content,
                onValueChange = { name -> viewModel.updateContent(name) },
            )
        }
    }
}

@Composable
fun ToDoDetails(viewModel: ToDoViewModel, visible: Boolean) {
    var dueDate by remember { viewModel.dueDate }

    if (visible) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            shape = CutCornerShape(0),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppPadding.screenPadding)
            ) {
                InfoField(
                    modifier = Modifier.padding(bottom = AppPadding.mdInputSpacerPadding),
                    text = stringResource(R.string.todo_due_date_info)
                )
                OutlinedDateTimePicker(
                    initialValue = dueDate,
                    label = stringResource(R.string.todo_due_date),
                    onDateTimeSelected = { dateMilis -> viewModel.updateDueDate(dateMilis) }
                )
            }
        }
    }
}