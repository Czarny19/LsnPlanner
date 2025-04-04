package com.lczarny.lsnplanner.presentation.ui.lessonplanlist

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
import com.lczarny.lsnplanner.presentation.model.ListScreenState
import com.lczarny.lsnplanner.presentation.ui.lessonplanlist.components.LessonPlanList

enum class ListPickerScreenSnackbar {
    SetActive,
    Deleted
}

@Composable
fun LessonPlanListScreen(navController: NavController, viewModel: LessonPlanListViewModel = hiltViewModel()) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        when (screenState) {
            ListScreenState.Loading -> FullScreenLoading(stringResource(R.string.please_wait))
            ListScreenState.List -> LessonPlanList(navController, viewModel)
        }
    }
}