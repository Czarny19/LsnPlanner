package com.lczarny.lsnplanner.presentation.ui.classdetails

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
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
import com.lczarny.lsnplanner.presentation.ui.classdetails.components.ClassDetailsPager

@Composable
fun ClassDetailsScreen(
    navController: NavController,
    defaultWeekDay: Int,
    classId: Long? = null,
    viewModel: ClassDetailsViewModel = hiltViewModel(),
) {
    viewModel.initializeClass(defaultWeekDay, classId)

    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        content = {
            when (screenState) {
                DetailsScreenState.Loading -> FullScreenLoading()
                DetailsScreenState.Create -> ClassDetailsPager(navController, viewModel, true)
                DetailsScreenState.Edit -> ClassDetailsPager(navController, viewModel, false)
                DetailsScreenState.Saving -> FullScreenLoading(stringResource(R.string.saving))
                DetailsScreenState.Finished -> navController.popBackStack()
            }
        }
    )
}