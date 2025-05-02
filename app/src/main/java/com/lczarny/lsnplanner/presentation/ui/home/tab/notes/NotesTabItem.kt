package com.lczarny.lsnplanner.presentation.ui.home.tab.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.common.model.Importance
import com.lczarny.lsnplanner.data.common.model.NoteModel
import com.lczarny.lsnplanner.presentation.components.AppIcons
import com.lczarny.lsnplanner.presentation.components.BasicDialogState
import com.lczarny.lsnplanner.presentation.components.ConfirmationDialog
import com.lczarny.lsnplanner.presentation.components.DraggableCard
import com.lczarny.lsnplanner.presentation.components.DraggableCardAction
import com.lczarny.lsnplanner.presentation.components.InfoChip
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.model.mapper.getColor
import com.lczarny.lsnplanner.presentation.model.mapper.getIcon
import com.lczarny.lsnplanner.presentation.model.mapper.getLabel
import com.lczarny.lsnplanner.presentation.navigation.NoteRoute
import com.lczarny.lsnplanner.presentation.ui.home.HomeViewModel
import com.lczarny.lsnplanner.utils.convertMillisToSystemDateTime

@Composable
fun NotesTabItem(navController: NavController, viewModel: HomeViewModel, note: NoteModel) {
    val context = LocalContext.current

    var confirmationDialogOpen by remember { mutableStateOf(false) }

    ConfirmationDialog(
        confirmationDialogOpen, BasicDialogState(
            title = context.getString(R.string.note_delete),
            text = context.getString(R.string.note_delete_question),
            onConfirm = {
                confirmationDialogOpen = false
                viewModel.deleteNote(note.id!!)
            },
            onDismiss = { confirmationDialogOpen = false }
        )
    )

    DraggableCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RectangleShape)
            .padding(AppPadding.MD_PADDING),
        clickAction = { navController.navigate(NoteRoute(noteId = note.id)) },
        endAction = DraggableCardAction(
            color = MaterialTheme.colorScheme.error,
            imageVector = AppIcons.DELETE,
            contentDescription = stringResource(R.string.delete),
            label = stringResource(R.string.delete),
            onClick = { confirmationDialogOpen = true }
        ),
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier.padding(AppPadding.MD_PADDING),
            verticalArrangement = Arrangement.spacedBy(AppPadding.MD_PADDING),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    note.title,
                    modifier = Modifier.weight(1.0f),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(note.modifyDate.convertMillisToSystemDateTime(context), style = MaterialTheme.typography.bodyMedium)
            }

            note.importance.let {
                if (it != Importance.Normal) {
                    InfoChip(
                        modifier = Modifier.align(Alignment.End),
                        label = it.getLabel(context),
                        imageVector = it.getIcon(),
                        color = it.getColor()
                    )
                }
            }

            HorizontalDivider()

            Text(
                note.content,
                maxLines = 5,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}