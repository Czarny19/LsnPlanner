package com.lczarny.lsnplanner.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lczarny.lsnplanner.presentation.constants.AppPadding
import com.lczarny.lsnplanner.presentation.constants.AppSizes

@Composable
@Stable
fun FullScreenLoading(label: String? = null) {
    Column(
        modifier = Modifier.padding(AppPadding.MD_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(modifier = Modifier.width(AppSizes.XL_ICON))
        label?.let {
            Text(
                it,
                modifier = Modifier.padding(top = AppPadding.XL_PADDING),
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}