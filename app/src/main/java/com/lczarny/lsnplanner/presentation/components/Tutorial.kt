package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.lczarny.lsnplanner.R
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes

@Composable
fun TutorialCard(modifier: Modifier = Modifier, msg: String, onConfirm: () -> Unit) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = AppSizes.CARD_ELEVATION),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppPadding.SCREEN_PADDING),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Icon(
                    Icons.Outlined.QuestionMark,
                    modifier = Modifier.size(AppSizes.MD_ICON),
                    contentDescription = stringResource(R.string.tutorial),
                )
                Text(
                    msg,
                    modifier = Modifier.padding(start = AppPadding.SM_PADDING),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                )
            }
            PrimaryButton(
                modifier = Modifier.padding(end = AppPadding.MD_PADDING, bottom = AppPadding.SM_PADDING),
                variant = PrimaryButtonVariant.Alt,
                text = stringResource(R.string.got_it),
                onClick = onConfirm
            )
        }
    }
}