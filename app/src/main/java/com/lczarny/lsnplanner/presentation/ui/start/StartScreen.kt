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
import androidx.compose.material.icons.outlined.WavingHand
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.components.FullScreenLoading
import com.lczarny.lsnplanner.presentation.components.PrimaryButton
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes
import com.lczarny.lsnplanner.presentation.navigation.HomeRoute
import com.lczarny.lsnplanner.presentation.navigation.LessonPlanRoute
import com.lczarny.lsnplanner.presentation.theme.AppTheme

@Composable
fun StartScreen(navController: NavController, viewModel: StartViewModel = hiltViewModel()) {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            content = {
                val screenState by viewModel.screenState.collectAsStateWithLifecycle()

                when (screenState) {
                    StartScreenState.Loading -> FullScreenLoading(label = stringResource(R.string.please_wait))
                    StartScreenState.FirstLaunch -> FirstLaunchInfo(navController)
                    StartScreenState.StartApp -> {
                        navController.navigate(HomeRoute()) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun FirstLaunchInfo(navController: NavController) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(AppPadding.SCREEN_PADDING)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                modifier = Modifier.size(AppSizes.XL_ICON),
                imageVector = Icons.Outlined.WavingHand,
                contentDescription = stringResource(R.string.first_launch_welcome),
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                modifier = Modifier.padding(vertical = AppPadding.MD_PADDING),
                text = stringResource(R.string.first_launch_welcome),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
            Text(
                modifier = Modifier.padding(vertical = AppPadding.MD_PADDING),
                text = stringResource(R.string.first_launch_info),
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
            )
            PrimaryButton(
                modifier = Modifier
                    .padding(vertical = AppPadding.SCREEN_PADDING)
                    .fillMaxWidth(),
                text = stringResource(R.string.first_launch_create_plan),
                onClick = { navController.navigate(LessonPlanRoute(firstLaunch = true)) }
            )
        }
    }
}