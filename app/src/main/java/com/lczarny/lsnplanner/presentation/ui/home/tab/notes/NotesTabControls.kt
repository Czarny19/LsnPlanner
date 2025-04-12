package com.lczarny.lsnplanner.presentation.ui.home.tab.notes

import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.components.AddIcon
import com.lczarny.lsnplanner.presentation.navigation.NoteRoute

@Composable
fun NotesTabFab(navController: NavController) {
    FloatingActionButton(
        onClick = { navController.navigate(NoteRoute()) },
        content = { AddIcon(contentDescription = stringResource(R.string.note_add)) }
    )
}