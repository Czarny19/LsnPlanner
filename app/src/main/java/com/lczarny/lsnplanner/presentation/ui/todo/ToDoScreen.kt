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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.local.model.ToDoImportance
import com.lczarny.lsnplanner.data.local.model.ToDoModel
import com.lczarny.lsnplanner.presentation.components.AppBarBackIconButton
import com.lczarny.lsnplanner.presentation.components.AppNavBar
import com.lczarny.lsnplanner.presentation.components.DropDownItem
import com.lczarny.lsnplanner.presentation.components.FullScreenLoading
import com.lczarny.lsnplanner.presentation.components.FullScreenTextArea
import com.lczarny.lsnplanner.presentation.components.InfoField
import com.lczarny.lsnplanner.presentation.components.OutlinedDateTimePicker
import com.lczarny.lsnplanner.presentation.components.OutlinedDropDown
import com.lczarny.lsnplanner.presentation.components.SavingDialog
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.theme.AppTheme
import com.lczarny.lsnplanner.presentation.ui.todo.model.ToDoState
import com.lczarny.lsnplanner.presentation.ui.todo.model.toDoImportanceLabelMap

@Composable
fun ToDoScreen(
    navController: NavController,
    lessonPlanId: Long,
    classId: Long? = null,
    toDoId: Long?,
    viewModel: ToDoViewModel = hiltViewModel()
) {
    viewModel.intializeToDo(lessonPlanId, classId, toDoId)

    val screenState by viewModel.screenState.collectAsState()

    AppTheme(
        content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
                content = {
                    when (screenState) {
                        ToDoState.Loading -> FullScreenLoading(label = stringResource(R.string.please_wait))
                        ToDoState.Edit -> ToDoForm(navController, false, viewModel)
                        ToDoState.Saving -> ToDoForm(navController, true, viewModel)
                        ToDoState.Finished -> navController.popBackStack()
                    }
                }
            )
        }
    )
}

@Composable
fun ToDoForm(navController: NavController, saving: Boolean, viewModel: ToDoViewModel) {
    val toDoData by viewModel.toDoData.collectAsState()

    var detailsExpanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (detailsExpanded) 180f else 0f,
        label = stringResource(R.string.card_animation)
    )

    toDoData?.let { data ->
        Scaffold(
            topBar = {
                AppNavBar(
                    title = stringResource(if (data.id != null) R.string.route_edit_todo else R.string.route_new_todo),
                    navIcon = { AppBarBackIconButton(navController) },
                    actions = {
                        IconButton(
                            modifier = Modifier.rotate(rotationState),
                            onClick = { detailsExpanded = detailsExpanded.not() },
                            content = {
                                Icon(
                                    imageVector = Icons.Outlined.ArrowDropDownCircle,
                                    contentDescription = stringResource(R.string.drop_down_arrow),
                                )
                            }
                        )
                        IconButton(
                            onClick = { viewModel.saveToDo() },
                            enabled = data.content.isEmpty().not(),
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
                ToDoDetails(viewModel, data, detailsExpanded)
                FullScreenTextArea(
                    placeholder = stringResource(R.string.write_here),
                    value = data.content,
                    onValueChange = { name -> viewModel.updateContent(name) },
                )
            }
        }
    }
}

@Composable
fun ToDoDetails(viewModel: ToDoViewModel, toDoData: ToDoModel, visible: Boolean) {
    if (visible.not()) {
        return
    }

    val toDoImportanceLabelMap = toDoImportanceLabelMap(LocalContext.current)

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
                modifier = Modifier.padding(bottom = AppPadding.inputBottomPadding),
                text = stringResource(R.string.todo_details_info)
            )
            OutlinedDateTimePicker(
                initialValue = toDoData.dueDate,
                label = stringResource(R.string.todo_due_date),
                onDateTimeSelected = { dateMilis -> viewModel.updateDueDate(dateMilis) }
            )
            OutlinedDropDown(
                modifier = Modifier.padding(bottom = AppPadding.smPadding),
                label = stringResource(R.string.todo_importance),
                value = DropDownItem(toDoData.importance, toDoImportanceLabelMap.getValue(toDoData.importance)),
                onValueChange = { importance -> viewModel.updateImportance(importance.value as ToDoImportance) },
                items = ToDoImportance.entries.map { DropDownItem(it, toDoImportanceLabelMap.getValue(it)) }
            )
        }
    }
}