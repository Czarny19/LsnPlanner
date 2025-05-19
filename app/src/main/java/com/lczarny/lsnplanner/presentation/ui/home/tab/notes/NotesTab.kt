package com.lczarny.lsnplanner.presentation.ui.home.tab.notes

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.components.EmptyList
import com.lczarny.lsnplanner.presentation.components.FabListBottomSpacer
import com.lczarny.lsnplanner.presentation.components.TutorialCard
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.ui.home.HomeViewModel

@Composable
fun NotesTab(padding: PaddingValues, navController: NavController, viewModel: HomeViewModel) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    val tutorialDone by viewModel.noteListSwipeTutorialDone.collectAsStateWithLifecycle()

    if (notes.isEmpty()) {
        EmptyList(stringResource(R.string.note_list_empty_hint))
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
    ) {
        if (tutorialDone.not()) {
            item {
                TutorialCard(
                    modifier = Modifier.padding(AppPadding.MD_PADDING),
                    msg = stringResource(R.string.tutorial_note_swipe),
                    onConfirm = { viewModel.markNoteListSwipeTutorialDone() }
                )
            }
        }

        items(items = notes, key = { it.id!! }) { item -> NotesTabItem(navController, viewModel, item) }

        item { FabListBottomSpacer() }
    }
}