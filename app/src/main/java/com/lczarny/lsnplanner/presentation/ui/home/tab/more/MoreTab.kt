package com.lczarny.lsnplanner.presentation.ui.home.tab.more

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.data.common.model.LessonPlanModel
import com.lczarny.lsnplanner.presentation.components.AppIcons
import com.lczarny.lsnplanner.presentation.components.BasicDialogState
import com.lczarny.lsnplanner.presentation.components.ConfirmationDialog
import com.lczarny.lsnplanner.presentation.navigation.ClassListRoute
import com.lczarny.lsnplanner.presentation.navigation.LessonPlanListRoute
import com.lczarny.lsnplanner.presentation.navigation.LessonPlanRoute
import com.lczarny.lsnplanner.presentation.ui.home.HomeViewModel
import com.lczarny.lsnplanner.presentation.ui.home.components.HomeScreenSnackbar
import com.lczarny.lsnplanner.utils.convertMillisToSystemDate
import kotlinx.coroutines.channels.Channel

@Composable
fun MoreTab(padding: PaddingValues, navController: NavController, viewModel: HomeViewModel, snackbarChannel: Channel<HomeScreenSnackbar>) {
    val lessonPlan by viewModel.lessonPlan.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        UserItem(viewModel)

        lessonPlan?.let { lessonPlanData ->
            ActivePlanItem(navController, lessonPlanData)
            ManagePlansItem(navController)
            ManageClassesItem(navController)
        }

        TutorialsItem(viewModel, snackbarChannel)
        SignOutItem(viewModel)
    }
}


@Composable
private fun UserItem(viewModel: HomeViewModel) {
    val context = LocalContext.current
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    MoreTabItem(
        icon = { Icon(AppIcons.USER, contentDescription = stringResource(R.string.user)) },
        label = userProfile?.email ?: "",
        subtitle = stringResource(R.string.user_joined, userProfile?.joined?.convertMillisToSystemDate(context) ?: "")
    )
}

@Composable
private fun ActivePlanItem(navController: NavController, lessonPlan: LessonPlanModel) {
    MoreTabItem(
        icon = { Icon(AppIcons.PLAN, contentDescription = stringResource(R.string.plan_type)) },
        label = lessonPlan.name,
        subtitle = stringResource(R.string.plan_active_manage)
    ) {
        navController.navigate(LessonPlanRoute(lessonPlanId = lessonPlan.id!!))
    }
}

@Composable
private fun ManagePlansItem(navController: NavController) {
    MoreTabItem(
        icon = { Icon(AppIcons.LIST, contentDescription = stringResource(R.string.plan_list)) },
        label = stringResource(R.string.plan_list),
        subtitle = stringResource(R.string.plan_list_manage)
    ) {
        navController.navigate(LessonPlanListRoute)
    }
}

@Composable
private fun ManageClassesItem(navController: NavController) {
    MoreTabItem(
        icon = { Icon(AppIcons.CLASS, contentDescription = stringResource(R.string.class_list)) },
        label = stringResource(R.string.class_list),
        subtitle = stringResource(R.string.class_list_manage)
    ) {
        navController.navigate(ClassListRoute)
    }
}

@Composable
private fun TutorialsItem(viewModel: HomeViewModel, snackbarChannel: Channel<HomeScreenSnackbar>) {
    var confirmationDialogOpen by remember { mutableStateOf(false) }

    ConfirmationDialog(
        confirmationDialogOpen, BasicDialogState(
            title = stringResource(R.string.reset_tutorials),
            text = stringResource(R.string.reset_tutorials_msg),
            onConfirm = {
                confirmationDialogOpen = false
                viewModel.resetTutorials { snackbarChannel.trySend(HomeScreenSnackbar.ResetTutorials) }
            },
            onDismiss = { confirmationDialogOpen = false }
        )
    )

    MoreTabItem(
        icon = { Icon(AppIcons.RESET, contentDescription = stringResource(R.string.reset_tutorials)) },
        label = stringResource(R.string.reset_tutorials),
        onClick = { confirmationDialogOpen = true }
    )
}

@Composable
private fun SignOutItem(viewModel: HomeViewModel) {
    var confirmationDialogOpen by remember { mutableStateOf(false) }

    ConfirmationDialog(
        confirmationDialogOpen, BasicDialogState(
            title = stringResource(R.string.user_sign_out),
            text = stringResource(R.string.user_sign_out_msg),
            onConfirm = {
                confirmationDialogOpen = false
                viewModel.signOut()
            },
            onDismiss = { confirmationDialogOpen = false }
        )
    )

    MoreTabItem(
        icon = { Icon(AppIcons.SIGN_OUT, contentDescription = stringResource(R.string.user_sign_out)) },
        label = stringResource(R.string.user_sign_out),
        onClick = { confirmationDialogOpen = true }
    )
}