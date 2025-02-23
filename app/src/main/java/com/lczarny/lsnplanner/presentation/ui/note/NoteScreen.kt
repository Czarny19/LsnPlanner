package com.lczarny.lsnplanner.presentation.ui.note

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.local.model.NoteImportance
import com.lczarny.lsnplanner.data.local.model.NoteModel
import com.lczarny.lsnplanner.presentation.components.AppBarBackIconButton
import com.lczarny.lsnplanner.presentation.components.AppNavBar
import com.lczarny.lsnplanner.presentation.components.DiscardChangesDialog
import com.lczarny.lsnplanner.presentation.components.DropDownItem
import com.lczarny.lsnplanner.presentation.components.FullScreenLoading
import com.lczarny.lsnplanner.presentation.components.FullScreenTextArea
import com.lczarny.lsnplanner.presentation.components.InfoField
import com.lczarny.lsnplanner.presentation.components.OutlinedDropDown
import com.lczarny.lsnplanner.presentation.components.PredefinedDialogState
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.model.DetailsScreenState
import com.lczarny.lsnplanner.presentation.model.mapper.getLabel
import com.lczarny.lsnplanner.presentation.theme.AppTheme

@Composable
fun NoteScreen(
    navController: NavController,
    lessonPlanId: Long,
    noteId: Long?,
    viewModel: NoteViewModel = hiltViewModel(),
) {
    viewModel.intializeNote(lessonPlanId, noteId)

    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            content = {
                val screenState by viewModel.screenState.collectAsStateWithLifecycle()

                when (screenState) {
                    DetailsScreenState.Loading -> FullScreenLoading(label = stringResource(R.string.please_wait))
                    DetailsScreenState.Edit -> NoteForm(navController, viewModel)
                    DetailsScreenState.Saving -> FullScreenLoading(stringResource(R.string.saving))
                    DetailsScreenState.Finished -> navController.popBackStack()
                }
            }
        )
    }
}

@Composable
fun NoteForm(navController: NavController, viewModel: NoteViewModel) {
    val note by viewModel.note.collectAsStateWithLifecycle()
    val saveEnabled by viewModel.saveEnabled.collectAsStateWithLifecycle()

    var detailsExpanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (detailsExpanded) 180f else 0f,
        label = stringResource(R.string.card_animation)
    )

    var discardChangesDialogOpen by remember { mutableStateOf(false) }

    DiscardChangesDialog(
        discardChangesDialogOpen,
        PredefinedDialogState(
            onConfirm = {
                discardChangesDialogOpen = false
                navController.popBackStack()
            },
            onDismiss = { discardChangesDialogOpen = false }
        )
    )

    note?.let { data ->
        Scaffold(
            topBar = {
                AppNavBar(
                    title = stringResource(if (data.id != null) R.string.route_edit_note else R.string.route_new_note),
                    navIcon = {
                        AppBarBackIconButton(onClick = {
                            if (viewModel.dataChanged()) {
                                discardChangesDialogOpen = true
                            } else {
                                navController.popBackStack()
                            }
                        })
                    },
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
                            onClick = { viewModel.saveNote() },
                            enabled = saveEnabled,
                            content = {
                                Icon(
                                    imageVector = Icons.Outlined.Check,
                                    contentDescription = stringResource(R.string.note_save),
                                )
                            }
                        )
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                if (detailsExpanded) NoteInfoCard(viewModel, data)
                FullScreenTextArea(
                    placeholder = stringResource(R.string.write_here),
                    initialValue = data.content,
                    onValueChange = { name -> viewModel.updateContent(name) },
                )
            }
        }
    }
}

@Composable
fun NoteInfoCard(viewModel: NoteViewModel, note: NoteModel) {
    val context = LocalContext.current

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
                .padding(AppPadding.SCREEN_PADDING)
        ) {
            InfoField(
                modifier = Modifier.padding(bottom = AppPadding.INPUT_BOTTOM_PADDING),
                text = stringResource(R.string.note_details_info)
            )
            OutlinedDropDown(
                modifier = Modifier.padding(top = AppPadding.SM_PADDING),
                label = stringResource(R.string.note_importance),
                value = DropDownItem(note.importance, note.importance.getLabel(context)),
                onValueChange = { importance -> viewModel.updateImportance(importance.value as NoteImportance) },
                items = NoteImportance.entries.map { DropDownItem(it, it.getLabel(context)) }
            )
        }
    }
}