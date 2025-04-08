package com.lczarny.lsnplanner.utils

import androidx.compose.runtime.MutableState
import androidx.navigation.NavController

fun NavController.navigateBackWithDataCheck(dataChanged: Boolean, discardChangesDialogOpen: MutableState<Boolean>) {
    if (dataChanged) {
        discardChangesDialogOpen.value = true
    } else {
        this.popBackStack()
    }
}