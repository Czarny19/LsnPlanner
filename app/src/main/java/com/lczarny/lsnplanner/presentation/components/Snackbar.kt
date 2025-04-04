package com.lczarny.lsnplanner.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable

@Composable
fun SuccessSnackbar(snackbarData: SnackbarData) {
    Snackbar(
        snackbarData = snackbarData,
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onTertiary,
        dismissActionContentColor = MaterialTheme.colorScheme.onTertiary
    )
}