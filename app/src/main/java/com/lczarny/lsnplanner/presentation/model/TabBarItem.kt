package com.lczarny.lsnplanner.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector

data class TabBarItem(
    val id: String,
    val label: String
)

data class TabBarItemWithIcon(
    val id: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)