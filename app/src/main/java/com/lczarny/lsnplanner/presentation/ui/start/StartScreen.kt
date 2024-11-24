package com.lczarny.lsnplanner.presentation.ui.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotStarted
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.components.FullScreenLoading
import com.lczarny.lsnplanner.presentation.components.PrimaryButton
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes
import com.lczarny.lsnplanner.presentation.navigation.HomeRoute
import com.lczarny.lsnplanner.presentation.navigation.LessonPlanRoute
import com.lczarny.lsnplanner.presentation.theme.AppTheme
import com.lczarny.lsnplanner.presentation.ui.start.model.StartState

@Composable
fun StartScreen(navController: NavController, viewModel: StartViewModel = hiltViewModel()) {
    val screenState by viewModel.screenState.collectAsState()

    AppTheme(
        content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
                content = {
                    when (screenState) {
                        StartState.Loading -> FullScreenLoading(label = stringResource(R.string.please_wait))
                        StartState.FirstLaunch -> FirstLaunchInfo(navController)
                        StartState.StartApp -> {
                            navController.navigate(HomeRoute()) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            )
        }
    )
}

@Composable
fun FirstLaunchInfo(navController: NavController) {
    Scaffold(
        bottomBar = {
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppPadding.screenPadding),
                text = stringResource(R.string.first_launch_create_plan),
                onClick = { navController.navigate(LessonPlanRoute(firstLaunch = true)) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(AppPadding.screenPadding)
                .padding(top = AppPadding.xlPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                modifier = Modifier
                    .padding(AppPadding.mdPadding)
                    .size(AppSizes.xlIcon),
                imageVector = Icons.Outlined.NotStarted,
                contentDescription = stringResource(R.string.information),
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(R.string.first_launch_welcome),
                modifier = Modifier.padding(AppPadding.mdPadding),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(R.string.first_launch_info),
                modifier = Modifier.padding(horizontal = AppPadding.mdPadding),
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
            )
        }
    }
}