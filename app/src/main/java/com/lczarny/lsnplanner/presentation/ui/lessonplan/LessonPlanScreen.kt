package com.lczarny.lsnplanner.presentation.ui.lessonplan

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
import com.lczarny.lsnplanner.model.DetailsScreenState
import com.lczarny.lsnplanner.presentation.navigation.HomeRoute
import com.lczarny.lsnplanner.presentation.ui.lessonplan.components.LessonPlanCreate
import com.lczarny.lsnplanner.presentation.ui.lessonplan.components.LessonPlanEdit

@Composable
fun LessonPlanScreen(
    navController: NavController,
    firstPlan: Boolean,
    lessonPlanId: Long?,
    viewModel: LessonPlanViewModel = hiltViewModel()
) {
    viewModel.initializePlan(lessonPlanId)

    Surface(modifier = Modifier.fillMaxSize()) {
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        when (screenState) {
            DetailsScreenState.Loading -> FullScreenLoading()
            DetailsScreenState.Create -> LessonPlanCreate(navController, viewModel, firstPlan)
            DetailsScreenState.Edit -> LessonPlanEdit(navController, viewModel)
            DetailsScreenState.Saving -> FullScreenLoading(stringResource(R.string.saving))
            DetailsScreenState.Finished -> navigateAfterFinished(navController, firstPlan)
        }
    }
}

private fun navigateAfterFinished(navController: NavController, firstPlan: Boolean) {
    if (firstPlan) {
        navController.navigate(HomeRoute) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    } else {
        navController.popBackStack()
    }
}