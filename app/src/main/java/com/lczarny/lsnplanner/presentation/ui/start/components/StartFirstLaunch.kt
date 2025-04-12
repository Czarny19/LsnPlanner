package com.lczarny.lsnplanner.presentation.ui.start.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.components.AppIcons
import com.lczarny.lsnplanner.presentation.components.OutlinedInputField
import com.lczarny.lsnplanner.presentation.components.PrimaryButton
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes
import com.lczarny.lsnplanner.presentation.ui.start.StartViewModel

@Composable
fun StartFirstLaunch(viewModel: StartViewModel) {
    val startEnabled by viewModel.startEnabled.collectAsStateWithLifecycle()

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
                AppIcons.WELCOME,
                modifier = Modifier
                    .padding(top = AppPadding.LG_PADDING)
                    .size(AppSizes.XL_ICON),
                contentDescription = stringResource(R.string.first_launch_welcome),
                tint = MaterialTheme.colorScheme.primary,
            )

            Text(
                stringResource(R.string.first_launch_welcome),
                modifier = Modifier.padding(vertical = AppPadding.MD_PADDING),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )

            Text(
                stringResource(R.string.first_launch_info),
                modifier = Modifier.padding(vertical = AppPadding.MD_PADDING),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )

            OutlinedInputField(
                modifier = Modifier.padding(vertical = AppPadding.MD_PADDING),
                label = stringResource(R.string.user_name),
                onValueChange = { userName -> viewModel.updateUserName(userName) },
                maxLength = 100
            )

            PrimaryButton(
                modifier = Modifier
                    .padding(vertical = AppPadding.MD_PADDING)
                    .fillMaxWidth(),
                text = stringResource(R.string.first_launch_create_plan),
                enabled = startEnabled,
                onClick = { viewModel.save() }
            )
        }
    }
}