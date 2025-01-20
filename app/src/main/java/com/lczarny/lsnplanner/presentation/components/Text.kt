package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.lczarny.lsnplanner.presentation.constants.AppPadding

@Composable
fun FormSectionHeader(modifier: Modifier = Modifier, label: String) {
    Column(modifier = modifier, horizontalAlignment = Alignment.Start) {
        Text(label, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Start)
        HorizontalDivider(modifier = Modifier.padding(top = AppPadding.xsmPadding))
    }
}