package com.lczarny.lsnplanner.presentation.ui.note.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.common.model.Importance
import com.lczarny.lsnplanner.data.common.model.NoteModel
import com.lczarny.lsnplanner.presentation.components.AppNavBar
import com.lczarny.lsnplanner.presentation.components.DiscardChangesDialog
import com.lczarny.lsnplanner.presentation.components.DropDownIcon
import com.lczarny.lsnplanner.presentation.components.DropDownItem
import com.lczarny.lsnplanner.presentation.components.FullScreenTextArea
import com.lczarny.lsnplanner.presentation.components.OutlinedDropDown
import com.lczarny.lsnplanner.presentation.components.OutlinedInputField
import com.lczarny.lsnplanner.presentation.components.SaveIcon
import com.lczarny.lsnplanner.presentation.components.TutorialCard
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.model.mapper.getLabel
import com.lczarny.lsnplanner.presentation.ui.note.NoteViewModel
import com.lczarny.lsnplanner.utils.navigateBackWithDataCheck

@Composable
fun NoteEdit(navController: NavController, viewModel: NoteViewModel, isNew: Boolean) {
    val note by viewModel.note.collectAsStateWithLifecycle()

    val dataChanged by viewModel.dataChanged.collectAsStateWithLifecycle()
    val saveEnabled by viewModel.saveEnabled.collectAsStateWithLifecycle()

    var detailsExpanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (detailsExpanded) 180f else 0f,
        label = stringResource(R.string.card_animation)
    )

    var discardChangesDialogOpen = remember { mutableStateOf(false) }

    DiscardChangesDialog(discardChangesDialogOpen, navController)

    note?.let { noteData ->
        Scaffold(
            topBar = {
                AppNavBar(
                    title = stringResource(if (isNew) R.string.route_new_note else R.string.route_edit_note),
                    onNavIconClick = { navController.navigateBackWithDataCheck(dataChanged, discardChangesDialogOpen) },
                    actions = {
                        IconButton(
                            modifier = Modifier.rotate(rotationState),
                            onClick = { detailsExpanded = detailsExpanded.not() },
                        ) { DropDownIcon() }

                        IconButton(
                            onClick = { viewModel.saveNote() },
                            enabled = saveEnabled,
                        ) { SaveIcon() }
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
                if (detailsExpanded) NoteTopMenu(viewModel, noteData)

                Box(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)) {
                    OutlinedInputField(
                        modifier = Modifier.padding(
                            top = if (detailsExpanded) 0.dp else AppPadding.MD_PADDING,
                            start = AppPadding.MD_PADDING,
                            end = AppPadding.MD_PADDING,
                            bottom = AppPadding.MD_PADDING
                        ),
                        label = stringResource(R.string.note_title),
                        initialValue = noteData.title,
                        onValueChange = { title -> viewModel.updateTitle(title) },
                        maxLines = 5,
                        maxLength = 200
                    )
                }

                FullScreenTextArea(
                    placeholder = stringResource(R.string.write_here),
                    initialValue = noteData.content,
                    onValueChange = { name -> viewModel.updateContent(name) },
                )
            }
        }
    }
}

@Composable
private fun NoteTopMenu(viewModel: NoteViewModel, note: NoteModel) {
    val context = LocalContext.current

    val tutorialDone by viewModel.noteImportanceTutorialDone.collectAsStateWithLifecycle()

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
                .padding(top = AppPadding.MD_PADDING, start = AppPadding.MD_PADDING, end = AppPadding.MD_PADDING)
        ) {
            if (tutorialDone.not()) TutorialCard(
                modifier = Modifier.padding(bottom = AppPadding.INPUT_BOTTOM_PADDING),
                msg = stringResource(R.string.note_details_info),
                onConfirm = { viewModel.markNoteImportanceTutorialDone() }
            )

            OutlinedDropDown(
                modifier = Modifier.padding(top = AppPadding.SM_PADDING),
                label = stringResource(R.string.note_importance),
                initialValue = DropDownItem(note.importance, note.importance.getLabel(context)),
                onValueChange = { importance -> viewModel.updateImportance(importance.value as Importance) },
                items = Importance.entries.map { DropDownItem(it, it.getLabel(context)) }
            )
        }
    }
}