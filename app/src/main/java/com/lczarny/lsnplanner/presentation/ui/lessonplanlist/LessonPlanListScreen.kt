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
import com.lczarny.lsnplanner.presentation.model.BasicScreenState
import com.lczarny.lsnplanner.presentation.ui.lessonplanlist.components.LessonPlanList

@Composable
fun LessonPlanListScreen(navController: NavController, viewModel: LessonPlanListViewModel = hiltViewModel()) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        when (screenState) {
            BasicScreenState.Loading -> FullScreenLoading(stringResource(R.string.please_wait))
            BasicScreenState.Ready -> LessonPlanList(navController, viewModel)
        }
    }
}