package com.lczarny.lsnplanner.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class TabBarItem(
    val id: String,
    val label: String
)

@Immutable
data class TabBarItemWithIcon(
    val id: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)