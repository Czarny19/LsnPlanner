package com.lczarny.lsnplanner.presentation.ui.planclass

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun PlanClassScreen(
    navController: NavController,
    lessonPlanId: Long,
    classId: Long? = null,
    viewModel: PlanClassViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsState()
}