package com.lczarny.lsnplanner.presentation.ui.start

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.lczarny.lsnplanner.presentation.components.FullScreenLoading
import com.lczarny.lsnplanner.presentation.model.StartScreenState
import com.lczarny.lsnplanner.presentation.navigation.HomeRoute
import com.lczarny.lsnplanner.presentation.navigation.LessonPlanRoute
import com.lczarny.lsnplanner.presentation.ui.start.components.StartFirstLaunch

@Composable
fun StartScreen(navController: NavController, viewModel: StartViewModel = hiltViewModel()) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val screenState by viewModel.screenState.collectAsStateWithLifecycle()

        when (screenState) {
            StartScreenState.Loading -> FullScreenLoading()
            StartScreenState.FirstLaunch -> StartFirstLaunch(viewModel)
            StartScreenState.StartApp -> navigateToHome(navController)
            StartScreenState.UserNameSaved -> navigateToLessonPlanCreation(navController)
        }
    }
}

private fun navigateToHome(navController: NavController) {
    navController.navigate(HomeRoute()) {
        popUpTo(navController.graph.id) { inclusive = true }
    }
}

private fun navigateToLessonPlanCreation(navController: NavController) {
    navController.navigate(LessonPlanRoute(firstLaunch = true))
}