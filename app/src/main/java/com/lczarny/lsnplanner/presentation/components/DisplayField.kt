package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lczarny.lsnplanner.presentation.constants.AppPadding

@Composable
fun DisplayField(modifier: Modifier = Modifier, label: String? = null, text: String, icon: @Composable () -> Unit) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(AppPadding.SCREEN_PADDING)
                .padding(start = AppPadding.SM_PADDING),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            icon.invoke()

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = AppPadding.LG_PADDING),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                label?.let {
                    Text(label, style = MaterialTheme.typography.titleSmall)
                    Text(text, style = MaterialTheme.typography.bodyMedium)
                } ?: run {
                    Text(text, modifier = Modifier.padding(vertical = AppPadding.MD_PADDING), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}