package com.lczarny.lsnplanner.presentation.ui.note

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.components.FullScreenLoading
import com.lczarny.lsnplanner.presentation.model.DetailsScreenState
import com.lczarny.lsnplanner.presentation.ui.note.components.NoteEdit

@Composable
fun NoteScreen(navController: NavController, noteId: Long?, viewModel: NoteViewModel = hiltViewModel()) {
    viewModel.intializeNote(noteId)

    Surface(modifier = Modifier.fillMaxSize()) {
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        when (screenState) {
            DetailsScreenState.Loading -> FullScreenLoading(stringResource(R.string.please_wait))
            DetailsScreenState.Create -> NoteEdit(navController, viewModel, true)
            DetailsScreenState.Edit -> NoteEdit(navController, viewModel, false)
            DetailsScreenState.Saving -> FullScreenLoading(stringResource(R.string.saving))
            DetailsScreenState.Finished -> navController.popBackStack()
        }
    }
}